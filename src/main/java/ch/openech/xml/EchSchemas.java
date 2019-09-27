package ch.openech.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.metamodel.model.MjEntity.MjEntityType;
import org.minimalj.metamodel.model.MjModel;
import org.minimalj.metamodel.model.MjProperty;
import org.minimalj.metamodel.model.MjProperty.MjPropertyType;
import org.minimalj.util.StringUtils;

import ch.openech.model.DatePartiallyKnown;
import ch.openech.model.EchSchemaValidation;
import ch.openech.model.UidStructure;
import ch.openech.model.YesNo;
import ch.openech.xml.write.EchNamespaceUtil;

public class EchSchemas {
	private static Logger LOG = Logger.getLogger(EchSchemas.class.getName());

	// namespace -> xsd file name in class path
	private static Map<String, String> fileByNamespace = new HashMap<>();

	// package -> namespace
	private static Map<String, String> namespaceByPackage = new HashMap<>();

	// namespace -> XsdModel
	private static Map<String, XsdModel> xsdModels = new HashMap<>();

	public static String getNamespaceByPackage(String packageName) {
		return namespaceByPackage.get(packageName);
	}
	
	private static final HashSet<MjEntity> updated = new HashSet<>();

	private static void read(Stream<String> xsdFiles) {
		xsdFiles.forEach(f -> {
			try (InputStream is = EchSchemas.class.getClassLoader().getResourceAsStream(f)) {
				if (is == null) {
					LOG.warning("Xsd not available in classpath: " + f);
				} else {
					XsdModel model = new XsdModel(is);
					xsdModels.put(model.getNamespace(), model);
					LOG.fine("Read names of " + model.getNamespace());
				}
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "Could not read: " + f);
			}
		});

		List<XsdModel> sortedModels = new ArrayList<>(xsdModels.values());
		sortedModels.sort((m1, m2) -> m1.getNamespace().compareTo(m2.getNamespace()));

		for (XsdModel model : sortedModels) {
			model.read(xsdModels);
			LOG.fine("Read entities of " + model.getNamespace());

			String packageName = packageName(model.getNamespace());
			model.getEntities().forEach(e -> e.packageName = packageName);

			model.getEntities().forEach(e -> e.name = EchClassNameGenerator.apply(e));
			
			namespaceByPackage.put(packageName, model.getNamespace());
		}
		
		applyHandMadeChanges();

		Set<String> collapsed = new TreeSet<>();
		sortedModels.forEach(m -> collapseToOlderVersion(m, xsdModels, collapsed));
		
