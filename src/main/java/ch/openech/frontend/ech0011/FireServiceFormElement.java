package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.resources.Resources;

import ch.ech.ech0021.FireServiceData;
import ch.openech.frontend.YesNoFormElement;
import ch.openech.xml.YesNo;

public class FireServiceFormElement
		extends FormLookupFormElement<FireServiceData> implements LookupParser {

	public FireServiceFormElement(FireServiceData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return 70;
	}

	@Override
	public FireServiceData parse(String text) {
		FireServiceData object = new FireServiceData();
		if (text != null) {
			text = text.trim();
			int index = text.indexOf('(');
			if (index > 0) {
				object.fireServiceValidFrom = RangeUtil.parseValidFrom(text.substring(index));
				text = text.substring(0, index).trim();
			}
			if (text.contains(Resources.getString("Dienstpflicht._0"))) {
				object.fireService = YesNo._0;
			} else if (text.contains(Resources.getString("Dienstpflicht._1"))) {
				object.fireService = YesNo._1;
			}
			if (text.contains(Resources.getString("Ersatzpflicht._0"))) {
				object.fireServiceLiability = YesNo._0;
			} else if (text.contains(Resources.getString("Ersatzpflicht._1"))) {
				object.fireServiceLiability = YesNo._1;
			}
			if (object.fireService == null && object.fireServiceLiability == null) {
				object.fireService = InvalidValues.createInvalidEnum(YesNo.class, text);
			}
		}
		return object;
	}

	@Override
	protected String render(FireServiceData value) {
		if (value != null) {
			return ArmedForcesFormElement.render(value.fireService, value.fireServiceLiability,
					value.fireServiceValidFrom);
		} else {
			return null;
		}
	}

	@Override
	public Form<FireServiceData> createForm() {
		Form<FireServiceData> form = new Form<>(2);
		YesNoFormElement yesNoFormElement = new YesNoFormElement(
				FireServiceData.$.fireService, "Dienstpflicht", true);
		form.line(yesNoFormElement);
		yesNoFormElement = new YesNoFormElement(FireServiceData.$.fireServiceLiability, "Ersatzpflicht", true);
		form.line(yesNoFormElement);
		form.line(FireServiceData.$.fireServiceValidFrom);
		return form;
	}

}
