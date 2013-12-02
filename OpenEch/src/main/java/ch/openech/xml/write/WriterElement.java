package ch.openech.xml.write;

import java.util.Set;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.xmlpull.v1.XmlSerializer;

import ch.openech.dm.types.EchCode;
import ch.openech.mj.model.InvalidValues;
import ch.openech.mj.model.annotation.StringLimitation;
import ch.openech.mj.model.properties.FlatProperties;
import ch.openech.mj.util.FieldUtils;
import ch.openech.mj.util.StringUtils;


public class WriterElement {

	private XmlSerializer writer;
	private String namespaceURI;
	private WriterElement child;
	
	public WriterElement(XmlSerializer writer, String namespaceURI) {
		this.writer = writer;
		this.namespaceURI = namespaceURI;
	}

	public WriterElement create(String childNameSpaceURI, String localName) throws Exception {
		flush();
		writer.startTag(namespaceURI, localName);
		child = new WriterElement(writer, childNameSpaceURI);
		return child;
	}

	public void text(String localName, String text) throws Exception {
		flush();
		writer.startTag(namespaceURI, localName);
		writer.text(text);
		writer.endTag(namespaceURI, localName);
	}

	public void textIfSet(String localName, String text) throws Exception {
		if (!StringUtils.isEmpty(text)) {
			flush();
			writer.startTag(namespaceURI, localName);
			writer.text(text);
			writer.endTag(namespaceURI, localName);
		}
	}
	
	public void text(String localName, LocalDate localDate) throws Exception {
		if (localDate != null) {
			text(localName, ISODateTimeFormat.date().print(localDate));
		}
	}

	public void text(String localName, LocalDateTime localDateTime) throws Exception {
		if (localDateTime != null) {
			text(localName, ISODateTimeFormat.date().print(localDateTime));
		}
	}

	public void text(String localName, EchCode echCode) throws Exception {
		if (echCode != null) {
			if (!InvalidValues.isInvalid(echCode)) {
				text(localName, echCode.getValue());
			} else {
				text(localName, InvalidValues.getInvalidValue(echCode));
			}
		}
	}

	public void text(String localName, Integer integer) throws Exception {
		if (integer != null) {
			text(localName, String.valueOf(integer));
		} 
	}
	
	public void text(String localName, StringLimitation textFilter) throws Exception {
		Object value = FieldUtils.getValue(textFilter);
		if (value != null) {
			text(localName, value.toString());
		} 
	}
	
	public void values(Object object, String... keys) throws Exception {
		for (String key : keys) {
			Object value = FlatProperties.getValue(object, key);
			if (value instanceof EchCode) {
				text(key, (EchCode)value);
			} else if (value != null) {
				String string = value.toString();
				if (!StringUtils.isBlank(string)) {
					text(key, string);
				}
			}
		}
	}

	public void values(Object object) throws Exception {
		Set<String> keys = FlatProperties.getProperties(object.getClass()).keySet();
		values(object, (String[])keys.toArray(new String[keys.size()]));
	}
	
	public void writeAttribute(String namespaceURI, String localName, String value) throws Exception {
		writer.attribute(namespaceURI, localName, value);
	}
	
	public void flush() throws Exception {
		if (child != null) {
			child.flush();
			child = null;
		}
	}
	
}
