package ch.openech.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.metamodel.model.MjModel;
import org.minimalj.model.properties.Properties;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.FieldUtils;
import org.minimalj.util.StringUtils;

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
	
	public EchReader(StringReader sr) {
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			xml = inputFactory.createXMLEventReader(sr);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
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
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();

				String namespace = startElement.getName().getNamespaceURI();
				String rootElementName = startElement.getName().getLocalPart();

				MjModel model = EchSchemas.getModel(namespace);
				Objects.requireNonNull(model, "No namespace: " + namespace + " for " + rootElementName);

				MjEntity entity = model.getEntity(StringUtils.upperFirstChar(rootElementName));
				if (entity == null) {
					entity = model.getEntity("RootElement_" + rootElementName);
				}
				return read(getClass(entity));
			} 
		}
		return null;
	}
	
	private Class<?> getClass(MjEntity entity) {
		try {
			return Class.forName(entity.packageName + "." + entity.getClassName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
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
		LOG.fine("Read entity: " + clazz.getSimpleName());
		
		T result = null;
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				if (result == null) {
					result = CloneHelper.newInstance(clazz);
				}
				
				StartElement startElement = event.asStartElement();
				String elementName = startElement.getName().getLocalPart();
				elementName = StringUtils.lowerFirstChar(elementName);
				
				PropertyInterface property = Properties.getProperty(clazz, elementName);
				if (property != null) {
					LOG.fine("Element: " + elementName +" -> Property: " + property.getPath());
					
					boolean isList = FieldUtils.isList(property.getClazz());
					if (isList) {
						Object value = read(property.getGenericClass());
						if (property.getValue(result) == null) {
							property.setValue(result, new ArrayList<>());
						}
						((List) property.getValue(result)).add(value);
					} else {
						Object value = read(property.getClazz());
						if (value != null) {
							property.setValue(result, value);
						}
					}
				} else {
					LOG.warning("Element: " + elementName + " -> No Property in " + clazz.getName());
					skip(xml);
				}
			} else if (event.isCharacters()) {
				String s = event.asCharacters().getData().trim();
				if (!StringUtils.isEmpty(s)) {
					result = FieldUtils.parse(s, clazz);
					if (result == null && clazz.isEnum()) {
						result = FieldUtils.parse("_" + s, clazz);
					}
				}
			} else if (event.isEndElement()) {
				return result;
			}
		}
		return result;
	}
	
	public static void skip(XMLEventReader xml) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				LOG.fine("Skipping XML Element: " + event.asStartElement().getName().getLocalPart());
				skip(xml);
			} else if (event.isEndElement()) break;
			// else ignore
		}
	}

}
