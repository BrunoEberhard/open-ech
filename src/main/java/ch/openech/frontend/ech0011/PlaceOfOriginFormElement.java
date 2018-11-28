package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.ech.ech0011.PlaceOfOrigin;

public class PlaceOfOriginFormElement extends ListFormElement<PlaceOfOrigin> {

	public PlaceOfOriginFormElement(PlaceOfOrigin key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected Form<PlaceOfOrigin> createForm(boolean edit) {
		Form<PlaceOfOrigin> form = new Form<>();
		form.line(PlaceOfOrigin.$.originName);
		return form;
	}

}
