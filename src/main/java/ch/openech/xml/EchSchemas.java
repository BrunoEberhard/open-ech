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

	// namespace -> package
	private static Map<String, String> packageByNamespace = new HashMap<>();
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
		xsdFiles = removeMinorVersion(xsdFiles);
		generate(xsdFiles);
	}

	private static void scanDirectory(File dir, List<File> files) {
		Arrays.stream(dir.listFiles()).filter(File::isDirectory).forEach(subDir -> scanDirectory(subDir, files));
		Arrays.stream(dir.listFiles())
				.filter(f -> f.getName().endsWith(".xsd") && !f.getName().endsWith("f.xsd") && !f.isDirectory())
				.forEach(files::add);
	}

	private static void generate(List<File> xsdFiles) {
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

		for (XsdModel model : xsdModels.values()) {
			model.read(xsdModels);
			LOG.info("Read entities of " + model.getNamespace());

			String packageName = packageName(model.getNamespace());
			model.getEntities().forEach(e -> e.packageName = packageName);

			model.getEntities().forEach(e -> e.name = convertName(e.name));
			
			namespaceByPackage.put(packageName, model.getNamespace());
			packageByNamespace.put(model.getNamespace(), packageName);
		}

		List<XsdModel> sortedModels = new ArrayList<>(xsdModels.values());
		// sortedModels.sort(new ModelOrder());
		
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
		for (int version = 2; version <= majorVersion; version++) {
			String versionNamespace = EchNamespaceUtil.schemaURI(schemaNumber, "" + version);
			String previousVersionNamespace = EchNamespaceUtil.schemaURI(schemaNumber, "" + (version-1));

			XsdModel m = xsdModels.get(versionNamespace);
			XsdModel previousModel = xsdModels.get(previousVersionNamespace);
			if (previousModel == null || m == null) {
				continue;
			}
			
			for (MjEntity entity : m.getEntities()) {
				MjEntity previousEntity = findEntity(previousModel, entity.name);
				if (sameSignatore(entity, previousEntity)) {
					entity.packageName = previousEntity.packageName;
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

		if (StringUtils.equals(previousEntity.minInclusive, entity.minInclusive)) {
			return false;
		}
		if (StringUtils.equals(previousEntity.maxInclusive, entity.maxInclusive)) {
			return false;
		}
		if (Objects.equals(previousEntity.minLength, entity.minLength)) {
			return false;
		}
		if (Objects.equals(previousEntity.maxLength, entity.maxLength)) {
			return false;
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

	// eine neue minor version wird nur publiziert, wenn sie vollständig rückwärtskompatibel ist.
	// Daher könnere frühere minor Version (der gleichen major Version) einfach ausgefiltert werden
	private static List<File> removeMinorVersion(List<File> xsdFiles) {
		Set<String> names = new TreeSet<>(xsdFiles.stream().map(file -> file.getName()).collect(Collectors.toSet()));
		List<File> result = new ArrayList<>();
		for (File file : xsdFiles) {
			if (!hasNewerVersion(file.getName(), names)) {
				result.add(file);
			}
		}
		return result;
	}

	private static boolean hasNewerVersion(String test, Collection<String> names) {
		int minorVersion = EchNamespaceUtil.extractSchemaMinorVersion(test);
		if (minorVersion >= 0) {
			int majorVersion = EchNamespaceUtil.extractSchemaMajorVersion(test);
			int schemaNumber = EchNamespaceUtil.extractSchemaNumber(test);
			if (majorVersion > 0 && schemaNumber > 0) {
				for (String name : names) {
					int thisSchemaNumber = EchNamespaceUtil.extractSchemaNumber(name);
					if (thisSchemaNumber == schemaNumber) {
						int thisMajorVersion = EchNamespaceUtil.extractSchemaMajorVersion(name);
						if (thisMajorVersion == majorVersion) {
							int thisMinorVersion = EchNamespaceUtil.extractSchemaMinorVersion(name);
							if (thisMinorVersion > minorVersion) {
								LOG.fine("skip " + schemaNumber + "-" + majorVersion + "." +  minorVersion);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public static String convertName(String name) {
		if (StringUtils.isEmpty(name)) {
			return name;
		}
		if (name.endsWith("Type")) {
			name = name.substring(0, name.length() - 4);
		}
		return StringUtils.upperFirstChar(name);
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
		
		String path[] = uri.getPath().split("/");
		for (String pathElement : path) {
			if (StringUtils.equals(pathElement, "xmlns", "")) continue;
			s.append(".");
			pathElement = pathElement.toLowerCase().replace("-", "");
			if (Character.isDigit(pathElement.charAt(0))) s.append("v");
			s.append(pathElement);
		}
		
		return s.toString();
	}
	
	static {
		File dir = new File("./src/main/xml");
		readDirectory(dir);
	}
	
	public static boolean filter(MjEntity entity) {
		String name = entity.getClassName();
		return !name.endsWith("DatePartiallyKnown") &&
				!(name.contains("Named") &&  name.contains("Id"));
	}
	
	private static void updateType(MjEntity entity) {
		if (entity.type == MjEntityType.ENTITY && entity.packageName.equals("ch.ech.ech0129.v4")) {
			// bei 129 sind einige complexType auch als Element aufgeführt, wie z.B.
			// Locality. Das ist unnötig und bewirkt, dass die types nicht inlined werden.
			if (!StringUtils.equals(entity.name, "Building", "Dwelling", "RealEstate", "Street")) {
				entity.type = MjEntityType.DEPENDING_ENTITY;
			}
		}
		boolean hasListProperty = entity.properties.stream().anyMatch(p -> p.propertyType == MjPropertyType.LIST);
		if (hasListProperty) {
			entity.type = MjEntityType.ENTITY;
		}
	}
	
	private static void checkForMissingSizes(MjEntity entity) {
		for (MjProperty property : entity.properties) {
			if (property.type.type == MjEntityType.String) {
				if (property.size == null) {
					if (property.name.equals("uuid")) {
						System.out.println("entity " + entity.name);
						property.size = 36;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		ClassGenerator generator = new ClassGenerator("./src/main/generated");
		for (XsdModel model : xsdModels.values()) {
			model.getEntities().forEach(EchSchemas::updateType);
			model.getEntities().forEach(EchSchemas::checkForMissingSizes);
		}
		for (XsdModel model : xsdModels.values()) {
			Collection<MjEntity> entities = model.getEntities();
			entities = entities.stream().filter(EchSchemas::filter).collect(Collectors.toList());
			generator.generate(entities);
		}
	}
	
}
