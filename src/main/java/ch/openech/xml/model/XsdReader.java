package ch.openech.xml.model;

import static ch.openech.xml.read.StaxEch.skip;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.model.properties.FlatProperties;
import org.minimalj.util.StringUtils;

import ch.openech.xml.model.XsdType.XsdTypeComplex;

public class XsdReader {
	private static Logger log = Logger.getLogger(XsdReader.class.getName());

	public XsdSchema read(String schemaLocation) throws Exception {
		System.out.println("Read: " + schemaLocation);
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		try (InputStream inputStream = new URL(schemaLocation).openStream()) {
			XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
			XsdSchema result = read(xml);
			xml.close();
			return result;
		}
	}

	public XsdSchema read(XMLEventReader xml) throws Exception {
		XsdSchema result = null;
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				if ("schema".equals(elementName)) {
					result = schema(xml);
					result.namespace = element.getAttributeByName(new QName("targetNamespace")).getValue();
				} else {
					skip(xml);
				}
			} else if (event.isEndElement()) {
				return result;
			}
		}
		return result;
	}

	public XsdSchema schema(XMLEventReader xml) throws Exception {
		XsdSchema result = new XsdSchema();
		
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
					result.imports.put(prefix, schema);
					skip(xml);
				} else if ("complexType".equals(elementName)) {
					XsdType type = complexType(xml);
					Attribute attributeName = element.getAttributeByName(new QName("name"));
					if (type != null) {
						type.name = attributeName.getValue();
						result.types.add(type);
					} else {
						System.out.println("Strange: " + attributeName.getValue());
					}
				} else if ("simpleType".equals(elementName)) {
					XsdType type = simpleType(xml);
					Attribute attributeName = element.getAttributeByName(new QName("name"));
					if (type != null) {
						type.name = attributeName.getValue();
						result.types.add(type);
					} else {
						System.out.println("Strange: " + attributeName.getValue());
					}
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

	public XsdType simpleType(XMLEventReader xml) throws Exception {
		XsdType result = null;
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				if ("restriction".equals(elementName)) {
					Attribute attributeBase = element.getAttributeByName(new QName("base"));
					if (attributeBase != null) {
						result = restriction(xml);
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
	
	public XsdType restriction(XMLEventReader xml) throws Exception {
		XsdType result = new XsdType();
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				if (StringUtils.equals(elementName, "minLength", "maxLength")) {
					Attribute attribute = element.getAttributeByName(new QName("value"));
					if (attribute != null) {
						Integer value = Integer.parseInt(attribute.getValue());
						FlatProperties.getProperty(result.getClass(), elementName).setValue(result, value);
					}
				} else if (StringUtils.equals(elementName, "minInclusive", "maxInclusive")) {
					Attribute attribute = element.getAttributeByName(new QName("value"));
					if (attribute != null) {
						Long value = Long.parseLong(attribute.getValue());
						FlatProperties.getProperty(result.getClass(), elementName).setValue(result, value);
					}
				} else if (StringUtils.equals(elementName, "enumeration")) {
					Attribute attribute = element.getAttributeByName(new QName("value"));
					if (attribute != null) {
						result.enumeration.add(attribute.getValue());
					}
				}
				skip(xml);
			} else if (event.isEndElement()) {
				assertEnd(event, "restriction");
				return result;
			}
		}
		return result;
	}
	
	public XsdType complexType(XMLEventReader xml) throws Exception {
		XsdTypeComplex result = null;
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				if ("sequence".equals(elementName)) {
					result = complexType(new XsdTypeComplex(), xml);
				} else if ("choice".equals(elementName)) {
					result = complexType(new XsdTypeComplex(), xml);
				} else 
				skip(xml);
			} else if (event.isEndElement()) {
				assertEnd(event, "complexType");
				return result;
			}
		}
		return result;
	}

	public XsdTypeComplex complexType(XsdTypeComplex result, XMLEventReader xml) throws Exception {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();
				// momentan wird rekursives sequence / choice ignoriert 
				if ("sequence".equals(elementName)) {
					complexType(result, xml);
				} else if ("choice".equals(elementName)) {
					complexType(result, xml);
				} else if ("element".equals(elementName)) {
					result.elements.add(element(element));
					skip(xml);
				} else 
				skip(xml);
			} else if (event.isEndElement()) {
				return result;
			}
		}
		return result;
	}
	
	public XsdElement element(StartElement element) throws Exception {
		XsdElement result = new XsdElement();
		Attribute attributeName = element.getAttributeByName(new QName("name"));
		if (attributeName != null) {
			result.name = attributeName.getValue();
		}
		Attribute attributeType = element.getAttributeByName(new QName("type"));
		if (attributeType != null) {
			result.typeName = attributeType.getValue();
		}
		Attribute attributeMinOccurs = element.getAttributeByName(new QName("minOccurs"));
		if (attributeMinOccurs != null) {
			result.minOccours = Integer.parseInt(attributeMinOccurs.getValue());
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		XsdSchema schema = new XsdReader().read("http://www.ech.ch/xmlns/eCH-0020/3/eCH-0020-3-0.xsd");
		schema.print();
	}

}
