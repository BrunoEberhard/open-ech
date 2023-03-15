package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.AnnotationUtil;
import org.minimalj.model.properties.Property;

import ch.ech.ech0021.NameOfParent;

public class NameOfParentFormElement extends FormLookupFormElement<NameOfParent> implements LookupParser {
	private static final int SIZE_FIRST_NAME = AnnotationUtil.getSize(Keys.getProperty(NameOfParent.$.firstNameValue));
	private static final int SIZE_NAME = AnnotationUtil.getSize(Keys.getProperty(NameOfParent.$.officialNameValue));

	public NameOfParentFormElement(Property property, boolean editable) {
		super(property, editable);
	}

	public NameOfParentFormElement(NameOfParent key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return SIZE_FIRST_NAME + 1 + SIZE_NAME;
	}

	@Override
	public NameOfParent parse(String text) {
		NameOfParent object = new NameOfParent();
		object.officialProofOfNameOfParentsYesNo = false;
		if (text != null) {
			text = text.trim();
			if (text.endsWith("*")) {
				object.officialProofOfNameOfParentsYesNo = true;
				text = text.substring(0, text.length() - 1);
				text = text.trim();
			}
			int index = text.lastIndexOf(' ');
			if (index > 0) {
				object.firstNameValue = text.substring(0, index);
				object.officialNameValue = text.substring(index + 1, text.length());
			} else {
				object.officialNameValue = text;
			}
		}
		return object;
	}

	@Override
	public Form<NameOfParent> createForm() {
		Form<NameOfParent> form = new Form<>(2);
		form.line(NameOfParent.$.firstNameValue, NameOfParent.$.officialNameValue);
		form.line(NameOfParent.$.officialProofOfNameOfParentsYesNo);
		return form;
	}
}
