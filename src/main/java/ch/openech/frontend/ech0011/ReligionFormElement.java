package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.ech.ech0011.ReligionData;
import ch.ech.ech0046.DateRange;

public class ReligionFormElement extends FormLookupFormElement<ReligionData> implements LookupParser {

	public ReligionFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	public ReligionFormElement(ReligionData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return 50;
	}

	@Override
	protected String render(ReligionData data) {
		if (data != null && data.religion != null) {
			StringBuilder s = new StringBuilder();
			s.append(data.religion);
			RangeUtil.appendRange(s, data.religionValidFrom, null);
			return s.toString();
		} else {
			return null;
		}
	}

	@Override
	public ReligionData parse(String text) {
		ReligionData object = new ReligionData();
		if (text != null) {
			text = text.trim();
			int index = text.indexOf("(");
			object.religion = text;
			if (index > 0) {
				object.religion = text.substring(0, index).trim();

				DateRange range = RangeUtil.parseDateRange(text.substring(index));
				object.religionValidFrom = range.dateFrom;
			}
		}
		return object;
	}

	@Override
	public Form<ReligionData> createForm() {
		Form<ReligionData> form = new Form<>();
		form.line(ReligionData.$.religion);
		form.line(ReligionData.$.religionValidFrom);
		return form;
	}

}