		for (XsdModel model : xsdModels.values()) {
			MjModel mjModel = new MjModel();
			model.getEntities().forEach(entity -> mjModel.addEntity(entity));
		}
	}

	// entities, die sich zwischen den Versionen nicht geändert haben sollen wiederverwendet werden
	private static void collapseToOlderVersion(XsdModel model, Map<String, XsdModel> xsdModels, Set<String> collapsed) {
		String namespace = model.getNamespace();
		if (collapsed.contains(namespace)) return;
		collapsed.add(namespace);

		for (String dependencyNamespace : model.getNamespaceByPrefix().values()) {
			XsdModel dependency = xsdModels.get(dependencyNamespace);
			if (dependency != null) {
				collapseToOlderVersion(dependency, xsdModels, collapsed);
			}
		}
		
		int schemaNumber = EchNamespaceUtil.extractSchemaNumber(namespace);
		int majorVersion = EchNamespaceUtil.extractSchemaMajorVersion(namespace);

		for (int version = majorVersion - 1; version >= 0; version--) {
			String previousVersionNamespace = EchNamespaceUtil.schemaURI(schemaNumber, "" + version);

			XsdModel previousModel = xsdModels.get(previousVersionNamespace);
			if (previousModel == null) {
				continue;
			}
			
			for (XsdMjEntity entity : model.getEntities()) {
				XsdMjEntity previousEntity = findEntity(previousModel, entity.name);
				if (sameSignatore(entity, previousEntity)) {
					previousEntity.packageName = entity.packageName;
				}
			}
		}
	}

	private static boolean sameSignatore(MjEntity entity, MjEntity previousEntity) {
		if (previousEntity == null || previousEntity.type != entity.type) {
			return false;
		}

		if (previousEntity.isEnumeration() != entity.isEnumeration()) {
			return false;
		}
		if (previousEntity.isEnumeration() && entity.isEnumeration()) {
			return Arrays.equals(previousEntity.values.toArray(), entity.values.toArray());
		}

		if (previousEntity.properties.size() != entity.properties.size()) {
			return false;
		}
		
		for (int i = 0; i<previousEntity.properties.size(); i++) {
			MjProperty property = entity.properties.get(i);
			MjProperty previousProperty = previousEntity.properties.get(i);
			if (!StringUtils.equals(property.name, previousProperty.name)) {
				return false;
			}
			if (!sameSignatore(property.type, previousProperty.type)) {
				return false;
			}
			if (!Objects.equals(property.size, previousProperty.size)) {
				return false;
			}
		}
		
		return true;
	}

	private static XsdMjEntity findEntity(XsdModel model, String name) {
		for (XsdMjEntity entity : model.getEntities()) {
			if (name.equals(entity.getClassName())) {
				return entity;
			}
		}
		return null;
	}

	public static Collection<XsdModel> getXsdModels() {
		return xsdModels.values();
	}
	
	public static XsdModel getXsdModel(String namespace) {
		return xsdModels.get(namespace);
	}

	private static XsdModel getXsdModel(int number) {
		for (Map.Entry<String, XsdModel> entry : xsdModels.entrySet()) {
			int n = EchNamespaceUtil.extractSchemaNumber(entry.getKey());
			if (number == n) {
				return entry.getValue();
			}
		}
		throw new IllegalArgumentException("Not available: " + number);
	}

	public static String packageName(String namespace) {
		StringBuilder s = new StringBuilder();
		URI uri = URI.create(namespace);
		
		String host[] = uri.getHost().split("\\.");
		for (String hostElement : host) {
			if (StringUtils.equals(hostElement, "www")) continue;
			if (s.length() != 0) s.insert(0, '.');
			s.insert(0, hostElement);
		}
		
		int schemaNumber = EchNamespaceUtil.extractSchemaNumber(namespace);
		String path[] = uri.getPath().split("/");
		for (String pathElement : path) {
			if (StringUtils.equals(pathElement, "xmlns", "")) continue;
			pathElement = pathElement.toLowerCase().replace("-", "");
			if (Character.isDigit(pathElement.charAt(0))) {
				if (schemaNumber == 20 || schemaNumber == 116 || schemaNumber == 211) {
					s.append(".v");
				} else {
					continue;
				}
			} else {
				s.append(".");
			}
			s.append(pathElement);
		}
		
		return s.toString();
	}
	
	public static boolean filter(MjEntity entity) {
		String name = entity.getClassName();
		if (name == null || entity.getPackageName() == null) {
			return false;
		}
		boolean skip = //
				name.equals(DatePartiallyKnown.class.getSimpleName()) || //
						name.contains("Named") && name.contains("Id") || //
						name.equals(YesNo.class.getSimpleName()) || name.equals("PaperLock") || //
						name.startsWith(UidStructure.class.getSimpleName()) || //
						// die Extension von ech 0078 ist komplett etwas anderes als bei ech 0020
						name.equals("Extension") && !entity.getPackageName().equals("ch.ech.ech0078") && !entity.getPackageName().equals("ch.ech.ech0155") || //
						// Kantone/Gemeinden werden von ech 0071 verwendet
						entity.getPackageName().equals("ch.ech.ech0007") ||
						// Länder werden von ech 0072 verwendet (CountryInformation)
						entity.getPackageName().equals("ch.ech.ech0008") || entity.getClassName().equals("Country") ||
						// Adressen sind ausser der enum von Hand neu geschrieben
						entity.getPackageName().equals("ch.ech.ech0010") && !(entity.getClassName().equals("MrMrs")) ||
						// bei den PersonAddon sind die Versionen 3 und 4 recht unterschiedlich
						// das anzugleichen macht viel vergebliche Mühe, diese Elemente werden
						// kaum je eigenständig gebrauchtw werden.
						name.startsWith("PersonAddon") && entity.getPackageName().startsWith("ch.ech.ech0021") || //
						name.equals("NameOfParentAtBirth") || //
						// Es wird immer die Destination von ech0011 verwendet
						name.equals("Destination") && entity.getPackageName().equals("ch.ech.ech0098") || //
						// PersonIdentificationLight und PersonIdentification sind zusammengefasst
						name.equals("PersonIdentificationLight") || //
//						// Die Person enthält alle Element der BaseDeliveryPerson
//						name.equals("BaseDeliveryPerson") || //
						// Wird fehlerhaft generiert, aber nirgends verwendet, kann übersprungen werden
						name.equals("ElectoralAddress") || //
						// In neueren 0021 schemas existieren nur noch die Spezialisierungen
						name.equals("Relationship") && entity.getPackageName().startsWith("ch.ech.ech0021") || //
						
						false;
		return !skip;
	}

	static {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(EchSchemaValidation.class.getClassLoader().getResourceAsStream("catalog.txt")))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("=");
				fileByNamespace.put(parts[0].trim(), parts[1].trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Stream<String> filesToRead = fileByNamespace.values().stream().filter(f -> !f.endsWith("f.xsd"));
		read(filesToRead);
	}

	public static String getFile(String namespace) {
		return fileByNamespace.get(namespace);
	}

	private static void applyHandMadeChanges() {
		for (XsdModel model : xsdModels.values()) {
			model.getEntities().forEach(EchSchemas::updateEntityType);
			model.getEntities().forEach(EchSchemas::checkForMissingSizes);
		}
	}

	private static void updateEntityType(MjEntity entity) {
		if (updated.contains(entity)) {
			return;
		}
		updated.add(entity);

		if (entity.type == MjEntityType.ENTITY && entity.getPackageName().equals("ch.ech.ech0129.v4")) {
			// bei 129 sind einige complexType auch als Element aufgeführt, wie z.B.
			// Locality. Das ist unnötig und bewirkt, dass die types nicht inlined werden.
			if (!StringUtils.equals(entity.getClassName(), "Building", "Dwelling", "RealEstate", "Street")) {
				entity.type = MjEntityType.DEPENDING_ENTITY;
			}
		}

		// Depending Entities enthalten keine 'id' und können daher nicht als
		// parents für Listen herhalten
		boolean hasListProperty = entity.properties.stream().anyMatch(p -> p.propertyType == MjPropertyType.LIST);
		if (hasListProperty) {
			// Eine Ausnahme sind entities, die nur als inline verwendet werden.
			// diese müssen zur Zeit von Hand herausgefiltert werden
			if (!(StringUtils.equals(entity.getClassName(), "NationalityData") && entity.getPackageName().contains("ech0011"))) {
				entity.type = MjEntityType.ENTITY;
			}
		}

		for (MjProperty property : entity.properties) {
			if ("CantonAbbreviation".equals(property.type.getClassName())) {
				property.type = getXsdModel(71).findEntity("cantonAbbreviationType");
			}
			updateEntityType(property.type);
		}
	}

	private static void checkForMissingSizes(MjEntity entity) {
		for (MjProperty property : entity.properties) {
			if (property.size == null) {
				if (property.type.type == MjEntityType.String) {
					if (property.name.equals("uuid")) {
						property.size = 36;
					}
				} else if (property.type.type == MjEntityType.Integer) {
					if (property.name.equals("historyMunicipalityId")) {
						property.size = 5;
					}
				}
			} 
		}
	}
	
}
