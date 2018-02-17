package ch.openech.xml;

import java.io.Writer;
import java.util.Optional;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.minimalj.metamodel.model.MjEntity;

import ch.openech.xml.write.IndentingXMLStreamWriter;

public class EchWriter implements AutoCloseable {
	public static final String XMLSchema_URI = "http://www.w3.org/2001/XMLSchema-instance";

	private static final EchClassNameGenerator CLASS_NAME_GENERATOR = new EchClassNameGenerator();
	
	private final Writer writer;
	private XMLStreamWriter xmlStreamWriter;

	public EchWriter(Writer writer) {
		this.writer = writer;

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			xmlStreamWriter = new IndentingXMLStreamWriter(factory.createXMLStreamWriter(writer));
			xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}
	public void write(Object object) throws XMLStreamException {
		write(object, true);
	}
	
	private void write(Object object, boolean root) throws XMLStreamException {
		Class<?> clazz = object.getClass();
		String packageName = clazz.getPackage().getName();
		String namespace = EchSchemas.getNamespaceByPackage(packageName);
		
		XsdModel xsdModel = EchSchemas.getXsdModel(namespace);
		if (xsdModel == null) {
			throw new IllegalArgumentException(namespace + " for " + packageName + " not found");
		}

		Optional<MjEntity> entityOptional = xsdModel.getEntities().stream().filter(e -> CLASS_NAME_GENERATOR.apply(e).equals(clazz.getSimpleName())).findFirst();
		if (!entityOptional.isPresent()) {
			throw new IllegalArgumentException("Entity " + clazz.getSimpleName() + " not found");
		}
		MjEntity entity = entityOptional.get();
		
		if (root) {
			xmlStreamWriter.writeStartElement(xsdModel.getPrefix(), entity.name, xsdModel.getNamespace());
			
			setPrefixs(xsdModel);
			writeNamespaces(xsdModel);
		} else {
			xmlStreamWriter.writeStartElement(entity.name);
		}
		
		xmlStreamWriter.writeEndElement();
	}
	
	private void setPrefixs(XsdModel xsdModel) throws XMLStreamException {
		xmlStreamWriter.setPrefix("xsi", XMLSchema_URI);
		xsdModel.getNamespaceByPrefix().forEach((prefix, namespace) -> setPrefix(prefix, namespace));
	}

	private void setPrefix(String prefix, String namespace) {
		try {
			xmlStreamWriter.setPrefix(prefix, namespace);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeNamespaces(XsdModel xsdModel) throws XMLStreamException {
		xmlStreamWriter.writeNamespace("xsi", XMLSchema_URI);
		xsdModel.getNamespaceByPrefix().forEach((prefix, namespace) -> writeNamespace(prefix, namespace));
	}

	protected void writeNamespace(String prefix, String namespace) {
		try {
			xmlStreamWriter.writeNamespace(prefix, namespace);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	/*
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
	*/
	
	@Override
	public void close() throws Exception {
		xmlStreamWriter.writeEndDocument();

		xmlStreamWriter.flush();
		writer.flush();
	}

}
