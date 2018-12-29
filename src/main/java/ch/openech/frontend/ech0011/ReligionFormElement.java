package ch.openech.frontend.ech0011;

import java.time.format.DateTimeParseException;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.DateUtils;

import ch.ech.ech0011.ReligionData;

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
	
			if (data.religionValidFrom != null) {
				s.append(" (Ab ");
				s.append(DateUtils.format(data.religionValidFrom));
				s.append(")");
			}
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
			int index = text.indexOf("(Ab ");
			object.religion = text;
			if (index > 0) {
				object.religion = text.substring(0, index).trim();
				int endIndex = text.indexOf(')', index);
				if (endIndex > 0) {
					try {
						String dateString = text.substring(index + 4, endIndex);
						object.religionValidFrom = DateUtils.parse(dateString);
					} catch (DateTimeParseException x) {
						object.religionValidFrom = InvalidValues.createInvalidLocalDate(text);
					}
				} else {
					object.religionValidFrom = InvalidValues.createInvalidLocalDate(text);
				}
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
