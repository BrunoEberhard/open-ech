package ch.openech.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.metamodel.model.MjEntity.MjEntityType;
import org.minimalj.metamodel.model.MjModel;
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

import ch.ech.ech0008.Country;
import ch.ech.ech0010.AddressInformation;
import ch.ech.ech0010.MailAddress;
import ch.ech.ech0011.Destination;
import ch.ech.ech0044.PersonIdentification;
import ch.openech.model.DatePartiallyKnown;
import ch.openech.model.NamedId;
import ch.openech.model.UidStructure;
import ch.openech.model.YesNo;
import ch.openech.xml.write.EchNamespaceUtil;

public class XsdModel {
	public static final XsdModel $ = Keys.of(XsdModel.class);
	
	public static final String XMLSchemaInstance_URI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XS = "http://www.w3.org/2001/XMLSchema";
	public static final String XML_NS = "http://www.w3.org/2000/xmlns/";

	private static Logger log = Logger.getLogger(XsdModel.class.getName());

	private Document document;
	
	private Map<String, XsdModel> models;
	
	private LinkedHashMap<String, Element> rootElements = new LinkedHashMap<>();
	private LinkedHashMap<String, XsdMjEntity> rootEntities = new LinkedHashMap<>();
	private LinkedHashMap<String, XsdMjEntity> entities = new LinkedHashMap<>();
	private String namespace;
	private boolean qualifiedElements = true;

	private final Map<String, String> namespaceByPrefix = new HashMap<>();
	private final Map<String, String> prefixByNamespace = new HashMap<>();
	
	private static Map<String, XsdMjEntity> XML_TYPES = new HashMap<>();
	private static Map<String, MjEntity> PREDEFINED_TYPES = new HashMap<>();
	
	public Collection<XsdMjEntity> getEntities() {
		return entities.values();
	}
	
	public Collection<XsdMjEntity> getRootEntities() {
		return rootEntities.values();
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
		XsdMjEntity INT = new XsdMjEntity(MjEntityType.Integer);
		XML_TYPES.put("unsignedShort", INT);
		XML_TYPES.put("unsignedInt", INT);
		XML_TYPES.put("int", INT);
		XML_TYPES.put("integer", INT);
		XML_TYPES.put("nonNegativeInteger", INT);
		XML_TYPES.put("positiveInteger", INT);
		
		XsdMjEntity YEAR = new XsdMjEntity(MjEntityType.Integer);
		YEAR.maxLength = 4;
		XML_TYPES.put("gYear", YEAR);

		XsdMjEntity DURATION = new XsdMjEntity(MjEntityType.Integer);
		DURATION.maxLength = 6;
		XML_TYPES.put("duration", DURATION);

		XsdMjEntity LONG = new XsdMjEntity(MjEntityType.Long);
		XML_TYPES.put("unsignedLong", LONG);
		XML_TYPES.put("long", LONG);
		
		XsdMjEntity STRING = new XsdMjEntity(MjEntityType.String);
		XML_TYPES.put("string", STRING);
		XML_TYPES.put("normalizedString", STRING);
		XML_TYPES.put("token", STRING);

		XsdMjEntity YEAR_MONTH = new XsdMjEntity(MjEntityType.String);
		YEAR_MONTH.maxLength = 7;
		XML_TYPES.put("gYearMonth", YEAR_MONTH);

		XsdMjEntity URI = new XsdMjEntity(MjEntityType.String);
		URI.maxLength = 2047; // https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
		XML_TYPES.put("anyURI", URI);
		
		XML_TYPES.put("boolean", new XsdMjEntity(MjEntityType.Boolean));
		XML_TYPES.put("decimal", new XsdMjEntity(MjEntityType.BigDecimal));
		XML_TYPES.put("date", new XsdMjEntity(MjEntityType.LocalDate));
		XML_TYPES.put("dateTime", new XsdMjEntity(MjEntityType.LocalDateTime));
		XML_TYPES.put("time", new XsdMjEntity(MjEntityType.LocalTime));

		XML_TYPES.put("anyType", new XsdMjEntity(MjEntityType.ByteArray));
	}
	
