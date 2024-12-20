package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.AnnotationUtil;
import org.minimalj.model.properties.Property;

import ch.ech.ech0011.ForeignerName;

public class ForeignerNameFormElement extends FormLookupFormElement<ForeignerName> implements LookupParser {
	private static final int SIZE_FIRST_NAME = AnnotationUtil.getSize(Keys.getProperty(ForeignerName.$.firstName));
	private static final int SIZE_NAME = AnnotationUtil.getSize(Keys.getProperty(ForeignerName.$.name));

	public ForeignerNameFormElement(Property property, boolean editable) {
		super(property, editable);
	}

	public ForeignerNameFormElement(ForeignerName key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return SIZE_FIRST_NAME + 1 + SIZE_NAME;
	}

	@Override
	public ForeignerName parse(String text) {
		ForeignerName object = new ForeignerName();
		if (text != null) {
			text = text.trim();
			int index = text.lastIndexOf(' ');
			if (index > 0) {
				object.firstName = text.substring(0, index);
				object.name = text.substring(index + 1, text.length());
			} else {
				object.name = text;
			}
		}
		return object;
	}

	@Override
	public Form<ForeignerName> createForm() {
		Form<ForeignerName> form = new Form<>(2);
		form.line(ForeignerName.$.firstName, ForeignerName.$.name);
		return form;
	}

}
