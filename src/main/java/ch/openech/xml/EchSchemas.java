package ch.openech.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.minimalj.metamodel.generator.ClassGenerator;
import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.metamodel.model.MjEntity.MjEntityType;
import org.minimalj.metamodel.model.MjModel;
import org.minimalj.metamodel.model.MjProperty;
import org.minimalj.metamodel.model.MjProperty.MjPropertyType;
import org.minimalj.util.StringUtils;

import ch.openech.xml.write.EchNamespaceUtil;

public class EchSchemas {
	private static Logger LOG = Logger.getLogger(EchSchemas.class.getName());

	// package -> namespace
	private static Map<String, String> namespaceByPackage = new HashMap<>();

	// namespace -> MjModel
	private static Map<String, MjModel> models = new HashMap<>();
	private static Map<String, XsdModel> xsdModels = new HashMap<>();

	public static String getNamespaceByPackage(String packageName) {
		return namespaceByPackage.get(packageName);
	}
	
	private static void readDirectory(File dir) {
		List<File> xsdFiles = new ArrayList<>();
		scanDirectory(dir, xsdFiles);
		read(xsdFiles);
	}

	private static void scanDirectory(File dir, List<File> files) {
		Arrays.stream(dir.listFiles()).filter(File::isDirectory).forEach(subDir -> scanDirectory(subDir, files));
		Arrays.stream(dir.listFiles())
				.filter(f -> f.getName().endsWith(".xsd") && !f.getName().endsWith("f.xsd") && !f.isDirectory())
				.forEach(files::add);
	}

	private static void read(List<File> xsdFiles) {
		for (File file : xsdFiles) {
			try (FileInputStream fis = new FileInputStream(file)) {
				XsdModel model = new XsdModel(fis);
				xsdModels.put(model.getNamespace(), model);
				LOG.fine("Read names of " + model.getNamespace());
			} catch (FileNotFoundException e) {
				LOG.log(Level.SEVERE, "Konnte xsd nicht finden: " + file.getName(), e);
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "Konnte xsd nicht lesen: " + file.getName(), e);
			}
		}

		List<XsdModel> sortedModels = new ArrayList<>(xsdModels.values());
		sortedModels.sort((m1, m2) -> m1.getNamespace().compareTo(m2.getNamespace()));

		for (XsdModel model : sortedModels) {
			model.read(xsdModels);
			LOG.info("Read entities of " + model.getNamespace());

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
			models.put(model.getNamespace(), mjModel);
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
			
			for (MjEntity entity : model.getEntities()) {
				MjEntity previousEntity = findEntity(previousModel, entity.name);
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

	private static MjEntity findEntity(XsdModel model, String name) {
		for (MjEntity entity : model.getEntities()) {
			if (name.equals(entity.name)) {
				return entity;
			}
		}
		return null;
	}

	public static Collection<MjModel> getModels() {
		return models.values();
	}
	
	public static MjModel getModel(String namespace) {
		return models.get(namespace);
	}
	
	public static Collection<XsdModel> getXsdModels() {
		return xsdModels.values();
	}
	
	public static XsdModel getXsdModel(String namespace) {
		return xsdModels.get(namespace);
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
				if (schemaNumber == 20 || schemaNumber == 211) {
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
		boolean skip = //
				name.equals(DatePartiallyKnown.class.getSimpleName()) || // 
				name.contains("Named") && name.contains("Id") || //
				name.equals(YesNo.class.getSimpleName()) || //
				name.startsWith(UidStructure.class.getSimpleName()) || //
				// die Extension von ech 0078 ist komplett etwas anderes als bei ech 0020
				name.equals("Extension") && !entity.packageName.equals("ch.ech.ech0078" )|| //
				// bei den PersonAddon sind die Versionen 3 und 4 recht unterschiedlich
				// das anzugleichen macht viel vergebliche Mühe, diese Elemente werden
				// kaum je eigenständig gebrauchtw werden.
				name.startsWith("PersonAddon") && entity.packageName.startsWith("ch.ech.ech0021") || //
				name.equals("NameOfParentAtBirth") || //
				false;
		return !skip;
	}
	
	static {
		File dir = new File("./src/main/xml");
		readDirectory(dir);
	}

	private static void applyHandMadeChanges() {
		for (XsdModel model : xsdModels.values()) {
			model.getEntities().forEach(EchSchemas::updateEntityType);
			model.getEntities().forEach(EchSchemas::checkForMissingSizes);
		}
	}

	private static void updateEntityType(MjEntity entity) {
		if (entity.type == MjEntityType.ENTITY && entity.packageName.equals("ch.ech.ech0129.v4")) {
			// bei 129 sind einige complexType auch als Element aufgeführt, wie z.B.
			// Locality. Das ist unnötig und bewirkt, dass die types nicht inlined werden.
			if (!StringUtils.equals(entity.name, "Building", "Dwelling", "RealEstate", "Street")) {
				entity.type = MjEntityType.DEPENDING_ENTITY;
			}
		}
		// Depending Entities enthalten keine 'id' und können daher nicht als
		// parents für Listen herhalten
		
		boolean hasListProperty = entity.properties.stream().anyMatch(p -> p.propertyType == MjPropertyType.LIST);
		if (hasListProperty) {
			// Eine Ausnahme sind entities, die nur als inline verwendet werden.
			// diese müssen zur Zeit von Hand herausgefiltert werden
			if (!(StringUtils.equals(entity.name, "NationalityData") && entity.packageName.contains("ech0011"))) {
				entity.type = MjEntityType.ENTITY;
			}
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
	
	public static void main(String[] args) throws Exception {
		ClassGenerator generator = new ClassGenerator("./src/main/generated");

		List<XsdModel> sortedModels = new ArrayList<>(xsdModels.values());
		sortedModels.sort((m1, m2) -> m1.getNamespace().compareTo(m2.getNamespace()));

		for (XsdModel model : sortedModels) {
			Collection<MjEntity> entities = model.getEntities();
			entities = entities.stream().filter(EchSchemas::filter).collect(Collectors.toList());
			generator.generate(entities);
		}
	}
	
}
