package ch.openech.xml.read;

import javax.xml.stream.XMLStreamException;

public interface StaxEchParser {

	public void process(String xml) throws XMLStreamException;
	
	public String getLastInsertedId();
}
