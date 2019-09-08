package ch.openech.frontend.ech0011;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.ObjectPage;

import ch.ech.ech0011.Person;
import ch.openech.ech0020.event.EventChangeReligionImplementation;
import ch.openech.ech0020.event.EventDeathImplementation;
import ch.openech.ech0020.event.PersonEventImplementation;
import ch.openech.transaction.EchTransaction;
import ch.openech.xml.EchWriter;

public class PersonPage extends ObjectPage<Person> {

	public PersonPage(Person object) {
		super(object);
	}

	@Override
	protected Form<Person> createForm() {
		return new PersonForm(false);
	}

	public List<Action> getActions() {
		ActionGroup actions = new ActionGroup("");
		actions.add(new PersonPageEditor<>(new EventChangeReligionImplementation()));
		actions.add(new PersonPageEditor<>(new EventDeathImplementation()));
		return actions.getItems();
	}

	private class PersonPageEditor<EDIT, IMPL extends PersonEventImplementation<EDIT>> extends ObjectPageEditor<EDIT> {
		private final IMPL impl;

		public PersonPageEditor(IMPL impl) {
			this.impl = impl;
		}

		@Override
		protected EDIT createObject() {
			return impl.createEvent((Person) PersonPage.this.getObject());
		}

		@Override
		protected Form<EDIT> createForm() {
			return impl.createForm();
		}

		@Override
		protected Person save(EDIT object) {
			String input = EchWriter.serialize(impl.createDelivery(object));
			return Backend.execute(new EchTransaction(input));
		}
	}

}
