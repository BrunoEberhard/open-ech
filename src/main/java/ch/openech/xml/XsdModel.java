package ch.openech.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.metamodel.model.MjEntity.MjEntityType;
import org.minimalj.metamodel.model.MjProperty;
import org.minimalj.metamodel.model.MjProperty.MjPropertyType;
import org.minimalj.model.Keys;
import org.minimalj.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XsdModel {
	public static final XsdModel $ = Keys.of(XsdModel.class);
	
	public static final String XMLSchemaInstance_URI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XS = "http://www.w3.org/2001/XMLSchema";
	public static final String XML_NS = "http://www.w3.org/2000/xmlns/";

	private static Logger log = Logger.getLogger(XsdModel.class.getName());

	private Document document;
	
	private Map<String, XsdModel> models;
	
	private LinkedHashMap<String, MjEntity> entities = new LinkedHashMap<>();
	private String namespace;
	
	private final Map<String, String> namespaceByPrefix = new HashMap<>();
	private final Map<String, String> prefixByNamespace = new HashMap<>();
	
	private static Map<String, MjEntity> XML_TYPES = new HashMap<>();
	
	public Collection<MjEntity> getEntities() {
		return entities.values();
	}
	
	public String getNamespace() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "namespace");
		
		return namespace;
	}
	
	public Map<String, String> getNamespaceByPrefix() {
		return namespaceByPrefix;
	}
	
	public String getPrefix() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "prefix");
		
		return prefixByNamespace.get(namespace);
	}
	
	static {
		MjEntity INT = new MjEntity(MjEntityType.Integer);
		XML_TYPES.put("gYear", INT);
		XML_TYPES.put("unsignedInt", INT);
		XML_TYPES.put("int", INT);
		XML_TYPES.put("integer", INT);
	
		MjEntity LONG = new MjEntity(MjEntityType.Long);
		XML_TYPES.put("unsignedLong", LONG);
		XML_TYPES.put("nonNegativeInteger", LONG);
		
		MjEntity STRING = new MjEntity(MjEntityType.String);
		XML_TYPES.put("string", STRING);
		XML_TYPES.put("gYearMonth", STRING);
		XML_TYPES.put("normalizedString", STRING);
		XML_TYPES.put("token", STRING);
		XML_TYPES.put("anyURI", STRING);
		
		XML_TYPES.put("boolean", new MjEntity(MjEntityType.Boolean));
		XML_TYPES.put("decimal", new MjEntity(MjEntityType.BigDecimal));
		XML_TYPES.put("date", new MjEntity(MjEntityType.LocalDate));
		XML_TYPES.put("dateTime", new MjEntity(MjEntityType.LocalDateTime));
		XML_TYPES.put("time", new MjEntity(MjEntityType.LocalTime));

		XML_TYPES.put("anyType", new MjEntity(MjEntityType.ByteArray));
	}

	public XsdModel() {
		
	}
	
	XsdModel(InputStream inputStream) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(inputStream);

			Element documentElement = document.getDocumentElement();
			this.namespace = documentElement.getAttribute("targetNamespace");
			
			NamedNodeMap attributes = documentElement.getAttributes();
			for (int i = 0; i<attributes.getLength(); i++) {
				Attr attribute = (Attr) attributes.item(i);
				if (XML_NS.equals(attribute.getNamespaceURI())) {
					String prefix = attribute.getLocalName();
					String namespaceURI = attribute.getValue();
					namespaceByPrefix.put(prefix, namespaceURI);
					prefixByNamespace.put(namespaceURI, prefix);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	void read(Map<String, XsdModel> models) {
		Objects.nonNull(models);
		boolean readBefore = this.models != null;
		if (readBefore) return;
		this.models = models;
		
		// dependencies must be read first to have the base types and its values
		for (String dependency : namespaceByPrefix.values()) {
			if (models.containsKey(dependency)) {
				models.get(dependency).read(models);
			}
		}
		
		Element documentElement = document.getDocumentElement();
		
		// allocate all types to have them ready for references
		forEachChild(documentElement, element -> {
			if (StringUtils.equals(element.getLocalName(), "simpleType", "complexType")) {
				String name = element.getAttribute("name");
				MjEntity entity = new MjEntity(name);
				entities.put(name, entity);
			}
		});
		
		forEachChild(documentElement, element -> {
			if ("simpleType".equals(element.getLocalName())) {
				// maybe only complex types are added to the model
				simpleType(element);
			} 
		});

		forEachChild(documentElement, element -> {
			if ("complexType".equals(element.getLocalName())) {
				complexType(element);
			} 
		});
		
		forEachChild(documentElement, element -> {
			if ("element".equals(element.getLocalName())) {
				MjProperty property = element(element);
				MjEntity entity = property.type;
				if (StringUtils.isEmpty(entity.name)) {
					entity.name = property.name;
					entities.put(entity.name, entity);
				}
			} 
		});		
	}
	
	public static void forEachChild(Node parent, Consumer<Element> function) {
		NodeList nodeList = parent.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				function.accept(element);
			}
		}
	}

	public static Element get(Element node, String localName) {
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i<childNodes.getLength(); i++) {
			Node c = childNodes.item(i);
			if (XS.equals(c.getNamespaceURI()) && StringUtils.equals(localName, c.getLocalName())) {
				return (Element) c;
			}
		}
		return null;
	}

	private MjEntity simpleType(Element node) {
		String name = node.getAttribute("name");
		boolean anonymous = StringUtils.isEmpty(name);
		MjEntity entity = anonymous ? new MjEntity(MjEntityType.ENTITY) : entities.get(name);
		Element restriction = get(node, "restriction");
		if (restriction != null) {
			String base = restriction.getAttribute("base");
			MjEntity baseEntity = findEntity(base);
			if (baseEntity == null) {
				throw new IllegalStateException("Base Entity not found: " + base + " for " + name);
			}
			if (anonymous) {
				return baseEntity;
			}
			
			entity.type = baseEntity.type;
			
			Node minInclusive = get(restriction, "minInclusive");
			entity.minInclusive = minInclusive != null ? ((Element) minInclusive).getAttribute("value") : null;
			Node maxInclusive = get(restriction, "maxInclusive");
			entity.maxInclusive = maxInclusive != null ? ((Element) maxInclusive).getAttribute("value") : null;
			Node minLength = get(restriction, "minLength");
			entity.minLength = minLength != null ? Integer.parseInt(((Element) minLength).getAttribute("value")) : 0;
			Node maxLength = get(restriction, "maxLength");
			entity.maxLength = maxLength != null ? Integer.parseInt(((Element) maxLength).getAttribute("value")) : null;
	
			if (baseEntity.isEnumeration() || get(restriction, "enumeration") != null) {
				entity.values = baseEntity.values != null ? new ArrayList<>(baseEntity.values) : new ArrayList<>();
				forEachChild(restriction, element -> {
					if ("enumeration".equals(element.getLocalName())) {
						entity.type = MjEntityType.String;
						String value = element.getAttribute("value");
						if (!entity.values.contains(value)) {
							entity.values.add(value);
						}
					}
				});
			}
		}
		return entity;
	}

	private MjEntity complexType(Element node) {
		String name = node.getAttribute("name");
		MjEntity entity = StringUtils.isEmpty(name) ? new MjEntity(MjEntityType.ENTITY) : entities.get(name);
		Element sequence = get(node, "sequence");
		if (sequence != null) {
			entity.properties.addAll(sequence(sequence));
		}
		Element choice = get(node, "choice");
		if (choice != null) {
			entity.properties.addAll(sequence(choice));
		}
		Element complexContent = get(node, "complexContent");
		if (complexContent != null) {
			entity.properties.addAll(complexContent(complexContent));
		}
		
		entity.setElement(node);
		return entity;
	}

	private List<MjProperty> sequence(Node node) {
		List<MjProperty> properties = new ArrayList<>();
		forEachChild(node, new SequenceVisitor(properties, false));
		return properties;
	}

	// sobald man sich in einer Choice befindet kann ein Property nicht
	// mehr mandatory (NotEmpty) sein. Daher muss hier rekursiv zusammengebaut
	// werden. Wichtig ist das letzte 'true' im 'choice' if  - branch
	private class SequenceVisitor implements Consumer<Element> {
		private final List<MjProperty> properties;
		private final boolean overrideNotEmpty;
		
		public SequenceVisitor(List<MjProperty> properties, boolean overrideNotEmpty) {
			this.properties = properties;
			this.overrideNotEmpty = overrideNotEmpty;
		}
		
		@Override
		public void accept(Element element) {
			if ("element".equals(element.getLocalName())) {
				MjProperty property = element(element);
				if (overrideNotEmpty) {
					property.notEmpty = null;
				}
				// Omit '<xs:element ref="eCH-0020:extension" minOccurs="0"/>'
				if (property.type != null) {
					properties.add(property);
				}
			} else if ("sequence".equals(element.getLocalName())) {
				forEachChild(element, new SequenceVisitor(properties, overrideNotEmpty));
			} else if ("choice".equals(element.getLocalName())) {
				// bei choice das "NotEmpty" rausschmeissen
				forEachChild(element, new SequenceVisitor(properties, true));
			} else {
				// what to do with xs:any ?
			}
		}
	}
	
	private List<MjProperty> complexContent(Element node) {
		List<MjProperty> properties = new ArrayList<>();
		Element extension = get(node, "extension");
		if (extension != null) {
			String base = extension.getAttribute("base");
			MjEntity baseEntity = findEntity(base);
			if (baseEntity == null) {
				throw new IllegalStateException("Base Entity not found: " + base);
			}
			properties.addAll(baseEntity.properties);
			MjEntity extensionContent = complexType(extension);
			properties.addAll(extensionContent.properties);
		}
		return properties;
	}

	
	private MjProperty element(Element element) {
		MjProperty property = new MjProperty();
		property.name = element.getAttribute("name");
		
		String type = element.getAttribute("type");
		property.type = findEntity(type);

		Element simpleType = get(element, "simpleType");
		if (simpleType != null) {
			property.type = simpleType(simpleType);
		}
		
		Element complexType = get(element, "complexType");
		if (complexType != null) {
			property.type = complexType(complexType);
		}
		
		if (property.type != null) {
			property.size = property.type.maxLength;
		}

		String minOccurs = element.getAttribute("minOccurs");
		property.notEmpty = !"0".equals(minOccurs);

		String maxOccurs = element.getAttribute("maxOccurs");
		if (!StringUtils.isEmpty(maxOccurs)) {
			if ("unbounded".equals(maxOccurs)) {
				property.propertyType = MjPropertyType.LIST;
			} else {
				int size = Integer.parseInt(maxOccurs);
				if (size > 1) {
					property.propertyType = MjPropertyType.LIST;
					property.size = size;
				}
			}
		}
		
		return property;
	}
	
	private MjEntity findEntity(String type) {
		if (!StringUtils.isEmpty(type)) {
			if (type.contains(":")) {
				String[] parts = type.split(":");
				String namespace = namespaceByPrefix.get(parts[0]);
				if ("xs".equals(parts[0])) {
					return XML_TYPES.get(parts[1]);
				} else if (!StringUtils.equals(namespace, this.namespace)) {
					XsdModel xsdModel = models.get(namespace);
					if (xsdModel != null) {
						return xsdModel.findEntity(parts[1]);
					} else {
						log.warning("Cannot resolve: " + namespace + ":" + parts[1]);
						MjEntity entity = new MjEntity(parts[1]);
						return entity;
					}
				} else {
					return entities.get(parts[1]);
				}
			} else {
				return entities.get(type);
			}
		} else {
			return null;
		}
	}

}
