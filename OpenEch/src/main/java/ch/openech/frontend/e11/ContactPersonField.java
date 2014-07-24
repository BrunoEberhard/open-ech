package ch.openech.frontend.e11;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.SearchDialogAction;
import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.frontend.toolkit.ResourceAction;
import org.minimalj.model.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.DateUtils;

import ch.openech.frontend.e10.AddressPanel;
import ch.openech.frontend.e44.PersonIdentificationPanel;
import ch.openech.frontend.page.PersonViewPage;
import ch.openech.frontend.page.SearchPersonPage;
import ch.openech.model.common.Address;
import ch.openech.model.person.ContactPerson;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;
import ch.openech.model.types.MrMrs;
import ch.openech.xml.write.EchSchema;

public class ContactPersonField extends ObjectFlowField<ContactPerson> {
	
	private final EchSchema echSchema;
	
	public ContactPersonField(PropertyInterface property) {
		this(property, null, true);
	}

	public ContactPersonField(PropertyInterface property, EchSchema echSchema, boolean editable) {
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
				addLink("Person anzeigen", PageLink.link(PersonViewPage.class, echSchema.getVersion(), contactPerson.person.idAsString()));
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
	public class SelectPersonContactEditor extends SearchDialogAction<Person> {
		
		public SelectPersonContactEditor() {
			super(SearchPersonPage.FIELD_NAMES);
		}
		
		@Override
		protected void save(Person person) {
			if (person != null) {
				ContactPerson contactPerson = ContactPersonField.this.getObject();
				
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
		public List<Person> search(String searchText) {
			return Backend.getInstance().read(Person.class, Criteria.search(searchText), 100);
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
	private class RemovePersonContactAction extends ResourceAction {
		@Override
		public void action() {
			getObject().person = null;
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
	private class RemoveAddressContactAction extends ResourceAction {
		@Override
		public void action() {
			getObject().address = null;
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
