package ch.openech.frontend.e11;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.DateUtils;

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

public class ContactPersonFormElement extends ObjectPanelFormElement<ContactPerson> {
	
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
			addText("Kontaktperson");
			addText(contactPerson.person.toHtml());
			if (isEditable()) {
				addAction(new RemovePersonContactAction());
			} else {
				addAction(new PersonPage(echSchema, contactPerson.person.id), "Person anzeigen");
			}
			addGap();
		}
		if (contactPerson.address != null) {
			addText("Kontaktadresse");
			addText(contactPerson.address.toHtml());
			if (isEditable()) {
				addAction(new RemoveAddressContactAction());
			}		
			addGap();
		}
		if (contactPerson.validTill != null) {
			addText("Gültig bis");
			DateUtils.format(contactPerson.validTill);
			addGap();
		}
	}

	@Override
	protected void showActions() {
		addAction(new SelectPersonContactEditor());
		addAction(new EnterPersonContactEditor());
		addGap();
		
        addAction(new AddAddressContactEditor(true), "AddAddressPerson");
        addAction(new AddAddressContactEditor(false), "AddAddressOrganisation");
	}

	// Person suchen
	public class SelectPersonContactEditor extends SearchDialogAction<PersonSearch> {
		
		public SelectPersonContactEditor() {
			super(PersonSearchPage.FIELD_NAMES);
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
				fireObjectChange();
			}
		}
		
		@Override
		public List<PersonSearch> search(String query) {
			return Backend.getInstance().read(PersonSearch.class, Criteria.search(query), 100);
		}

	};
    
    // Identifikationen der Kontaktpersonen frei erfassen
	public class EnterPersonContactEditor extends ObjectFieldPartEditor<PersonIdentification> {
		@Override
		public Form<PersonIdentification> createForm() {
			return new PersonIdentificationPanel();
		}

		@Override
		protected PersonIdentification getPart(ContactPerson object) {
			return object.person;
		}

		@Override
		protected void setPart(ContactPerson object, PersonIdentification personIdentification) {
			object.person = personIdentification;
		}
    };

    // Kontaktperson entfernen
	private class RemovePersonContactAction extends Action {
		@Override
		public void action() {
			getValue().person = null;
			fireObjectChange();
		}
    };

    // Kontaktadresse bearbeiten (Person oder Adresse)
	public class AddAddressContactEditor extends ObjectFieldPartEditor<Address> {
		private final boolean person;
		
		public AddAddressContactEditor(boolean person) {
			this.person = person;
		}
		
		@Override
		public Form<Address> createForm() {
			return new AddressPanel(false, person, !person);
		}

		@Override
		protected Address getPart(ContactPerson object) {
			return object.address;
		}

		@Override
		protected void setPart(ContactPerson object, Address address) {
			object.address = address;
		}
    };

    // Kontaktadresse entfernen
	private class RemoveAddressContactAction extends Action {
		@Override
		public void action() {
			getValue().address = null;
			fireObjectChange();
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
