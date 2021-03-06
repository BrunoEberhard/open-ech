package ch.openech.xml;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.model.Code;
import org.minimalj.model.properties.Properties;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.Codes;
import org.minimalj.util.FieldUtils;
import org.minimalj.util.StringUtils;

import ch.openech.model.Any;

public class EchReader implements AutoCloseable {
	public static final Logger LOG = Logger.getLogger(EchReader.class.getName());

	private final XMLEventReader xml;
	
	public EchReader(InputStream inputStream) {
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			xml = inputFactory.createXMLEventReader(inputStream);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}
	
	public EchReader(Reader reader) {
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			xml = inputFactory.createXMLEventReader(reader);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public static <T> T deserialize(String s) {
		EchReader reader = new EchReader(new StringReader(s));
		return (T) reader.read();
	}

	@Override
	public void close() {
		if (xml != null) {
			try {
				xml.close();
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
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
		Object result = null;
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();

				String namespace = startElement.getName().getNamespaceURI();
				String rootElementName = startElement.getName().getLocalPart();

				XsdModel model = EchSchemas.getXsdModel(namespace);
				Objects.requireNonNull(model, "No namespace: " + namespace + " for " + rootElementName);

				rootElementName = StringUtils.upperFirstChar(rootElementName);
				Class<?> clazz = getClass(rootElementName, namespace);
				result = read(clazz);

				readAttributes(startElement, result);
			}
		}
		return result;
	}
	
	private void readAttributes(StartElement startElement, Object result) {
		Iterator<Attribute> attributes = startElement.getAttributes();
		while (attributes.hasNext()) {
			Attribute attribute = attributes.next();
			String attributeName = attribute.getName().getLocalPart();
			String valueString = attribute.getValue();
			PropertyInterface property = Properties.getProperty(result.getClass(), attributeName);
			if (property != null) {
				Object value = parse(valueString, property.getClazz());
				property.setValue(result, value);
			}
		}
	}
	
	private Class<?> getClass(String name, String namespace) {
		try {
			String packageName = EchSchemas.packageName(namespace);
			return Class.forName(packageName + "." + name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	/*
	 * Einlesen des Inhalts eines XML-Elements (das Start Tag wurde bereits gelesen).
	 * Massgebend ist, welche Tags vom Parser angetroffen werden. Zu diesen wird ein
	 * Match auf der angebebenen Klasse gesucht. Das Resultat wird nicht vom Tag
	 * bestimmt, sondern eben als Parameter mitgegeben.
	 * 
	 * Eine Klasse kann also weitere Felder besitzen, die von einem XML Dokument nicht
	 * mitgeliefert werden.
	 */
	private <T> T read(Class<T> clazz) throws XMLStreamException {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Read entity: " + clazz.getSimpleName());
		}
		
		T result = null;
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (result == null && (event.isStartElement() || event.isEndElement())) {
				result = CloneHelper.newInstance(clazz);
			}
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String elementName = startElement.getName().getLocalPart();
				elementName = StringUtils.lowerFirstChar(elementName);
				
				PropertyInterface property = Properties.getProperty(clazz, elementName);
				if (property != null) {
					if (LOG.isLoggable(Level.FINE)) {
						LOG.fine("Element: " + elementName + " -> Property: " + property.getPath());
					}
					
					boolean isList = FieldUtils.isList(property.getClazz());
					if (isList) {
						Object value = read(property.getGenericClass());
						readAttributes(startElement, value);
						if (property.getValue(result) == null) {
							property.setValue(result, new ArrayList<>());
						}
						((List) property.getValue(result)).add(value);
					} else {
						Object value = read(property.getClazz());
						if (value != null) {
							readAttributes(startElement, value);
							property.setValue(result, value);
						}
					}
				} else if (Properties.getProperty(clazz, "any") != null) {
					property = Properties.getProperty(clazz, "any");
					Any any = (Any) property.getValue(result);

					String namespace = startElement.getName().getNamespaceURI();
					String rootElementName = startElement.getName().getLocalPart();

					XsdModel model = EchSchemas.getXsdModel(namespace);
					Objects.requireNonNull(model, "No namespace: " + namespace + " for " + rootElementName);

					Class<?> elementClass = getClass(StringUtils.upperFirstChar(rootElementName), namespace);
					any.object = read(elementClass);
					any.elementName = rootElementName;
					any.namespace = namespace;
				} else {
					LOG.warning("Element: " + elementName + " -> No Property in " + clazz.getName());
					skip(xml);
				}
			} else if (event.isCharacters()) {
				String s = event.asCharacters().getData().trim();
				if (!s.isEmpty()) {
					result = parse(s, clazz);
				}
			} else if (event.isEndElement()) {
				break;
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T parse(String valueString, Class<T> clazz) {
		T value;
		if (Code.class.isAssignableFrom(clazz) && !StringUtils.isEmpty(valueString)) {
			value = (T) Codes.findCode((Class<Code>) clazz, valueString);
		} else {
			if (clazz.isEnum()) {
				valueString = valueString.replace('.', '_');
			}
			value = FieldUtils.parse(valueString, clazz);
		}
		return value;
	}
	
	public static void skip(XMLEventReader xml) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				if (LOG.isLoggable(Level.FINE)) {
					LOG.fine("Skipping XML Element: " + event.asStartElement().getName().getLocalPart());
				}
				skip(xml);
			} else if (event.isEndElement()) break;
			// else ignore
		}
	}

}
