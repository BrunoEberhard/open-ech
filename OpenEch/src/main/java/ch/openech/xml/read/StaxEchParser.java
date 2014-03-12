package ch.openech.xml.read;

import javax.xml.stream.XMLStreamException;

public interface StaxEchParser<T> {

	public void process(String xml) throws XMLStreamException;
	
	public T getLastInserted();
}
