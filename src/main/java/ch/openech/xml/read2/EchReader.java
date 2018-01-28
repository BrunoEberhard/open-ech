package ch.openech.xml.read2;

import static ch.openech.xml.read.StaxEch.skip;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.util.CloneHelper;

public class EchReader {
	private static Logger log = Logger.getLogger(EchReader.class.getName());

	private final EchReader parent;
	
	private final List<EchAttributeReader> readers = new ArrayList<>();
	
	public EchReader() {
		this.parent = null;
		readers.add(new EchReaderSimpleValues());
	}
	
	public EchReader(EchReader parent) {
		this.parent = parent;
	}

	public <T> T read(XMLEventReader xml, Class<T> clazz) throws XMLStreamException {
		T result = CloneHelper.newInstance(clazz);
		
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String elementName = startElement.getName().getLocalPart();
				if (!read(xml, elementName, result)) {
					skip(xml);
					log.info("Skipping " + elementName + " of " + clazz.getSimpleName());
				}
			} 
		}
		return result;
	}
	
	private boolean read(XMLEventReader xml, String elementName, Object object) throws XMLStreamException {
		for (EchAttributeReader attributeReader : readers) {
			if (attributeReader.read(xml, elementName, object)) {
				return true;
			}
		}
		if (parent != null) {
			return parent.read(xml, elementName, object);
		}
		return false;
	}
	
	//
	
	/*
	public static void main(String[] args) {
		EchSchema schema = EchSchema.getNamespaceContext(129, "1.0");
		WriterEch0129 writer = new WriterEch0129(schema);
		
		StringWriter writer = new StringWriter();
		
		factory = new StaxWriterFactory(writer);
		factory.setPrefix("e90", WriterEch0090.URI);
        factory.setPrefix("xsi", XMLSchema_URI);
		
		writer.building(document, building);
		
	}
	
	public void write(building building) {
		try {
			startDocument(context, 196, "building");
			write(this, taxStatement, URI);
			endDocument();
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}
	*/

}
