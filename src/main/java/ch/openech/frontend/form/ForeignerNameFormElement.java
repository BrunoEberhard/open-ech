package ch.openech.frontend.form;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.StringUtils;

import ch.ech.ech0011.v8.ForeignerName;

public class ForeignerNameFormElement extends ObjectFormElement<ForeignerName> {

	public ForeignerNameFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	@Override
	protected void show(ForeignerName foreignerName) {
		StringBuilder s = new StringBuilder();
		if (!StringUtils.isEmpty(foreignerName.name)) {
			s.append(foreignerName.name);
		}
		if (!StringUtils.isEmpty(foreignerName.firstName)) {
			if (s.length() > 0) {
				s.append(", ");
			}
			s.append(foreignerName.firstName);
		}
		if (s.length() == 0) {
			add(new NewObjectFormElementEditor());
		} else {
			add(new ObjectFormElementEditor(s.toString()));
		}
	}

	@Override
	protected Form<ForeignerName> createForm() {
		Form<ForeignerName> form = new Form<>();
		form.line(ForeignerName.$.name);
		form.line(ForeignerName.$.firstName);
		return form;
	}
	
	@Override
	protected void display() {
		super.display();
		if (getValue() == null) {
			add(new NewObjectFormElementEditor());
		}
	}

}
