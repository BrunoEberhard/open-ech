package ch.openech.frontend.ech0011;

import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.ech.ech0011.ReligionData;
import ch.ech.ech0046.DateRange;

public class ReligionDataFormElement extends FormLookupFormElement<ReligionData> implements LookupParser {

	public ReligionDataFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	public ReligionDataFormElement(ReligionData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return 50;
	}

	@Override
	protected Search<String> getSearch() {
		return new ReligionFormElement.ReligionSearch();
	}

	@Override
	protected String render(ReligionData data) {
		if (data != null && data.religion != null) {
			StringBuilder s = new StringBuilder();
			s.append(ReligionFormElement.renderReligion(data.religion));
			RangeUtil.appendRange(s, data.religionValidFrom, null);
			return s.toString();
		} else {
			return null;
		}
	}

	@Override
	public ReligionData parse(String text) {
		ReligionData data = new ReligionData();
		if (text != null) {
			text = text.trim();
			int index = text.indexOf("(");
			if (index > 0) {
				DateRange range = RangeUtil.parseDateRange(text.substring(index));
				data.religionValidFrom = range.dateFrom;

				text = text.substring(0, index).trim();
			}
			data.religion = ReligionFormElement.parseReligion(text);
		}
		return data;
	}

	@Override
	public Form<ReligionData> createForm() {
		Form<ReligionData> form = new Form<>();
		form.line(new ReligionFormElement(ReligionData.$.religion, Form.EDITABLE));
		form.line(ReligionData.$.religionValidFrom);
		return form;
	}

}
