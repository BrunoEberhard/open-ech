package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormatLookupFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.AnnotationUtil;
import org.minimalj.model.properties.PropertyInterface;

import ch.ech.ech0011.ForeignerName;

public class ForeignerNameFormElement extends FormatLookupFormElement<ForeignerName> {
	private static final int SIZE_FIRST_NAME = AnnotationUtil.getSize(Keys.getProperty(ForeignerName.$.firstName));
	private static final int SIZE_NAME = AnnotationUtil.getSize(Keys.getProperty(ForeignerName.$.name));

	public ForeignerNameFormElement(PropertyInterface property, boolean editable) {
		super(property, true, editable);
	}

	public ForeignerNameFormElement(ForeignerName key, boolean editable) {
		super(key, true, editable);
	}

	@Override
	protected int getAllowedSize() {
		return SIZE_FIRST_NAME + 1 + SIZE_NAME;
	}

	@Override
	protected void parse(ForeignerName object, String text) {
		object.name = object.firstName = null;
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
	}

	@Override
	public Form<ForeignerName> createForm() {
		Form<ForeignerName> form = new Form<>(2);
		form.line(ForeignerName.$.firstName, ForeignerName.$.name);
		return form;
	}
}
