package ch.openech.xml.write;

import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.minimalj.model.properties.FlatProperties;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.StringUtils;

import ch.openech.model.types.EchCode;

public class WriterElement {
	public static final String XMLSchema_URI = "http://www.w3.org/2001/XMLSchema-instance";

	private XMLStreamWriter writer;
	private String namespaceURI;
	private WriterElement child;
	
	public WriterElement(Writer writer, String namespaceURI) {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			this.writer = new IndentingXMLStreamWriter(factory.createXMLStreamWriter(writer));
			this.writer.writeStartDocument("UTF-8", "1.0");
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
		this.namespaceURI = namespaceURI;
	}
	
	public WriterElement(XMLStreamWriter writer, String namespaceURI) {
		this.writer = writer;
		this.namespaceURI = namespaceURI;
	}

	public WriterElement create(String childNameSpaceURI, String localName) throws XMLStreamException {
		flush();
		writer.writeStartElement(namespaceURI, localName); // OUR namespaceURI not the child's
		child = new WriterElement(writer, childNameSpaceURI);
		return child;
	}

	public void startDocument(EchSchema context, int schemaNumber, String rootType) throws XMLStreamException {
		List<Integer> namespaceNumbers = new ArrayList<Integer>(context.getNamespaceNumbers());
		Collections.sort(namespaceNumbers);
		
		setPrefixs(context, namespaceNumbers);
		writer.writeStartElement(namespaceURI, rootType);
		writeXmlSchemaLocation(context, schemaNumber);
		writeNamespaces(context, namespaceNumbers);
	}

	private void writeXmlSchemaLocation(EchSchema context, int schemaNumber) throws XMLStreamException {
		writer.writeAttribute(WriterEch0020.XMLSchema_URI, "schemaLocation",
				context.getNamespaceURI(schemaNumber) + " " + 
				context.getNamespaceLocation(schemaNumber));
	}

	private void setPrefixs(EchSchema namespaceContext, List<Integer> namespaceNumbers) throws XMLStreamException {
		writer.setPrefix("xsi", XMLSchema_URI);
		for (int number : namespaceNumbers) {
			writer.setPrefix("e" + number, namespaceContext.getNamespaceURI(number));
		}
	}
	
	private void writeNamespaces(EchSchema namespaceContext, List<Integer> namespaceNumbers) throws XMLStreamException {
		writer.writeNamespace("xsi", XMLSchema_URI);
		for (int number : namespaceNumbers) {
			writer.writeNamespace("e" + number, namespaceContext.getNamespaceURI(number));
		}
		if (namespaceContext.getOpenEchNamespaceLocation() != null) {
			writer.writeNamespace("openEch", namespaceContext.getOpenEchNamespaceLocation());
		}
	}
	
	public void endDocument() throws XMLStreamException {
		flush();

		writer.writeEndElement();
		writer.writeEndDocument();

		writer.flush();
	}

	public void text(String localName, String text) throws Exception {
		flush();
		writer.writeStartElement(namespaceURI, localName);
		writer.writeCharacters(text);
		writer.writeEndElement();
	}

	public void textIfSet(String localName, String text) throws Exception {
		if (!StringUtils.isEmpty(text)) {
			flush();
			writer.writeStartElement(namespaceURI, localName);
			writer.writeCharacters(text);
			writer.writeEndElement();
		}
	}
	
	public void text(String localName, LocalDate localDate) throws Exception {
		if (localDate != null) {
			text(localName, DateTimeFormatter.ISO_DATE.format(localDate));
		}
	}

	public void text(String localName, LocalDateTime localDateTime) throws Exception {
		if (localDateTime != null) {
			text(localName, DateTimeFormatter.ISO_DATE_TIME.format(localDateTime));
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
	
	public void values(Object object, String... keys) throws Exception {
		for (String key : keys) {
			Object value = FlatProperties.getValue(object, key);
			if (value instanceof EchCode) {
				text(key, (EchCode) value);
			} else if (value instanceof LocalDate) {
				text(key, (LocalDate) value);
			} else if (value instanceof LocalDateTime) {
				text(key, (LocalDateTime) value);
			} else if (value instanceof BigDecimal) {
				text(key, ((BigDecimal) value).toPlainString());
			} else if (value != null) {
				String string = value.toString();
				if (!StringUtils.isBlank(string)) {
					text(key, string);
				}
			}
		}
	}

	public void values(Object object) throws Exception {
		values(object, false);
	}
	
	public void values(Object object, boolean skipId) throws Exception {
		Set<String> keys = FlatProperties.getProperties(object.getClass()).keySet();
		keys = new HashSet<String>(keys);
		keys.remove("id");
		values(object, keys.toArray(new String[keys.size()]));
	}
	
	public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
		writer.writeAttribute(namespaceURI, localName, value);
	}

	public void writeAttribute(String localName, String value) throws XMLStreamException {
		writer.writeAttribute(localName, value);
	}
	
	public void flush() throws XMLStreamException {
		if (child != null) {
			child.flush();
			writer.writeEndElement();
			child = null;
		}
	}
	
}
