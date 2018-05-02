package ch.openech.xml;

import java.io.Writer;
import java.util.Collection;
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
	
	public void writeDocument(Object object) throws XMLStreamException {
		XsdModel xsdModel = getXsdModel(object.getClass());
		// Hier eigentlich die Entities total vergessen, es geht nur um das Xsd!
		MjEntity entity = findEntity(object);

		Element element = entity.getElement();
		String name = element.getAttribute("name");
		
		xmlStreamWriter.writeStartElement(xsdModel.getPrefix(), name, xsdModel.getNamespace());
		setPrefixs(xsdModel);
		writeNamespaces(xsdModel);
		writeElement(object, element);
		xmlStreamWriter.writeEndElement();
	}

	private XsdModel getXsdModel(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		String namespace = EchSchemas.getNamespaceByPackage(packageName);
		
		XsdModel xsdModel = EchSchemas.getXsdModel(namespace);
		if (xsdModel == null) {
			throw new IllegalArgumentException(namespace + " for " + packageName + " not found");
		}
		return xsdModel;
	}

	private MjEntity findEntity(Object object) {
		Class<?> clazz = object.getClass();

		XsdModel xsdModel = getXsdModel(clazz);
		
		Optional<MjEntity> entityOptional = xsdModel.getEntities().stream().filter(e -> CLASS_NAME_GENERATOR.apply(e).equals(clazz.getSimpleName())).findFirst();
		MjEntity entity = entityOptional.orElseThrow(() -> new IllegalArgumentException("Entity " + clazz.getSimpleName() + " not found"));
		return entity;
	}
	
	private void writeElement(Object object, Element element) throws XMLStreamException {
		xmlStreamWriter.writeStartElement(element.getNamespaceURI(), element.getLocalName());
		if (entity.type.getJavaClass() != null) {
			xmlStreamWriter.writeCData(object.toString());
		} else {
			writeContent(object, element);
		}
		xmlStreamWriter.writeEndElement();
	}
	
	private void writeContent(Object object, Element element) {
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
		
			if (object instanceof Collection) {
				Collection<?> collection = (Collection<?>) object;
				collection.forEach(value -> {
					write2(element, value);
				});
			} else {
				Object value = FlatProperties.getValue(object, name);
				write2(element, value);
			}
		}

		private void write2(Element element, Object value) {
			if (value != null) {
				if ("element".equals(element.getLocalName())) {
					XsdModel model = getXsdModel(value);
					MjEntity entity = findEntity(value, model);
					try {
						writeElement(value, entity);
					} catch (XMLStreamException e) {
						throw new RuntimeException(e);
					}
				} else if ("sequence".equals(element.getLocalName())) {
					sequence(value, element);
				} else if ("choice".equals(element.getLocalName())) {
					choice(value, element);
				} else {
					// what to do with xs:any ?
				}
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

	@Override
	public void close() throws Exception {
		xmlStreamWriter.writeEndDocument();

		xmlStreamWriter.flush();
		writer.flush();
	}

}
