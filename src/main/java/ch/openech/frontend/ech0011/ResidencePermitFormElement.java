package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import ch.ech.ech0011.ResidencePermitData;
import ch.ech.ech0046.DateRange;

public class ResidencePermitFormElement extends FormLookupFormElement<ResidencePermitData> implements LookupParser {

	private static final String EINGANG = "Eingang: ";

	private ResidencePermitData data;

	public ResidencePermitFormElement(ResidencePermitData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return 50;
	}

	@Override
	public String getCaption() {
		return "Gültigkeit";
	}

	@Override
	protected String render(ResidencePermitData data) {
		this.data = data;
		if (data != null) {
			StringBuilder s = new StringBuilder();

			if (data.entryDate != null && !InvalidValues.isInvalid(data.entryDate)) {
				s.append(EINGANG + DateUtils.format(data.entryDate));
			}

			RangeUtil.appendRange(s, data.residencePermitValidFrom, data.residencePermitValidTill);
			return s.toString();
		} else {
			return null;
		}
	}

	@Override
	public ResidencePermitData parse(String text) {
		if (!StringUtils.isEmpty(text)) {
			if (data == null) {
				data = new ResidencePermitData();
			} else {
				data.residencePermitValidFrom = null;
				data.residencePermitValidTill = null;
				data.entryDate = null;
			}
			text = text.trim();
			int index = text.indexOf('(');
			String permitString = text;
			if (index >= 0) {
				permitString = text.substring(0, index).trim();
				DateRange range = RangeUtil.parseDateRange(text.substring(index));
				data.residencePermitValidFrom = range.dateFrom;
				data.residencePermitValidTill = range.dateTo;
			}
			int index2 = permitString.indexOf(EINGANG);
			if (index2 >= 0 && index2 < permitString.length() - EINGANG.length()) {
				String entryDateString = permitString.substring(index2 + EINGANG.length());
				data.entryDate = DateUtils.parse(entryDateString);
			}
			if (data.residencePermitValidFrom == null && data.residencePermitValidTill == null
					&& data.entryDate == null) {
				data.residencePermitValidFrom = InvalidValues.createInvalidLocalDate(text);
			}
		}
		return data;
	}

	@Override
	public Form<ResidencePermitData> createForm() {
		Form<ResidencePermitData> form = new Form<>(2);
		form.line(ResidencePermitData.$.residencePermitCategory);
		form.line(ResidencePermitData.$.getResidencePermitRulingSelection());
		form.line(ResidencePermitData.$.getResidencePermitSubCategorySelection());
		form.line(ResidencePermitData.$.residencePermitValidFrom, ResidencePermitData.$.residencePermitValidTill);
		form.line(ResidencePermitData.$.entryDate);
		return form;
	}

}
