package ch.openech.xml.write;


public interface WriterElement {
	
	public WriterElement create(String URI, String tagName) throws Exception;
	
	public void text(String tagName, String text) throws Exception;
	
	public void values(Object object) throws Exception;
	
	public void values(Object object, String... keys) throws Exception;

	public void writeAttribute(String namespaceURI, String localName, String value) throws Exception;
	
	public void flush() throws Exception;
	
}
