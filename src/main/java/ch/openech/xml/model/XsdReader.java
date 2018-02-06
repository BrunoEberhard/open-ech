package ch.openech.xml.model;

import static ch.openech.xml.read.StaxEch.skip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.model.properties.FlatProperties;
import org.minimalj.util.StringUtils;

import ch.openech.xml.model.XsdNode.XsdChoice;
import ch.openech.xml.model.XsdNode.XsdSequence;
import ch.openech.xml.model.XsdType.XsdTypeComplex;
import ch.openech.xml.model.XsdType.XsdTypeSimple;
import ch.openech.xml.write.EchNamespaceUtil;

public class XsdReader {
	private static Logger log = Logger.getLogger(XsdReader.class.getName());

	private final Map<String, XsdSchema> schemaLocations = new HashMap<>();
	private final Map<String, XsdSchema> schemaNamespaces = new HashMap<>();
	
	public XsdSchema read(String schemaLocation) throws Exception {
		if (schemaLocations.containsKey(schemaLocation)) {
			return schemaLocations.get(schemaLocation);
		}
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		try (InputStream inputStream = getInputStream(schemaLocation)) {
			XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
			XsdSchema result = read(xml);
			result.schemaLocation = schemaLocation;
			schemaLocations.put(schemaLocation, result);
			schemaNamespaces.put(result.namespace, result);
			xml.close();
			return result;
		}
	}

	private InputStream getInputStream(String namespaceLocation) throws XMLStreamException, IOException {
		try {
			return new URL(namespaceLocation).openStream();
		} catch (Exception e) {
			return EchNamespaceUtil.getLocalCopyOfSchema(namespaceLocation);
		}
	}

