package ch.openech.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.metamodel.model.MjModel;
import org.minimalj.model.properties.FlatProperties;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.FieldUtils;
import org.minimalj.util.StringUtils;

import ch.openech.xml.read.StaxEch;

public class EchReader implements AutoCloseable {

	private final XMLEventReader xml;
	private MjModel model;
	
	// namespace -> location
	// public final Map<String, String> locations = new HashMap<>();

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

				String namespace = startElement.getName().getNamespaceURI();
				String rootElementName = startElement.getName().getLocalPart();

				MjModel model = EchSchemas.getModel(namespace);
				if (model == null) {
					System.out.println("No namespace: " + namespace + " for " + rootElementName);
				}
				MjEntity entity = model.getEntity(StringUtils.upperFirstChar(rootElementName));
				if (entity == null) {
					entity = model.getEntity("RootElement_" + rootElementName);
				}
				return read(getClass(entity));
			} 
		}
		return null;
	}
	
	private Class getClass(MjEntity entity) {
		try {
			return Class.forName(entity.packageName + "." + entity.getClassName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Object read(Class<?> clazz) throws XMLStreamException {
		Object result = null;
		
		System.out.println("Read entity: " + clazz.getSimpleName());
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				if (result == null) {
					result = CloneHelper.newInstance(clazz);
				}
				
				StartElement startElement = event.asStartElement();
				String elementName = startElement.getName().getLocalPart();
					
				System.out.println("Element: " + elementName);
				PropertyInterface property = FlatProperties.getProperty(clazz, elementName, true);
				if (property != null) {
					Object value;
					if (FieldUtils.isList(property.getClazz())) {
						value = readList(property.getGenericClass());
					} else {
						value = read(property.getClazz());
					}
					property.setValue(result, value);
				} else {
					System.out.println("No property for " + elementName);
					StaxEch.skip(xml);
				}
			} else if (event.isCharacters()) {
				String s = event.asCharacters().getData().trim();
				if (!StringUtils.isEmpty(s)) {
					System.out.println("Characters: " + s);
					result = FieldUtils.parse(s, clazz);
				}
			} else if (event.isEndElement()) {
				return result;
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private List readList(Class<?> clazz) throws XMLStreamException {
		List result = new ArrayList<>();
		
		System.out.println("Read list: " + clazz.getSimpleName());
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				Object element = read(clazz);
				result.add(element);
			} else if (event.isEndElement()) {
				return result;
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		InputStream is = EchReader.class.getResourceAsStream("/ch/ech/data/eCH0071_canton.xml");
		EchReader reader = new EchReader(is);
		reader.read();
	}
	
}
