package ch.openech.xml.read2;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public abstract class EchAttributeReader {

	public abstract boolean read(XMLEventReader xml, String elementName, Object object) throws XMLStreamException;

}
