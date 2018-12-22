package ch.openech.frontend.ech0011;

import java.util.List;
import java.util.stream.Collectors;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.Frontend.TableActionListener;
import org.minimalj.frontend.editor.SearchDialog;
import org.minimalj.frontend.form.element.AbstractLookupFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.repository.query.By;

import ch.ech.ech0011.ContactData.ContactReference;
import ch.ech.ech0011.Person;
import ch.ech.ech0098.Organisation;

public class ContactReferenceFormElement extends AbstractLookupFormElement<ContactReference>
		implements Search<ContactReferenceFormElement.PersonOrOrganisation> {

	private SearchDialog<PersonOrOrganisation> dialog;

	public ContactReferenceFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	@Override
	protected void lookup() {
		Object[] columns = new Object[] { PersonOrOrganisation.$.getText() };
		dialog = new SearchDialog<>(this, columns, false, new SearchDialogActionListener(), null);
		dialog.show();

	}

	public static class PersonOrOrganisation {
		public static final PersonOrOrganisation $ = Keys.of(PersonOrOrganisation.class);

		private final Object object;

		public PersonOrOrganisation() {
			// only for keys
			this.object = null;
		}

		public PersonOrOrganisation(Object object) {
			this.object = object;
		}

		public Object getObject() {
			return object;
		}

		public String getText() {
			if (Keys.isKeyObject(this))
				return Keys.methodOf(this, "text");

			if (object instanceof Person) {
				return ((Person) object).nameData.officialName;
			} else if (object instanceof Organisation) {
				return ((Organisation) object).organisationIdentification.organisationName;
			} else {
				return "-";
			}
		}
	}

	private class SearchDialogActionListener implements TableActionListener<PersonOrOrganisation> {
		@Override
		public void action(PersonOrOrganisation personOrOrganisation) {
			ContactReference contactReference = ContactReferenceFormElement.this.getValue();
			if (personOrOrganisation.getObject() instanceof Person) {
				contactReference.setPerson(((Person) personOrOrganisation.getObject()));
			} else if (personOrOrganisation.getObject() instanceof Organisation) {
				contactReference.setOrganisation(((Organisation) personOrOrganisation.getObject()));
			} else {
				throw new IllegalStateException();
			}
			ContactReferenceFormElement.this.setValueInternal(contactReference);
			dialog.closeDialog();
		}
	}

	@Override
	public List<PersonOrOrganisation> search(String query) {
		List<Person> persons = Backend.find(Person.class, By.search(query));
		return persons.stream().map(p -> new PersonOrOrganisation(p)).collect(Collectors.toList());
	}

}
