package ch.openech.frontend.e11;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.frontend.page.PageAction;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.DateUtils;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.e10.AddressPanel;
import ch.openech.frontend.e44.PersonIdentificationPanel;
import ch.openech.frontend.page.PersonPage;
import ch.openech.frontend.page.PersonSearchPage;
import ch.openech.model.common.Address;
import ch.openech.model.person.ContactPerson;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;
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
		if (contactPerson.person != null) {
			add("Kontaktperson");
			if (isEditable()) {
				add(contactPerson.person, new RemovePersonContactAction());
			} else {
				add(contactPerson.person, new PageAction(new PersonPage(echSchema, contactPerson.person.id), "Person anzeigen"));
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
			new EnterPersonContactEditor(),
			// TODO gap
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
				Person person = Backend.getInstance().read(Person.class, personSearch.id);
				
				ContactPerson contactPerson = ContactPersonFormElement.this.getValue();
				
				contactPerson.person = person.personIdentification();
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
			return Backend.getInstance().read(PersonSearch.class, Criteria.search(query), 100);
		}

	};
    
    // Identifikationen der Kontaktpersonen frei erfassen
	public class EnterPersonContactEditor extends Editor<PersonIdentification, Void> {
		@Override
		public Form<PersonIdentification> createForm() {
			return new PersonIdentificationPanel();
		}

		@Override
		protected PersonIdentification createObject() {
			return getValue().person;
		}

		@Override
		protected Void save(PersonIdentification personIdentification) {
			getValue().person = personIdentification;
			return null;
		}

		@Override
		protected void finished(Void result) {
			handleChange();
		}
    };

    // Kontaktperson entfernen
	private class RemovePersonContactAction extends Action {
		@Override
		public void action() {
			getValue().person = null;
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
	public Form<ContactPerson> createFormPanel() {
		// not used
		return null;
	}

}
