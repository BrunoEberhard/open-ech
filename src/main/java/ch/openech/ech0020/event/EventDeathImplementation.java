package ch.openech.ech0020.event;

import org.minimalj.frontend.form.Form;

import ch.ech.ech0011.DeathData;
import ch.ech.ech0020.v3.EventDeath;
import ch.openech.frontend.ech0011.GeneralPlaceFormElement;

public class EventDeathImplementation extends AbstractPersonEventImplementation<EventDeath, DeathData> {

	@Override
	public Form<EventDeath> createForm() {
		Form<EventDeath> form = new Form<>(2);
		form.line(EventDeath.$.deathData.deathPeriod.dateFrom);
		form.line(new GeneralPlaceFormElement(EventDeath.$.deathData.placeOfDeath));
		return form;
	}

}