	public XsdSchema read(XMLEventReader xml) throws Exception {
		XsdSchema result = new XsdSchema();
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				if ("schema".equals(elementName)) {
					result.namespace = element.getAttributeByName(new QName("targetNamespace")).getValue();
					Iterator<?> attributesIterator = element.getNamespaces();
					while (attributesIterator.hasNext()) {
						Object o = attributesIterator.next();
						if (o instanceof Attribute) {
							Attribute attribute = (Attribute) o;
							String prefix = attribute.getName().getLocalPart();
							String namespace = attribute.getValue();
							result.prefixes.put(prefix, namespace);
						}
					}
					result = schema(result, xml);
				} else {
					skip(xml);
				}
			} else if (event.isEndElement()) {
				return result;
			}
		}
		return result;
	}

	public XsdSchema schema(XsdSchema result, XMLEventReader xml) throws Exception {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				if ("import".equals(elementName)) {
					String schemaLocation = element.getAttributeByName(new QName("schemaLocation")).getValue();
					String namespace = element.getAttributeByName(new QName("namespace")).getValue();
					XsdSchema schema = read(schemaLocation);
					String prefix = element.getNamespaceContext().getPrefix(namespace);
					result.imports.put(namespace, schema);
					result.prefixes.put(prefix, namespace);
					skip(xml);
				} else if ("complexType".equals(elementName)) {
					XsdType type = complexType(result, xml);
					Attribute attributeName = element.getAttributeByName(new QName("name"));
					type.name = attributeName.getValue();
					result.types.add(type);
				} else if ("simpleType".equals(elementName)) {
					XsdType type = simpleType(result, xml);
					Attribute attributeName = element.getAttributeByName(new QName("name"));
					type.name = attributeName.getValue();
					System.out.println(" for " + type.name);
					result.types.add(type);
				} else if ("element".equals(elementName)) {
					XsdElement rootElement = element(result, element, xml);
					result.elements.add(rootElement);
				} else 
				skip(xml);
			} else if (event.isEndElement()) {
				assertEnd(event, "schema");
				return result;
			}
		}
		return result;
	}

	private void assertEnd(XMLEvent event, String... ends) {
		String elementName = event.asEndElement().getName().getLocalPart();
		if (!StringUtils.equals(elementName, ends)) {
			throw new IllegalStateException(elementName);
		}
	}

	public XsdTypeSimple simpleType(XsdSchema schema, XMLEventReader xml) throws Exception {
		XsdTypeSimple result = new XsdTypeSimple(schema);
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				if ("restriction".equals(elementName)) {
					Attribute attributeBase = element.getAttributeByName(new QName("base"));
					if (attributeBase != null) {
						String qualifiedName = attributeBase.getValue();
						result.base = schema.getType(qualifiedName);
						simpleTypeRestriction(schema, xml, result);
					} else {
						skip(xml);
					}
				} else 
				skip(xml);
			} else if (event.isEndElement()) {
				assertEnd(event, "simpleType");
				return result;
			}
		}
		return result;
	}
	
	public void simpleTypeRestriction(XsdSchema schema, XMLEventReader xml, XsdTypeSimple typeSimple) throws Exception {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				if (StringUtils.equals(elementName, "minLength", "maxLength")) {
					Attribute attribute = element.getAttributeByName(new QName("value"));
					if (attribute != null) {
						Integer value = Integer.parseInt(attribute.getValue());
						FlatProperties.getProperty(typeSimple.getClass(), elementName).setValue(typeSimple, value);
					}
				} else if (StringUtils.equals(elementName, "minInclusive")) {
					Attribute attribute = element.getAttributeByName(new QName("value"));
					if (attribute != null) {
						typeSimple.setMinInclusive(attribute.getValue());
					}
				} else if (StringUtils.equals(elementName, "maxInclusive")) {
					Attribute attribute = element.getAttributeByName(new QName("value"));
					if (attribute != null) {
						typeSimple.setMaxInclusive(attribute.getValue());
					}
				} else if (StringUtils.equals(elementName, "enumeration")) {
					Attribute attribute = element.getAttributeByName(new QName("value"));
					if (attribute != null) {
						typeSimple.enumeration.add(attribute.getValue());
					}
				}
				skip(xml);
			} else if (event.isEndElement()) {
				assertEnd(event, "restriction");
				return;
			}
		}
	}
	
	public XsdTypeComplex complexType(XsdSchema schema, XMLEventReader xml) throws Exception {
		XsdTypeComplex result = new XsdTypeComplex(schema);
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				XsdNode node = node(schema, event.asStartElement(), xml);
				if (node != null) {
					result.node = node;
				}
			} else if (event.isEndElement()) {
				assertEnd(event, "complexType");
				return result;
			}
		}
		return result;
	}

	public XsdNode content(XsdSchema schema, XMLEventReader xml) throws Exception {
		XsdNode result = null;
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				if ("restriction".equals(elementName)) {
					result = new XsdNode.XsdRestriction();
					Attribute attributeType = element.getAttributeByName(new QName("base"));
					((XsdNode.XsdRestriction) result).base = schema.getType(attributeType.getValue());
					((XsdNode.XsdRestriction) result).node = restrictionOrExtension(schema, xml);
					if (((XsdNode.XsdRestriction) result).node == null) {
						System.out.println("No node");
					}
				} else if ("extension".equals(elementName)) {
					result = new XsdNode.XsdExtension();
					Attribute attributeType = element.getAttributeByName(new QName("base"));
					((XsdNode.XsdExtension) result).base = schema.getType(attributeType.getValue());
					((XsdNode.XsdExtension) result).node = restrictionOrExtension(schema, xml);
					if (((XsdNode.XsdExtension) result).node == null) {
						System.out.println("No node");
					}
				} else
				skip(xml);
			} else if (event.isEndElement()) {
				return result;
			}
		}
		return result;
	}
	
	public XsdNode restrictionOrExtension(XsdSchema schema, XMLEventReader xml) throws Exception {
		XsdNode result = null;
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				XsdNode node = node(schema, event.asStartElement(), xml);
				if (node != null) {
					result = node;
				}
			} else if (event.isEndElement()) {
				return result;
			}
		}
		return result;
	}

	public XsdNode node(XsdSchema schema, StartElement element, XMLEventReader xml) throws Exception {
		XsdNode result = null;
		String elementName = element.getName().getLocalPart();
		if ("sequence".equals(elementName)) {
			result = sequence(schema, xml);
		} else if ("choice".equals(elementName)) {
			result = choice(schema, xml);
		} else if ("simpleContent".equals(elementName) || "complexContent".equals(elementName)) {
			result = content(schema, xml);					
		} else if ("element".equals(elementName)) {
			Attribute attributeRef = element.getAttributeByName(new QName("ref"));
			if (attributeRef != null) {
				if (attributeRef.getValue().endsWith(":extension")) {
					skip(xml);
					return null;
				}
			}
			result = element(schema, element, xml);
		} else 
		skip(xml);
		return result;
	}
	
	private XsdSequence sequence(XsdSchema schema, XMLEventReader xml) throws Exception {
		XsdSequence result = new XsdSequence();
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				XsdNode node = node(schema, event.asStartElement(), xml);
				if (node != null) {
					result.nodes.add(node);
				}
			} else if (event.isEndElement()) {
				assertEnd(event, "sequence");
				return result;
			}
		}
		return result;
	}
	
	private XsdChoice choice(XsdSchema schema, XMLEventReader xml) throws Exception {
		XsdChoice result = new XsdChoice();
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				XsdNode node = node(schema, event.asStartElement(), xml);
				if (node != null) {
					result.nodes.add(node);
				}
			} else if (event.isEndElement()) {
				assertEnd(event, "choice");
				return result;
			}
		}
		return result;
	}

	public XsdElement element(XsdSchema schema, StartElement element, XMLEventReader xml) throws Exception {
		XsdElement result = new XsdElement();
		
		Attribute attributeName = element.getAttributeByName(new QName("name"));
		if (attributeName != null) {
			result.name = attributeName.getValue();
		}

		Attribute attributeType = element.getAttributeByName(new QName("type"));
		if (attributeType != null) {
			result.type = schema.getType(attributeType.getValue());
		}
		
		Attribute attributeMinOccurs = element.getAttributeByName(new QName("minOccurs"));
		if (attributeMinOccurs != null) {
			result.minOccours = Integer.parseInt(attributeMinOccurs.getValue());
		}
		
		Attribute attributeMaxOccurs = element.getAttributeByName(new QName("maxOccurs"));
		if (attributeMaxOccurs != null) {
			if ("unbounded".equals(attributeMaxOccurs.getValue())) {
				result.maxOccours = Integer.MAX_VALUE;
			} else {
				result.maxOccours = Integer.parseInt(attributeMaxOccurs.getValue());
			}
		}
		
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement e = event.asStartElement();
				String elementName = e.getName().getLocalPart();
				if ("simpleType".equals(elementName)) {
					result.type = simpleType(schema, xml);
				} else if ("complexType".equals(elementName)) {
					result.type = complexType(schema, xml);
				} else 
				skip(xml);
			} else if (event.isEndElement()) {
				assertEnd(event, "element");
				return result;
			}
		}

		return result;
	}

	
	public static boolean deleteDirectory(File dir) {
	    if(! dir.exists() || !dir.isDirectory())    {
	        return false;
	    }

	    String[] files = dir.list();
	    for(int i = 0, len = files.length; i < len; i++)    {
	        File f = new File(dir, files[i]);
	        if(f.isDirectory()) {
	            deleteDirectory(f);
	        }else   {
	            f.delete();
	        }
	    }
	    return dir.delete();
	}
	
	public static void main(String[] args) throws Exception {
		File dir = new File("C:\\Users\\eberhard\\git\\open-ech\\src\\main\\generated");
		deleteDirectory(dir);
		
		XsdReader reader = new XsdReader();

		XsdSchema schema = reader.read("http://www.ech.ch/xmlns/eCH-0211/1/eCH-0211-1-0.xsd");
		schema.print();
		
		schema = reader.read("http://www.ech.ch/xmlns/eCH-0020/3/eCH-0020-3-0.xsd");
		schema.print();
		
		schema = reader.read("http://www.ech.ch/xmlns/eCH-0020/3/eCH-0020-2-3.xsd");
		schema.print();
	}

}
