package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.EnumUtils;
import org.minimalj.util.DateUtils;

import ch.ech.ech0011.MaritalData;

public class MaritalDataFormElement extends FormLookupFormElement<MaritalData> {

	public MaritalDataFormElement(MaritalData key, boolean editable) {
		super(key, editable);
	}

	@Override
	protected Form<MaritalData> createForm() {
		Form<MaritalData> form = new Form<>(2);
		form.line(MaritalData.$.maritalStatus, MaritalData.$.dateOfMaritalStatus);
		form.line(new GeneralPlaceFormElement(MaritalData.$.placeOfMarriage), MaritalData.$.officialProofOfMaritalStatusYesNo);
		form.line(MaritalData.$.cancelationReason);

		return form;
	}

	@Override
	protected String render(MaritalData data) {
		StringBuilder s = new StringBuilder();
		if (data != null) {
			if (data.maritalStatus != null) {
				s.append(EnumUtils.getText(data.maritalStatus));
			}
			if (data.dateOfMaritalStatus != null) {
				s.append(' ').append(DateUtils.format(data.dateOfMaritalStatus));
			}
		}
		return s.toString();
	}

}
