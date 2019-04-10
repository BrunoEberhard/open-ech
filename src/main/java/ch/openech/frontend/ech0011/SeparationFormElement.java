package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.EnumUtils;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;

import ch.ech.ech0011.Separation;
import ch.ech.ech0011.SeparationData;
import ch.ech.ech0046.DateRange;

public class SeparationFormElement extends FormLookupFormElement<SeparationData> implements LookupParser {

	public SeparationFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	public SeparationFormElement(SeparationData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return 50;
	}

	@Override
	public SeparationData parse(String text) {
		SeparationData object = new SeparationData();
		if (text != null) {
			text = text.trim();
			int index = text.indexOf('(');
			String separationString = text;
			if (index > 0) {
				separationString = text.substring(0, index).trim();
				DateRange range = RangeUtil.parseDateRange(text.substring(index));
				object.separationValidFrom = range.dateFrom;
				object.separationValidTill = range.dateTo;
			}
			for (Separation s : Separation.values()) {
				if (EnumUtils.getText(s).equals(separationString)) {
					object.separation = s;
					break;
				}
			}
			if (object.separation == null) {
				object.separation = InvalidValues.createInvalidEnum(Separation.class, separationString);
			}
		}
		return object;
	}

	@Override
	public Form<SeparationData> createForm() {
		Form<SeparationData> form = new Form<>(2);
		form.line(SeparationData.$.separation);
		form.line(SeparationData.$.separationValidFrom, SeparationData.$.separationValidTill);
		return form;
	}

}