	static {
		MjModel defaultModel = new MjModel();
		
		MjEntity datePartiallyKnown = new MjEntity(defaultModel, DatePartiallyKnown.class);
		datePartiallyKnown.type = MjEntityType.DEPENDING_ENTITY;
		PREDEFINED_TYPES.put("datePartiallyKnownType", datePartiallyKnown);

		MjEntity yesNo = new MjEntity(defaultModel, YesNo.class);
		yesNo.type = MjEntityType.Enum;
		yesNo.values = Arrays.asList("0", "1");
		PREDEFINED_TYPES.put("yesNoType", yesNo);
		PREDEFINED_TYPES.put("paperLockType", yesNo);
		
		MjEntity namedId = new MjEntity(defaultModel, NamedId.class);
		namedId.type = MjEntityType.DEPENDING_ENTITY;
		PREDEFINED_TYPES.put("namedPersonIdType", namedId);
		PREDEFINED_TYPES.put("namedOrganisationIdType", namedId);
		PREDEFINED_TYPES.put("namedIdType", namedId);
		
		MjEntity uidStructure = new MjEntity(defaultModel, UidStructure.class);
		uidStructure.type = MjEntityType.DEPENDING_ENTITY;
		PREDEFINED_TYPES.put("uidStructureType", uidStructure);

		MjEntity personIdentification = new MjEntity(defaultModel, PersonIdentification.class);
		personIdentification.type = MjEntityType.ENTITY;
		PREDEFINED_TYPES.put("personIdentificationLightType", personIdentification);

		MjEntity destination = new MjEntity(defaultModel, Destination.class);
		destination.type = MjEntityType.DEPENDING_ENTITY;
		PREDEFINED_TYPES.put("destinationType", destination);

		MjEntity country = new MjEntity(defaultModel, Country.class);
		country.type = MjEntityType.ENTITY;
		PREDEFINED_TYPES.put("countryType", country);

		MjEntity mailAddress = new MjEntity(defaultModel, MailAddress.class);
		mailAddress.type = MjEntityType.DEPENDING_ENTITY;
		PREDEFINED_TYPES.put("personMailAddressType", mailAddress);
		PREDEFINED_TYPES.put("organisationMailAddressType", mailAddress);

		MjEntity addressInformation = new MjEntity(defaultModel, AddressInformation.class);
		addressInformation.type = MjEntityType.DEPENDING_ENTITY;
		PREDEFINED_TYPES.put("addressInformationType", addressInformation);
		PREDEFINED_TYPES.put("swissAddressInformationType", addressInformation);

		MjEntity originIdentification = new MjEntity(MjEntityType.String);
		// originIdentification.values = // da könnte man noch Kantone + FL + XX
		// einfügen (für ech 229)
		PREDEFINED_TYPES.put("originIdentificationType", originIdentification);
		PREDEFINED_TYPES.put("cantonFlAbbreviationType", originIdentification); // ok, hier wäre ohne Ausland
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
				} else if (attribute.getName().equals("elementFormDefault")) {
					qualifiedElements = !"unqualified".equals(attribute.getValue());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	void read(Map<String, XsdModel> models) {
		Objects.requireNonNull(models);
		boolean readBefore = this.models != null;
		if (readBefore) return;
		this.models = models;
		
		// dependencies must be read first to have the base types and its values
		Set<String> namespaces = new HashSet<String>(namespaceByPrefix.values());
		for (String dependency : namespaces) {
			if (models.containsKey(dependency)) {
				models.get(dependency).read(models);
			} else {
				int schemaNumber = EchNamespaceUtil.extractSchemaNumber(dependency);
				if (schemaNumber > 0) {
					for (Entry<String, XsdModel> m : models.entrySet()) {
						if (EchNamespaceUtil.extractSchemaNumber(m.getKey()) == schemaNumber) {
							m.getValue().read(models);
						}
					}
				}
			}
			XsdModel depModel = models.get(dependency);
			if (depModel != null) {
				for (Map.Entry<String, String> e : depModel.namespaceByPrefix.entrySet()) {
					if (!namespaceByPrefix.containsKey(e.getKey())) {
						namespaceByPrefix.put(e.getKey(), e.getValue());
						prefixByNamespace.put(e.getValue(), e.getKey());
					}
				}
			}
		}
		
		Element documentElement = document.getDocumentElement();
		
		// allocate all types to have them ready for references
		forEachChild(documentElement, element -> {
			if (StringUtils.equals(element.getLocalName(), "simpleType", "complexType")) {
				String name = element.getAttribute("name");
				XsdMjEntity entity = new XsdMjEntity(name);
				entity.type = MjEntityType.DEPENDING_ENTITY;
				entity.setElement(element);
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
				// PREDEFINED_TYPES sind nur MjEntities
				if (property.type instanceof XsdMjEntity) {
					XsdMjEntity entity = (XsdMjEntity) property.type;
					// Root Element können mit einem type - Attribute auf eine Type - Definition
					// verweisen oder sie können eine eigene Definition mitbringen. Diese eigenen
					// Definitionen müssen dann als neue Entität mit dem Namen des Root Elements
					// erfasst werden.
					if (StringUtils.isEmpty(entity.name)) {
						entity.name = property.name;
						entities.put(property.name, entity);
					}
					rootEntities.put(property.name, entity);
					rootElements.put(property.name, element);
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

	public List<Element> getAttributes(Element node) {
		List<Element> elements = new ArrayList<>();
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node c = childNodes.item(i);
			if (XS.equals(c.getNamespaceURI()) && StringUtils.equals("attribute", c.getLocalName())) {
				elements.add((Element) c);
			} else if (c instanceof Element) {
				Element child = (Element) c;
				List<Element> childElements = getAttributes(child);
				elements.addAll(childElements);
				
				Element extension = get(child, "extension");
				if (extension != null) {
					String base = extension.getAttribute("base");
					Element baseElement = findElement(base);
					if (baseElement != null) {
						List<Element> baseAttribures = getAttributes(baseElement);
						elements.addAll(baseAttribures);
					}
				}
			}
		}
		return elements;
	}

	private MjEntity simpleType(Element node) {
		String name = node.getAttribute("name");
		boolean anonymous = StringUtils.isEmpty(name);
		XsdMjEntity entity = anonymous ? new XsdMjEntity(MjEntityType.DEPENDING_ENTITY) : entities.get(name);
		Element restriction = get(node, "restriction");
		if (restriction != null) {
			String base = restriction.getAttribute("base");
			MjEntity baseEntity = findEntity(base);
			if (baseEntity == null) {
				throw new IllegalStateException("Base Entity not found: " + base + " for " + name);
			}

			entity.type = baseEntity.type;

			// restriction auf enumeration werden ignoriert,
			// z.B. bei eCH-0021:typeOfRelationshipType
			if (baseEntity.isEnumeration()) {
				entity.values = baseEntity.values;
				return baseEntity;
			}
			
			Node minInclusive = get(restriction, "minInclusive");
			entity.minInclusive = minInclusive != null ? ((Element) minInclusive).getAttribute("value") : null;
			Node maxInclusive = get(restriction, "maxInclusive");
			entity.maxInclusive = maxInclusive != null ? ((Element) maxInclusive).getAttribute("value") : null;
			Node minLength = get(restriction, "minLength");
			entity.minLength = minLength != null ? Integer.parseInt(((Element) minLength).getAttribute("value")) : null;
			Node maxLength = get(restriction, "maxLength");
			entity.maxLength = maxLength != null ? Integer.parseInt(((Element) maxLength).getAttribute("value")) : null;

			if (entity.maxLength == null) {
				int m = 0;
				if (entity.maxInclusive != null) m = entity.maxInclusive.length();
				if (entity.minInclusive != null) m = Math.max(m, entity.minInclusive.length());
				if (m > 0) entity.maxLength = m;
			}
			
			Node patternNode = get(restriction, "pattern");
			if (patternNode instanceof Element) {
				String pattern = ((Element) patternNode).getAttribute("value");
				// das deckt es nicht 100% ab, aber meist haben die nutzbaren reges die Form
				// "\d{14}"
				int index = pattern.lastIndexOf('{');
				if (pattern.startsWith("\\d{") && index == 2 && pattern.endsWith("}")) {
					String[] patternValues = pattern.substring(3, pattern.length() - 1).split(",");
					if (patternValues.length > 1) {
						entity.minLength = Integer.parseInt(patternValues[0]);
					}
					entity.maxLength = Integer.parseInt(patternValues[patternValues.length - 1]);
				}
			}
			
			if (get(restriction, "enumeration") != null) {
				entity.values = baseEntity.values != null ? new ArrayList<>(baseEntity.values) : new ArrayList<>();
				AtomicInteger maxValueLength = new AtomicInteger(0);
				forEachChild(restriction, element -> {
					if ("enumeration".equals(element.getLocalName())) {
						entity.type = MjEntityType.Enum;
						String value = element.getAttribute("value");
						maxValueLength.set(Math.max(maxValueLength.get(), value.length()));
						if (!entity.values.contains(value)) {
							entity.values.add(value);
						}
					}
				});
				
				if (baseEntity.type == MjEntityType.String || baseEntity.type == MjEntityType.Integer || baseEntity.type == MjEntityType.Long) {
					if (maxValueLength.get() > 0 && entity.maxLength == null) {
						entity.maxLength = maxValueLength.get();
					}
				}
			} else if (baseEntity.isEnumeration()) {
				// wenn von einer enum abgeleitet wird und keine Werte angegeben werden sollen
				// die Werte der "super - enum" verwendet werden
				entity.values = baseEntity.values;
			}
		}
		
		entity.setElement(node);
		return entity;
	}

	private MjEntity complexType(Element node) {
		String name = node.getAttribute("name");
		XsdMjEntity entity = StringUtils.isEmpty(name) ? new XsdMjEntity(MjEntityType.DEPENDING_ENTITY) : entities.get(name);
		if (!entity.properties.isEmpty()) {
			return entity;
		}
		
		Element sequence = get(node, "sequence");
		if (sequence != null) {
			entity.properties.addAll(sequence(sequence, mayBeLeftOut(sequence)));
		}
		Element all = get(node, "all");
		if (all != null) {
			entity.properties.addAll(sequence(all, mayBeLeftOut(all)));
		}
		Element choice = get(node, "choice");
		if (choice != null) {
			entity.properties.addAll(sequence(choice, mayBeLeftOut(choice) || hasMoreThanOneOption(choice)));
		}
		Element complexContent = get(node, "complexContent");
		if (complexContent != null) {
			Element restriction = get(complexContent, "restriction");
			if (restriction != null) {
				// Im xsd werden hier aus dem Basis Typ einzelne Properties
				// herausgepickt. Das wird ignoriert und gleich das Basis entity verwendet
				String base = restriction.getAttribute("base");
				MjEntity baseEntity = findEntity(base);
				if (baseEntity == null) {
					throw new IllegalStateException("Base Entity not found: " + base);
				}
				// TODO müssten hier Entities kopiert werden damits passt?
				if (baseEntity instanceof XsdMjEntity) {
					entities.put(name, (XsdMjEntity) baseEntity);
				}
				return baseEntity;
			} else {
				entity.properties.addAll(complexContent(complexContent));
			}
		}
		List<Element> attributes = getAttributes(node);
		if (attributes != null) {
			for (Element attribute : attributes) {
				MjProperty property = element(attribute);
				if (!StringUtils.isEmpty(property.name) && property.type != null) {
//					if (property.name.equals("version")) property.name = "version_";
					entity.properties.add(property);
				}
			}
		}

		entity.setElement(node);
		return entity;
	}

	// pathologischer Fall: Ein Choice Element hat nur ein echtes Child. Dh man muss
	// genau diese Option anwählen. Damit bleiben die dort vorhandenen NotEmpty -
	// Felder auch zwingend, da man nie auf eine andere Option ausweichen kann. Gibt
	// es nur bei ech 0011 version 3 (destinationType)
	private boolean hasMoreThanOneOption(Node node) {
		boolean foundOne = false;
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i).getNodeType() == Node.TEXT_NODE)
				continue;
			if (foundOne)
				return true;
			foundOne = true;
		}
		return false;
	}

	// wenn auf einer sequence oder choice minOccurs="0" als attribute angegeben
	// ist können die enthaltenen Element nicht "notEmpty" sein.
	private boolean mayBeLeftOut(Element element) {
		String minOccurs = element.getAttribute("minOccurs");
		return "0".equals(minOccurs);
	}

	private List<MjProperty> sequence(Node node, boolean overrideNotEmpty) {
		List<MjProperty> properties = new ArrayList<>();
		forEachChild(node, new SequenceVisitor(properties, overrideNotEmpty));
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
			} else if (StringUtils.equals(element.getLocalName(), "sequence", "all")) {
				forEachChild(element, new SequenceVisitor(properties, overrideNotEmpty || mayBeLeftOut(element)));
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
			if (baseEntity.properties.isEmpty() && (baseEntity instanceof XsdMjEntity) && ((XsdMjEntity) baseEntity).getElement() != null) {
				complexType(((XsdMjEntity) baseEntity).getElement());
			}
			properties.addAll(baseEntity.properties);
			MjEntity extensionContent = complexType(extension);
			properties.addAll(extensionContent.properties);
		}
		Element restriction = get(node, "restriction");
		if (restriction != null) {
			MjEntity restrictionContent = complexType(restriction);
			properties.addAll(restrictionContent.properties);
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
		property.notEmpty = minOccurs == null || !minOccurs.equals("0");

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
			if (property.propertyType == MjPropertyType.LIST && property.type.isEnumeration()) {
				property.propertyType = MjPropertyType.ENUM_SET;
			}
		}
		
		return property;
	}
	
	public MjEntity findEntity(String type) {
		if (type.equals("string40Type")) {
			type = "string60Type";
		}
		if (!StringUtils.isEmpty(type)) {
			if (type.contains(":")) {
				String[] parts = type.split(":");
				String namespace = namespaceByPrefix.get(parts[0]);
				if ("xs".equals(parts[0])) {
					return XML_TYPES.get(parts[1]);
				} else if (!StringUtils.equals(namespace, this.namespace)) {
					XsdModel xsdModel = findModel(namespace);
					if (xsdModel != null) {
						return xsdModel.findEntity(parts[1]);
					} else {
						log.warning("Cannot resolve: " + namespace + ":" + parts[1]);
						return new XsdMjEntity(parts[1]);
					}
				} else {
					type = parts[1];
				}
			}
			if (PREDEFINED_TYPES.containsKey(type)) {
				return PREDEFINED_TYPES.get(type);
			} else if (entities.containsKey(type)) {
				return entities.get(type);
			} else {
				log.warning("Cannot resolve: " + type);
				return new XsdMjEntity(type);
			}
		} else {
			return null;
		}
	}
	
	public Element findElement(String type) {
		if (type.contains(":")) {
			String[] parts = type.split(":");
			String namespace = namespaceByPrefix.get(parts[0]);
			if (!StringUtils.equals(namespace, this.namespace)) {
				XsdModel xsdModel = findModel(namespace);
				return xsdModel != null ? xsdModel.findElement(parts[1]) : null;
			} else {
				type = parts[1];
			}
		}
		return entities.get(type).getElement();
	}

	public XsdModel findModel(String namespace) {
		// ech 173 verwendet die forgiving schemas. Die werden zur Zeit nicht
		// explizit erzeugt. Daher werden die namespaces konviertiert von
		// http://www.ech.ch/xmlns/eCH-0021-f/7 zu
		// http://www.ech.ch/xmlns/eCH-0021/7
		String namespaceWithoutForgiving = namespace.replaceAll("-f/", "/");

		XsdModel xsdModel = models.get(namespaceWithoutForgiving);
		return xsdModel;
	}

	public Element getRootElement(String rootElementName) {
		return rootElements.get(rootElementName);
	}

	public boolean isQualifiedElements() {
		return qualifiedElements;
	}

}
