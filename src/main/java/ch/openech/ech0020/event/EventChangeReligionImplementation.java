package ch.openech.ech0020.event;

import org.minimalj.frontend.form.Form;

import ch.ech.ech0011.ReligionData;
import ch.ech.ech0020.v3.EventChangeReligion;
import ch.openech.frontend.ech0011.ReligionFormElement;

public class EventChangeReligionImplementation extends AbstractPersonEventImplementation<EventChangeReligion, ReligionData> {

	@Override
	public Form<EventChangeReligion> createForm() {
		Form<EventChangeReligion> form = new Form<>();
		form.line(new ReligionFormElement(EventChangeReligion.$.religionData.religion, Form.EDITABLE));
		form.line(EventChangeReligion.$.religionData.religionValidFrom);
		return form;
	}

}
