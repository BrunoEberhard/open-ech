package ch.openech.frontend.e11;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.frontend.page.PageAction;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.repository.query.By;
import org.minimalj.util.DateUtils;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.e10.AddressPanel;
import ch.openech.frontend.e44.PersonIdentificationLightPanel;
import ch.openech.frontend.page.OrganisationPage;
import ch.openech.frontend.page.PersonPage;
import ch.openech.frontend.page.PersonSearchPage;
import ch.openech.model.common.Address;
import ch.openech.model.person.ContactPerson;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentificationLight;
import ch.openech.model.person.PersonSearch;
import ch.openech.model.types.MrMrs;
import ch.openech.xml.write.EchSchema;

public class ContactPersonFormElement extends ObjectFormElement<ContactPerson> {
	
	private final EchSchema echSchema;
	
	public ContactPersonFormElement(PropertyInterface property) {
		this(property, null, true);
	}

	public ContactPersonFormElement(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}
	
	@Override
	protected void show(ContactPerson contactPerson) {
		if (contactPerson.partner.personIdentification != null) {
			add("Kontaktperson");
			if (isEditable()) {
				add(contactPerson.partner.personIdentification, new RemovePersonContactAction());
			} else {
				add(contactPerson.partner.personIdentification, new PageAction(new PersonPage(echSchema, contactPerson.partner.personIdentification.id), "Person anzeigen"));
				add("Person anzeigen", new PersonPage(echSchema, contactPerson.partner.personIdentification.id));
			}
		} else if (contactPerson.partner.personIdentificationLight != null) {
			if (isEditable()) {
				add(contactPerson.partner.personIdentificationLight, new RemovePersonContactAction());
			} else {
				add(contactPerson.partner.personIdentificationLight);
			}
		} else if (contactPerson.partner.organisation != null) {
			if (isEditable()) {
				add(contactPerson.partner.organisation.organisationName, new RemovePersonContactAction());
			} else {
				add(contactPerson.partner.organisation.organisationName);
				add("Firma anzeigen", new OrganisationPage(echSchema, contactPerson.partner.organisation.id));
			}
		}
		
		if (contactPerson.address != null) {
			add("Kontaktadresse");
			if (isEditable()) {
				add(contactPerson.address, new RemoveAddressContactAction());
			} else {
				add(contactPerson.address);
			}
		}
		if (contactPerson.validTill != null) {
			add("Gültig bis");
			add(DateUtils.format(contactPerson.validTill));
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] {
			new SelectPersonContactEditor(),
			new PersonIdentificationLightEditor(),
			new AddAddressContactEditor(true),
			new AddAddressContactEditor(false),
		};
	}

	// Person suchen
	public class SelectPersonContactEditor extends SearchDialogAction<PersonSearch> {
		
		public SelectPersonContactEditor() {
			super(PersonSearchPage.FIELDS);
		}
		
		@Override
		protected void save(PersonSearch personSearch) {
			if (personSearch != null) {
				ContactPerson contactPerson = ContactPersonFormElement.this.getValue();

				Person person = Backend.read(Person.class, personSearch.id);
				ContactPersonFormElement.this.getValue().partner.personIdentification = person.personIdentification();
				
				contactPerson.partner.personIdentification = person.personIdentification();
				
				if (person.dwellingAddress != null) {
					contactPerson.address = person.dwellingAddress.mailAddress;
					if (contactPerson.address != null) {
						contactPerson.address.lastName = person.officialName;
						contactPerson.address.firstName = person.firstName;
						if (person.isMale()) contactPerson.address.mrMrs = MrMrs.Frau;
						if (person.isFemale()) contactPerson.address.mrMrs = MrMrs.Herr;
					}
				}
				handleChange();
			}
		}
		
		@Override
		public List<PersonSearch> search(String query) {
			return Backend.find(PersonSearch.class, By.search(query).limit(100));
		}

	};
    
    // Identifikationen der Kontaktpersonen frei erfassen
	public class PersonIdentificationLightEditor extends Editor<PersonIdentificationLight, Void> {
		@Override
		public Form<PersonIdentificationLight> createForm() {
			return new PersonIdentificationLightPanel();
		}

		@Override
		public PersonIdentificationLight createObject() {
			if (getValue().partner.personIdentificationLight != null) {
				return getValue().partner.personIdentificationLight;
			} else {
				return new PersonIdentificationLight();
			}
		}

		@Override
		public Void save(PersonIdentificationLight edited) {
			getValue().partner.personIdentificationLight = edited;
			return null;
		}
		
		@Override
		protected void finished(Void result) {
			handleChange();
		}
	}

    // Kontaktperson entfernen
	private class RemovePersonContactAction extends Action {
		@Override
		public void action() {
			getValue().partner.clear();
			handleChange();
		}
    };

    // Kontaktadresse bearbeiten (Person oder Adresse)
	public class AddAddressContactEditor extends Editor<Address, Void> {
		private final boolean person;
		
		public AddAddressContactEditor(boolean person) {
			super(Resources.getString(person ? "AddAddressPerson" : "AddAddressOrganisation"));
			this.person = person;
		}
		
		@Override
		public Form<Address> createForm() {
			return new AddressPanel(false, person, !person);
		}

		@Override
		protected Address createObject() {
			return getValue().address;
		}

		@Override
		protected Void save(Address address) {
			getValue().address = address;
			return null;
		}
		
		@Override
		protected void finished(Void result) {
			handleChange();
		}
    };

    // Kontaktadresse entfernen
	private class RemoveAddressContactAction extends Action {
		@Override
		public void action() {
			getValue().address = null;
			handleChange();
		}
	};
	
//	@Override
//	public void fillWithDemoData() {
//		Point p = new Point(textArea.getX() + textArea.getWidth() / 2, textArea.getY() + textArea.getHeight() / 2);
//		fieldMenu.showMenu(p);
//		JMenu menu = (JMenu)fieldMenu.getMenu().getComponent(1);
//		JMenuItem menuItem = menu.getItem(0);
//		menu.doClick();
//		menuItem.doClick();
//		fieldMenu.fillWithDemoData();
//	}

	@Override
	public Form<ContactPerson> createForm() {
		// not used
		return null;
	}

}
