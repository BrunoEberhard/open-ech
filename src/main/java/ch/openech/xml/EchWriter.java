package ch.openech.xml;

import java.io.Writer;
import java.util.Optional;
import java.util.function.Consumer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.model.properties.FlatProperties;
import org.minimalj.util.StringUtils;
import org.w3c.dom.Element;

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
		Class<?> clazz = object.getClass();
		String packageName = clazz.getPackage().getName();
		String namespace = EchSchemas.getNamespaceByPackage(packageName);
		
		XsdModel xsdModel = EchSchemas.getXsdModel(namespace);
		if (xsdModel == null) {
			throw new IllegalArgumentException(namespace + " for " + packageName + " not found");
		}
		document(object, xsdModel);
	}
	
	private void document(Object object, XsdModel xsdModel) throws XMLStreamException {
		Class<?> clazz = object.getClass();
		Optional<MjEntity> entityOptional = xsdModel.getEntities().stream().filter(e -> CLASS_NAME_GENERATOR.apply(e).equals(clazz.getSimpleName())).findFirst();
		if (!entityOptional.isPresent()) {
			throw new IllegalArgumentException("Entity " + clazz.getSimpleName() + " not found");
		}
		MjEntity entity = entityOptional.get();
		Element element = entity.getElement();
		String name = element.getAttribute("name");
		
		xmlStreamWriter.writeStartElement(xsdModel.getPrefix(), name, xsdModel.getNamespace());
		setPrefixs(xsdModel);
		writeNamespaces(xsdModel);
		write(object, entity.getElement());
		xmlStreamWriter.writeEndElement();
	}
	
	private void element(Object object, MjEntity entity) throws XMLStreamException {
//		Class<?> clazz = object.getClass();
//		Optional<MjEntity> entityOptional = xsdModel.getEntities().stream().filter(e -> CLASS_NAME_GENERATOR.apply(e).equals(clazz.getSimpleName())).findFirst();
//		if (!entityOptional.isPresent()) {
//			throw new IllegalArgumentException("Entity " + clazz.getSimpleName() + " not found");
//		}
//		MjEntity entity = entityOptional.get();
		
		xmlStreamWriter.writeStartElement(entity.name);
		if (entity.type.getJavaClass() != null) {
			xmlStreamWriter.writeCData(object.toString());
		} else {
			write(object, entity.getElement());
		}
		xmlStreamWriter.writeEndElement();
	}
	
	private void write(Object object, Element element) {
		Element complexType = XsdModel.get(element, "complexType");
		if (complexType != null) {
			complexType(object, complexType);
		}
	}
	
	private void complexType(Object object, Element element) {
		Element sequence = XsdModel.get(element, "sequence");
		if (sequence != null) {
			sequence(object, sequence);
		}
		Element choice = XsdModel.get(element, "choice");
		if (choice != null) {
			choice(object, sequence);
		}
//		Element complexContent = get(element, "complexContent");
//		if (complexContent != null) {
//			entity.properties.addAll(complexContent(complexContent));
//		}
	}
	
	private void sequence(Object object, Element element) {
		XsdModel.forEachChild(element, new SequenceVisitor(object, false));
	}

	private void choice(Object object, Element element) {
		XsdModel.forEachChild(element, new SequenceVisitor(object, true));
	}

	private class SequenceVisitor implements Consumer<Element> {
		private final Object object;
		private final boolean choice;
		
		public SequenceVisitor(Object object, boolean choice) {
			this.object = object;
			this.choice = choice;
		}
		
		@Override
		public void accept(Element element) {
			String name = element.getAttribute("name");
			if (StringUtils.isEmpty(name)) return;
			
			Object value = FlatProperties.getValue(object, name);
			if ("element".equals(element.getLocalName())) {
				write(value, element);
			} else if ("sequence".equals(element.getLocalName())) {
				sequence(value, element);
			} else if ("choice".equals(element.getLocalName())) {
				choice(value, element);
			} else {
				// what to do with xs:any ?
			}
		}
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
