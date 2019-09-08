package ch.openech.xml;

import org.minimalj.metamodel.generator.GeneratorEntity;
import org.w3c.dom.Element;

public class XsdMjEntity extends GeneratorEntity {

	private Element element;

	public XsdMjEntity(String name) {
		super(name);
	}

	public XsdMjEntity(MjEntityType type) {
		super(type);
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
