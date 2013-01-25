package ch.openech.client.e44;

import java.util.List;

import ch.openech.client.page.SearchPersonPage;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.server.EchServer;

public class PersonIdentificationField extends ObjectFlowField<PersonIdentification> {

	public PersonIdentificationField(PropertyInterface property) {
		super(property);
	}
	
	@Override
	public IForm<PersonIdentification> createFormPanel() {
		return new PersonIdentificationPanel();
	}

	@Override
	protected void show(PersonIdentification object) {
		addHtml(object.toHtml());
	}
	
	@Override
	protected void showActions() {
		if (isEmpty()) {
			addAction(new RemoveObjectAction());
		}
        addAction(new PersonSearchAction());
        addAction(new ObjectFieldEditor());
	}

	public final class PersonSearchAction extends SearchDialogAction<Person> {
		
		public PersonSearchAction() {
			super(SearchPersonPage.FIELD_NAMES);
		}

		@Override
		protected List<Person> search(String text) {		
			List<Person> resultList = EchServer.getInstance().getPersistence().person().find(text);
			return resultList;
		}
		
		@Override
		protected void save(Person object) {
			setObject(object.personIdentification);
		}
	}
	
}
