package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.repository.criteria.By;

import ch.openech.frontend.page.PersonSearchPage;
import ch.openech.model.person.PartnerIdentification;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentificationLight;
import ch.openech.model.person.PersonSearch;

public class PartnerIdentificationFormElement extends ObjectFormElement<PartnerIdentification> {

	public PartnerIdentificationFormElement(PropertyInterface property) {
		super(property);
	}
	
	@Override
	protected void show(PartnerIdentification partnerIdentification) {
		if (!partnerIdentification.isEmpty()) {
			add(partnerIdentification, new ClearPartnerIdentificationObjectAction());
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new PersonSearchAction(), new PersonIdentificationLightEditor() };
	}
	
	public class PersonIdentificationLightEditor extends Editor<PersonIdentificationLight, Void> {
		@Override
		public Form<PersonIdentificationLight> createForm() {
			return new PersonIdentificationLightPanel();
		}

		@Override
		public PersonIdentificationLight createObject() {
			if (getValue().personIdentificationLight != null) {
				return getValue().personIdentificationLight;
			} else {
				return new PersonIdentificationLight();
			}
		}

		@Override
		public Void save(PersonIdentificationLight edited) {
			getValue().personIdentificationLight = edited;
			return null;
		}
		
		@Override
		protected void finished(Void result) {
			handleChange();
		}
	}

	public class PersonSearchAction extends SearchDialogAction<PersonSearch> {
		
		public PersonSearchAction() {
			super(PersonSearchPage.FIELDS);
		}

		@Override
		protected void save(PersonSearch object) {
			Person person = Backend.read(Person.class, object.id);
			PartnerIdentificationFormElement.this.getValue().personIdentification = person.personIdentification();
			handleChange();
		}

		@Override
		public List<PersonSearch> search(String searchText) {
			return Backend.read(PersonSearch.class, By.search(searchText), 100);
		}
	}
	
	public class ClearPartnerIdentificationObjectAction extends Action {
		@Override
		public void action() {
			PartnerIdentificationFormElement.this.getValue().clear();
			handleChange();
		}
	}

	@Override
	protected Form<PartnerIdentification> createForm() {
		// not used directly
		return null;
	}
}
