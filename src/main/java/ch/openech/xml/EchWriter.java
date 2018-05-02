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

	private XsdModel xsdModel;
	
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
		Class<?> clazz = object.getClass();
		xsdModel = getXsdModel(clazz);
		Optional<MjEntity> entityOptional = xsdModel.getEntities().stream().filter(e -> CLASS_NAME_GENERATOR.apply(e).equals(clazz.getSimpleName())).findFirst();
		MjEntity entity = entityOptional.orElseThrow(() -> new IllegalArgumentException("Entity " + clazz.getSimpleName() + " not found"));
		
		xmlStreamWriter.writeStartElement(xsdModel.getPrefix(), entity.getElement().getAttribute("name"), xsdModel.getNamespace());
		setPrefixs(xsdModel);
		writeNamespaces(xsdModel);
		writeContent(object, entity.getElement());
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
	
	private void writeElement(Object object, Element element) throws XMLStreamException {
		if (object != null) {
			xmlStreamWriter.writeStartElement(element.getAttribute("name"));
			writeContent(object, element);
			xmlStreamWriter.writeEndElement();
		}
	}
	
	private void writeContent(Object object, Element element) throws XMLStreamException {
		Element complexType = XsdModel.get(element, "complexType");
		if (complexType != null) {
			complexType(object, complexType);
		}

		Element simpleType = XsdModel.get(element, "simpleType");
		if (simpleType != null) {
			xmlStreamWriter.writeCharacters(object.toString());
		}
		
		String type = element.getAttribute("type");
		if (!StringUtils.isEmpty(type)) {
			MjEntity entity = xsdModel.findEntity(type);
			if (entity.isPrimitiv() || entity.isEnumeration()) {
				xmlStreamWriter.writeCharacters(object.toString());
			} else {
				Element typeElement = entity.getElement();
				complexType(object, typeElement);
			}
		}
		
	}
	
	private void complexType(Object object, Element element) throws XMLStreamException {
		Element sequence = XsdModel.get(element, "sequence");
		if (sequence != null) {
			sequence(object, sequence);
		}
		Element choice = XsdModel.get(element, "choice");
		if (choice != null) {
			choice(object, sequence);
		}
	}
	
	private void sequence(Object object, Element element) throws XMLStreamException {
		XsdModel.forEachChild(element, new SequenceVisitor(object));
	}

	private void choice(Object object, Element element) throws XMLStreamException {
		XsdModel.forEachChild(element, new SequenceVisitor(object));
	}

	private class SequenceVisitor implements Consumer<Element> {
		private final Object object;
		
		public SequenceVisitor(Object object) {
			this.object = object;
		}
		
		@Override
		public void accept(Element element) {
			String name = element.getAttribute("name");
			if (StringUtils.isEmpty(name))
				return;

			if (object instanceof Collection) {
				Collection<?> collection = (Collection<?>) object;
				collection.forEach(value -> {
					try {
						writeElement(value, element);
					} catch (XMLStreamException e) {
						throw new RuntimeException(e);
					}
				});
			} else {
				Object value = FlatProperties.getValue(object, name);
				try {
					writeElement(value, element);
				} catch (XMLStreamException e) {
					throw new RuntimeException(e);
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
