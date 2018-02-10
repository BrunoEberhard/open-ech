package ch.openech.xml;

import java.io.Writer;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.minimalj.model.properties.Properties;

import ch.openech.xml.model.XsdElement;
import ch.openech.xml.model.XsdNode;
import ch.openech.xml.model.XsdNode.XsdSequence;
import ch.openech.xml.model.XsdSchema;
import ch.openech.xml.model.XsdType;
import ch.openech.xml.model.XsdType.XsdTypeComplex;
import ch.openech.xml.model.XsdType.XsdTypeJava;
import ch.openech.xml.model.XsdType.XsdTypeSimple;
import ch.openech.xml.write.IndentingXMLStreamWriter;
import ch.openech.xml.write.WriterEch0020;

public class EchWriter implements AutoCloseable {
	public static final String XMLSchema_URI = "http://www.w3.org/2001/XMLSchema-instance";

	private final XsdSchema schema;
	private final Writer writer;
	private XMLStreamWriter xmlStreamWriter;

	public EchWriter(Writer writer, XsdSchema schema) {
		this.writer = writer;
		this.schema = schema;

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			xmlStreamWriter = new IndentingXMLStreamWriter(factory.createXMLStreamWriter(writer));
			xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeXmlSchemaLocations() throws XMLStreamException {
		xmlStreamWriter.writeAttribute(WriterEch0020.XMLSchema_URI, "schemaLocation",
				schema.namespace + " " + schema.schemaLocation);
		schema.imports.values().forEach(schema -> {
			try {
				xmlStreamWriter.writeAttribute(WriterEch0020.XMLSchema_URI, "schemaLocation",
						schema.namespace + " " + schema.schemaLocation);
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private void setPrefixs() throws XMLStreamException {
		xmlStreamWriter.setPrefix("xsi", XMLSchema_URI);
		schema.prefixes.forEach((prefix, namespace) -> setPrefix(prefix, namespace));
	}

	private void setPrefix(String prefix, String namespace) {
		try {
			xmlStreamWriter.setPrefix(prefix, namespace);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeNamespaces() throws XMLStreamException {
		xmlStreamWriter.writeNamespace("xsi", XMLSchema_URI);
		schema.prefixes.forEach((prefix, namespace) -> writeNamespace(prefix, namespace));
	}

	protected void writeNamespace(String prefix, String namespace) {
		try {
			xmlStreamWriter.writeNamespace(prefix, namespace);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	protected void write(XsdElement element, Object object) {
		writeElement(element, object, true);
	}

	protected void writeElement(XsdElement element, Object object, boolean root) {
		if (object == null) {
			return;
		}
		try {
			XsdType type = element.getType();

			if (root) {
				xmlStreamWriter.writeStartElement(schema.getPrefix(type.schema.namespace), element.name, type.schema.namespace);
			
				setPrefixs();
				writeXmlSchemaLocations();
				writeNamespaces();
			} else {
				xmlStreamWriter.writeStartElement(element.name);
			}
			
			if (type instanceof XsdTypeJava || type instanceof XsdTypeSimple) {
				xmlStreamWriter.writeCharacters("" + object);
			} else if (type instanceof XsdTypeComplex) {
				XsdTypeComplex typeComplex = (XsdTypeComplex) type;
				writeNode(typeComplex.node, object);
			}
			xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeNode(XsdNode node, Object object) {
		if (node instanceof XsdElement) {
			XsdElement element = (XsdElement) node;
			Object value = Properties.getProperty(object.getClass(), element.name).getValue(object);
			if (value instanceof List) {
				List<?> list = (List<?>) value;
				for (Object listItem : list) {
					writeElement((XsdElement) node, listItem, false);
				}
			} else {
				writeElement((XsdElement) node, value, false);
			}
		} else if (node instanceof XsdSequence) {
			XsdSequence sequence = (XsdSequence) node;
			for (XsdNode subNode : sequence.nodes) {
				writeNode(subNode, object);
			}
		} else {
			System.out.println("TODO: " + node.getClass());
		}
	}

	@Override
	public void close() throws Exception {
		xmlStreamWriter.writeEndDocument();

		xmlStreamWriter.flush();
		writer.flush();
	}

}
