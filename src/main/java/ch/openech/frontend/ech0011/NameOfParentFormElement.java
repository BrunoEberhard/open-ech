package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormatLookupFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.AnnotationUtil;
import org.minimalj.model.properties.PropertyInterface;

import ch.ech.ech0021.NameOfParent;

public class NameOfParentFormElement extends FormatLookupFormElement<NameOfParent> {
	private static final int SIZE_FIRST_NAME = AnnotationUtil.getSize(Keys.getProperty(NameOfParent.$.firstNameValue));
	private static final int SIZE_NAME = AnnotationUtil.getSize(Keys.getProperty(NameOfParent.$.officialNameValue));

	public NameOfParentFormElement(PropertyInterface property, boolean editable) {
		super(property, true, editable);
	}

	public NameOfParentFormElement(NameOfParent key, boolean editable) {
		super(key, true, editable);
	}

	@Override
	protected int getAllowedSize() {
		return SIZE_FIRST_NAME + 1 + SIZE_NAME;
	}

	@Override
	protected void parse(NameOfParent object, String text) {
		object.officialNameValue = object.firstNameValue = null;
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
//		if (object.officialNameValue != null && object.officialNameValue.length() > SIZE_FIRST_NAME) {
//			InvalidValues.markInvalid(object, text, "Nachname zu lang");
//		} else if (object.firstNameValue != null && object.firstNameValue.length() > SIZE_NAME) {
//			InvalidValues.markInvalid(object, text, "Vorname(n) zu lang");
//		}
	}

	@Override
	public Form<NameOfParent> createForm() {
		Form<NameOfParent> form = new Form<>(2);
		form.line(NameOfParent.$.firstNameValue, NameOfParent.$.officialNameValue);
		form.line(NameOfParent.$.officialProofOfNameOfParentsYesNo);
		return form;
	}
}
