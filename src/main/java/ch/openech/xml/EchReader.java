package ch.openech.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.model.properties.FlatProperties;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.StringUtils;

import ch.openech.xml.model.XsdReader;
import ch.openech.xml.model.XsdSchema;
import ch.openech.xml.model.XsdType;
import ch.openech.xml.model.XsdType.XsdTypeComplex;
import ch.openech.xml.model.XsdType.XsdTypeJava;
import ch.openech.xml.model.XsdType.XsdTypeSimple;
import ch.openech.xml.read.StaxEch;

public class EchReader implements AutoCloseable {

	private final XMLEventReader xml;
	private XsdSchema schema;
	
	// namespace -> location
	public final Map<String, String> locations = new HashMap<>();

	// namespace -> schema
	public final Map<String, XsdSchema> schemas = new HashMap<>();

	public EchReader(InputStream inputStream) {
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			xml = inputFactory.createXMLEventReader(inputStream);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}
	
	public EchReader(StringReader sr) {
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			xml = inputFactory.createXMLEventReader(sr);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws Exception {
		if (xml != null) {
			xml.close();
		}
	}
	
	public Object read() {
		try {
			return _read();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Object _read() throws XMLStreamException {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				if (schema == null) {
					Iterator<?> i = startElement.getAttributes();
					while (i.hasNext()) {
						Attribute attribute = (Attribute) i.next();
						if ("schemaLocation".equals(attribute.getName().getLocalPart())) {
							String namespaceAndLocation = attribute.getValue();
							String[] parts = namespaceAndLocation.split(" ");
							locations.put(parts[0], parts[1]);
							schemas.put(parts[0], new XsdReader().read(parts[1]));
						}
					}
				}
				String namespace = startElement.getName().getNamespaceURI();
				XsdSchema schema = schemas.get(namespace);
				if (schema == null) {
					System.out.println("No namespace: " + namespace + " for " + startElement.getName().getLocalPart());
				}
				XsdType type = schema.getType(startElement.getName().getLocalPart());
				return read(type);
			} 
		}
		return null;
	}
	
	private Object read(XsdType type) throws XMLStreamException {
		if (type instanceof XsdTypeSimple || type instanceof XsdTypeJava) {
			return readValue(type);
		} else if (type instanceof XsdTypeComplex) {
			XsdTypeComplex typeComplex = (XsdTypeComplex) type;
			return read(typeComplex);
		} else {
			throw new IllegalArgumentException("" + type);
		}
	}
	
	private Object read(XsdTypeComplex type) throws XMLStreamException {
		Object result = CloneHelper.newInstance(type.getClazz());
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String elementName = startElement.getName().getLocalPart();
				PropertyInterface property = FlatProperties.getProperty(type.getClazz(), elementName);
				if (property != null) {
					XsdType elementType;
					String elementNamespace = startElement.getName().getNamespaceURI();
					if (StringUtils.isEmpty(elementNamespace)) {
						elementType = type.schema.getType(elementName);
					} else {
						elementType = type.schema.getQualifiedType(elementNamespace + ":" + elementName);
					}
					Object value = read(elementType);
					property.setValue(result, value);
				} else {
					System.out.println("No property for " + elementName);
				}
			} 
		}
		return result;
	}

	private Object readValue(XsdType type) throws XMLStreamException {
		Class<?> clazz = type.getClazz();
		if (clazz == String.class) {
			return StaxEch.token(xml);
		} else if (clazz == Boolean.class) {
			return StaxEch.bulean(xml);
		} else if (clazz == Integer.class) {
			return StaxEch.integer(xml);
		} else if (clazz == Long.class) {
			return StaxEch.loong(xml);
		} else if (clazz == LocalDate.class) {
			return StaxEch.date(xml);
//		} else if (Enum.class.isAssignableFrom(clazz)) {
//			return StaxEch.enuum(xml, object, property);
		} else {
			throw new IllegalArgumentException("Unknown field type: " + clazz.getName());
		}
	}
}
