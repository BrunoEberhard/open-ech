package ch.openech.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
		Element rootElement = xsdModel.getRootElement(StringUtils.lowerFirstChar(clazz.getSimpleName()));

		xmlStreamWriter.writeStartElement(xsdModel.getPrefix(), rootElement.getAttribute("name"), xsdModel.getNamespace());
		setPrefixs(xsdModel);
		writeNamespaces(xsdModel);
		writeContent(object, rootElement);
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
				String namespace = element.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace");
				xmlStreamWriter.writeStartElement(namespace, element.getAttribute("name"));
				writeContent(object, element);
				xmlStreamWriter.writeEndElement();
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private void writeContent(Object object, Element element) throws XMLStreamException {
		List<Element> attributes = XsdModel.getList(element, "attribute");
		if (attributes != null) {
			for (Element attribute : attributes) {
				String attributeName = attribute.getAttribute("name");
				Object value = Properties.getProperty(object.getClass(), attributeName).getValue(object);
				xmlStreamWriter.writeAttribute(attributeName, value.toString());
			}
		}

		Element simpleType = XsdModel.get(element, "simpleType");
		if (simpleType != null) {
			xmlStreamWriter.writeCharacters(object.toString());
			return;
		}

		Element complexType = XsdModel.get(element, "complexType");
		if (complexType != null) {
			writeContent(object, complexType);
			return;
		}

		Element complexContent = XsdModel.get(element, "complexContent");
		if (complexContent != null) {
			Element extension = XsdModel.get(complexContent, "extension");
			if (extension != null) {
				String base = extension.getAttribute("base");
				MjEntity entity = xsdModel.findEntity(base);
				Element typeElement = entity.getElement();
				writeContent(object, typeElement);
				writeContent(object, extension);
			}

			Element restriction = XsdModel.get(complexContent, "restriction");
			if (restriction != null) {
				// base is ignored
				writeContent(object, restriction);
			}
		}
		
		Element sequence = XsdModel.get(element, "sequence");
		if (sequence != null) {
			XsdModel.forEachChild(sequence, new ElementWriter(object));
		}

		Element choice = XsdModel.get(element, "choice");
		if (choice != null) {
			XsdModel.forEachChild(choice, new ElementWriter(object));
		}

		String type = element.getAttribute("type");
		if (!StringUtils.isEmpty(type)) {
			MjEntity entity = xsdModel.findEntity(type);
			if (entity.isPrimitiv() || entity.isEnumeration()) {
				xmlStreamWriter.writeCharacters(object.toString());
			} else {
				Element typeElement = entity.getElement();
				writeContent(object, typeElement);
			}
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
