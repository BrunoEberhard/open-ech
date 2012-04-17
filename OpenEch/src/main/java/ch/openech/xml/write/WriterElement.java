package ch.openech.xml.write;

import java.util.Set;

import javax.xml.stream.XMLStreamWriter;

import ch.openech.mj.db.model.ColumnAccess;
import ch.openech.mj.util.StringUtils;


public class WriterElement {

	private XMLStreamWriter writer;
	private String namespaceURI;
	private WriterElement child;
	
	public WriterElement(XMLStreamWriter writer, String namespaceURI) {
		this.writer = writer;
		this.namespaceURI = namespaceURI;
	}

	public WriterElement create(String childNameSpaceURI, String localName) throws Exception {
		flush();
		writer.writeStartElement(namespaceURI, localName); // OUR namespaceURI not the child's
		child = new WriterElement(writer, childNameSpaceURI);
		return child;
	}

	public void text(String localName, String text) throws Exception {
		flush();
		writer.writeStartElement(namespaceURI, localName);
		writer.writeCharacters(text);
		writer.writeEndElement();
	}

	public void values(Object object, String... keys) throws Exception {
		for (String key : keys) {
			Object value = ColumnAccess.getValue(object, key);
			if (value != null) {
				String string = value.toString();
				if (!StringUtils.isBlank(string)) {
					text(key, string);
				}
			}
		}
	}

	public void values(Object object) throws Exception {
		Set<String> keys = ColumnAccess.getKeys(object.getClass());
		values(object, (String[])keys.toArray(new String[keys.size()]));
	}
	
	public void writeAttribute(String namespaceURI, String localName, String value) throws Exception {
		writer.writeAttribute(namespaceURI, localName, value);
		// writer.writeAttribute(localName, value);
	}
	
	public void flush() throws Exception {
		if (child != null) {
			child.flush();
			writer.writeEndElement();
			child = null;
		}
	}
	
}
