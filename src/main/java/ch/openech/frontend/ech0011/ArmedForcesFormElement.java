package ch.openech.frontend.ech0011;

import java.time.LocalDate;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.resources.Resources;

import ch.ech.ech0021.ArmedForcesData;
import ch.openech.frontend.YesNoFormElement;
import ch.openech.model.YesNo;

public class ArmedForcesFormElement
		extends FormLookupFormElement<ArmedForcesData> implements LookupParser {

	public ArmedForcesFormElement(ArmedForcesData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return 70;
	}

	@Override
	public ArmedForcesData parse(String text) {
		ArmedForcesData object = new ArmedForcesData();
		if (text != null) {
			text = text.trim();
			int index = text.indexOf('(');
			if (index > 0) {
				object.armedForcesValidFrom = RangeUtil.parseValidFrom(text.substring(index));
				text = text.substring(0, index).trim();
			}
			if (text.contains(Resources.getString("Dienstpflicht._0"))) {
				object.armedForcesService = YesNo._0;
			} else if (text.contains(Resources.getString("Dienstpflicht._1"))) {
				object.armedForcesService = YesNo._1;
			}
			if (text.contains(Resources.getString("Ersatzpflicht._0"))) {
				object.armedForcesLiability = YesNo._0;
			} else if (text.contains(Resources.getString("Ersatzpflicht._1"))) {
				object.armedForcesLiability = YesNo._1;
			}
			if (object.armedForcesService == null && object.armedForcesLiability == null) {
				object.armedForcesService = InvalidValues.createInvalidEnum(YesNo.class, text);
			}
		}
		return object;
	}

	@Override
	protected String render(ArmedForcesData value) {
		if (value != null) {
			return render(value.armedForcesService, value.armedForcesLiability, value.armedForcesValidFrom);
		} else {
			return null;
		}
	}

	public static String render(YesNo service, YesNo liability, LocalDate validFrom) {
		boolean serviceAvailable = service != null;
		boolean liabilityAvailable = liability != null;
		StringBuilder s = new StringBuilder();
		if (serviceAvailable || liabilityAvailable) {
			if (serviceAvailable) {
				s.append(Resources.getString("Dienstpflicht." + service.name()));
			}
			if (liabilityAvailable) {
				if (serviceAvailable) {
					s.append(", ");
				}
				s.append(Resources.getString("Ersatzpflicht." + liability.name()));
			}
			RangeUtil.appendValidFrom(s, validFrom);
		}
		return s.toString();

	}

	@Override
	public Form<ArmedForcesData> createForm() {
		Form<ArmedForcesData> form = new Form<>(2);
		YesNoFormElement yesNoFormElement = new YesNoFormElement(
				ArmedForcesData.$.armedForcesService, "Dienstpflicht", true);
		form.line(yesNoFormElement);
		yesNoFormElement = new YesNoFormElement(ArmedForcesData.$.armedForcesLiability, "Ersatzpflicht", true);
		form.line(yesNoFormElement);
		form.line(ArmedForcesData.$.armedForcesValidFrom);
		return form;
	}

}
