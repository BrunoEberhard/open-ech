package ch.openech.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.model.properties.Properties;
import org.minimalj.util.StringUtils;
import org.w3c.dom.Element;

import ch.openech.xml.write.IndentingXMLStreamWriter;

public class EchWriter implements AutoCloseable {
	public static final String XMLSchema_URI = "http://www.w3.org/2001/XMLSchema-instance";

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
		Optional<MjEntity> entityOptional = xsdModel.getEntities().stream().filter(e -> EchClassNameGenerator.apply(e).equals(clazz.getSimpleName())).findFirst();
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

	private void setPrefixs(XsdModel xsdModel) throws XMLStreamException {
		xmlStreamWriter.setPrefix("xsi", XMLSchema_URI);
		for (Map.Entry<String, String> entry : xsdModel.getNamespaceByPrefix().entrySet()) {
			xmlStreamWriter.setPrefix(entry.getKey(), entry.getValue());
		}
	}

	private void writeNamespaces(XsdModel xsdModel) throws XMLStreamException {
		xmlStreamWriter.writeNamespace("xsi", XMLSchema_URI);
		for (Map.Entry<String, String> entry : xsdModel.getNamespaceByPrefix().entrySet()) {
			xmlStreamWriter.writeNamespace(entry.getKey(), entry.getValue());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void writeElement(Object object, Element element) {
		if (object instanceof Collection) {
			((Collection) object).forEach(listItem -> writeElement(listItem, element));
		} else if (object != null) {
			try {
				xmlStreamWriter.writeStartElement(element.getAttribute("name"));
				writeContent(object, element);
				xmlStreamWriter.writeEndElement();
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private void writeContent(Object object, Element element) throws XMLStreamException {
		Element simpleType = XsdModel.get(element, "simpleType");
		if (simpleType != null) {
			xmlStreamWriter.writeCharacters(object.toString());
			return;
		}

		Element complexType = XsdModel.get(element, "complexType");
		if (complexType != null) {
			complexType(object, complexType);
			return;
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
	
	private void complexType(Object object, Element element) {
		Element sequence = XsdModel.get(element, "sequence");
		if (sequence != null) {
			XsdModel.forEachChild(sequence, new ElementWriter(object));
		}
		Element choice = XsdModel.get(element, "choice");
		if (choice != null) {
			XsdModel.forEachChild(choice, new ElementWriter(object));
		}
	}
	
	private class ElementWriter implements Consumer<Element> {
		private final Object object;
		
		public ElementWriter(Object object) {
			this.object = object;
		}
		
		@Override
		public void accept(Element element) {
			String name = element.getAttribute("name");
			if (!StringUtils.isEmpty(name)) {
				Object value = Properties.getProperty(object.getClass(), name).getValue(object);
				writeElement(value, element);
			}
		}		
	}
	
	@Override
	public void close() throws XMLStreamException, IOException {
		xmlStreamWriter.writeEndDocument();

		xmlStreamWriter.flush();
		writer.flush();
	}

}
