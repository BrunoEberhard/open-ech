package ch.openech.frontend.ech0011;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.ech.ech0021.PlaceOfOriginAddon;

public class PlaceOfOriginAddonFormElement extends ListFormElement<PlaceOfOriginAddon> {

	public PlaceOfOriginAddonFormElement(List<PlaceOfOriginAddon> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected CharSequence render(PlaceOfOriginAddon value) {
		StringBuilder s = new StringBuilder();
		s.append(value.origin.originName);
		RangeUtil.appendRange(s, value.naturalizationDate, value.expatriationDate);
		return s;
	}

	@Override
	protected Form<PlaceOfOriginAddon> createForm(boolean edit) {
		Form<PlaceOfOriginAddon> form = new Form<>(2);
		form.line(new PlaceOfOriginFormElement(PlaceOfOriginAddon.$.origin));
		form.line(PlaceOfOriginAddon.$.reasonOfAcquisition);
		form.line(PlaceOfOriginAddon.$.naturalizationDate, PlaceOfOriginAddon.$.expatriationDate);
		return form;
	}

}
