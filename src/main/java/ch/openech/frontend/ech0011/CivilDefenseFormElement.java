package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.resources.Resources;

import ch.ech.ech0021.CivilDefenseData;
import ch.openech.frontend.YesNoFormElement;
import ch.openech.xml.YesNo;

public class CivilDefenseFormElement extends FormLookupFormElement<CivilDefenseData> implements LookupParser {

	public CivilDefenseFormElement(CivilDefenseData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return 50;
	}

	@Override
	public CivilDefenseData parse(String text) {
		CivilDefenseData object = new CivilDefenseData();
		if (text != null) {
			text = text.trim();
			int index = text.indexOf('(');
			if (index > 0) {
				object.civilDefenseValidFrom = RangeUtil.parseValidFrom(text.substring(index));
				text = text.substring(0, index).trim();
			}
			if (text.contains(Resources.getString("Dienstpflicht._0"))) {
				object.civilDefense = YesNo._0;
			} else if (text.contains(Resources.getString("Dienstpflicht._1"))) {
				object.civilDefense = YesNo._1;
			}
			if (object.civilDefense == null) {
				object.civilDefense = InvalidValues.createInvalidEnum(YesNo.class, text);
			}
		}
		return object;
	}

	@Override
	protected String render(CivilDefenseData value) {
		if (value != null) {
			return ArmedForcesFormElement.render(value.civilDefense, null, value.civilDefenseValidFrom);
		} else {
			return null;
		}
	}

	@Override
	public Form<CivilDefenseData> createForm() {
		Form<CivilDefenseData> form = new Form<>(2);
		YesNoFormElement yesNoFormElement = new YesNoFormElement(CivilDefenseData.$.civilDefense, "Dienstpflicht",
				true);
		form.line(yesNoFormElement);
		form.line(CivilDefenseData.$.civilDefenseValidFrom);
		return form;
	}

}
