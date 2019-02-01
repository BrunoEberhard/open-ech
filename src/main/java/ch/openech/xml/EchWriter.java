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
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.StringUtils;
import org.w3c.dom.Element;

import ch.ech.ech0008.Country;
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

		String rootElementName = StringUtils.lowerFirstChar(clazz.getSimpleName());
		Element rootElement = xsdModel.getRootElement(rootElementName);

		xmlStreamWriter.writeStartElement(xsdModel.getPrefix(), rootElement.getAttribute("name"), xsdModel.getNamespace());
		setPrefixs(xsdModel);
		writeNamespaces(xsdModel);
		writeElementContent(object, rootElement);
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
				if (!EmptyObjects.isEmpty(object) || object.equals("")) {
					String name = element.getAttribute("name");
					if (xsdModel.isQualifiedElements()) {
						String namespace = element.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace");
						xmlStreamWriter.writeStartElement(namespace, name);
					} else {
						xmlStreamWriter.writeStartElement(name);
					}
					writeElementContent(object, element);
					xmlStreamWriter.writeEndElement();
				}
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	// Elemente haben entweder einen Type (spezifiziert mit dem Attribute "type").
	// Oder sie enthalten selber einen simple oder complex - Type.
	private void writeElementContent(Object object, Element element) throws XMLStreamException {
		String type = element.getAttribute("type");
		if (!StringUtils.isEmpty(type)) {
			MjEntity entity = xsdModel.findEntity(type);
			if (entity.isPrimitiv() || entity.isEnumeration()) {
				if (object.getClass().isEnum() && object.toString().startsWith("_")) {
					xmlStreamWriter.writeCharacters(object.toString().substring(1));
				} else {
					xmlStreamWriter.writeCharacters(object.toString());
				}
			} else {
				Element typeElement = xsdModel.findElement(type);
				writeElementAttributes(object, typeElement);
				writeElements(object, typeElement);
			}
		}

		Element simpleType = XsdModel.get(element, "simpleType");
		if (simpleType != null) {
			writeElementAttributes(object, simpleType);
			if (object instanceof Country) {
				// in der Destination wird AddressInformation statt eine spezielle Klasse
				// verwendet. Damit hat country aber die falsche Klasse, das wird hier
				// geradegebogen.
				xmlStreamWriter.writeCharacters(((Country) object).iso2Id);
			} else {
				xmlStreamWriter.writeCharacters(object.toString());
			}
			return;
		}

		Element complexType = XsdModel.get(element, "complexType");
		if (complexType != null) {
			writeElementAttributes(object, complexType);
			writeElements(object, complexType);
			return;
		}
	}

	private void writeElementAttributes(Object object, Element typeElement) throws XMLStreamException {
		List<Element> attributes = XsdModel.getList(typeElement, "attribute");
		if (attributes != null) {
			for (Element attribute : attributes) {
				String attributeName = attribute.getAttribute("name");
				Object value = Properties.getProperty(object.getClass(), attributeName).getValue(object);
				if (!EmptyObjects.isEmpty(value)) {
					xmlStreamWriter.writeAttribute(attributeName, value.toString());
				}
			}
		}
	}

	private void writeElements(Object object, Element element) {
		XsdModel.forEachChild(element, new ElementWriter(object));
	}

	private class ElementWriter implements Consumer<Element> {
		private final Object object;
		
		public ElementWriter(Object object) {
			this.object = object;
		}
		
		@Override
		public void accept(Element element) {
			if (element.getLocalName().equals("complexContent")) {
				Element extension = XsdModel.get(element, "extension");
				if (extension != null) {
					String base = extension.getAttribute("base");
					Element baseType = xsdModel.findElement(base);

					writeElements(object, baseType);
					writeElements(object, extension);
				}

				Element restriction = XsdModel.get(element, "restriction");
				if (restriction != null) {
					// base is ignored
					writeElement(object, restriction);
				}
				return;
			}

			if (element.getLocalName().equals("element")) {
				String name = element.getAttribute("name");
				if (!StringUtils.isEmpty(name)) {
					PropertyInterface property = Properties.getProperty(object.getClass(), name);
					if (property != null) {
						Object value = property.getValue(object);
						writeElement(value, element);
					} else {
						System.out.println("Not found: " + name + " on " + object.getClass().getSimpleName());
					}
				} else {
					// wahrscheinlich ref="extension"
				}
				return;
			}

			if (element.getLocalName().equals("choice")) {
				XsdModel.forEachChild(element, new ElementWriter(object));
				return;
			}

			if (element.getLocalName().equals("sequence")) {
				XsdModel.forEachChild(element, new ElementWriter(object));
				return;
			}

			if (element.getLocalName().equals("any")) {
				PropertyInterface property = Properties.getProperty(object.getClass(), "any");
				if (property != null) {
					Any any = (Any) property.getValue(object);
					if (any != null && !EmptyObjects.isEmpty(any.object)) {
						XsdModel model = xsdModel.findModel(any.namespace);
						try {
							String prefix = model.getPrefix();
							String namespace = any.namespace;
							xmlStreamWriter.setPrefix(prefix, namespace);
							Element e = model.getRootElement(any.elementName);
							writeElement(any.object, e);
						} catch (XMLStreamException e) {
							throw new RuntimeException(e);
						}
					}
				} else {
					System.out.println("Not found: 'any' on " + object.getClass().getSimpleName());
				}
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
