package ch.openech.client.e11;

import ch.openech.client.e10.AddressPanel;
import ch.openech.client.e44.PersonIdentificationPanel;
import ch.openech.client.page.PersonViewPage;
import ch.openech.client.page.SearchPersonPage;
import ch.openech.dm.common.Address;
import ch.openech.dm.person.ContactPerson;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.types.MrMrs;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.search.FulltextIndexSearch;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;
import ch.openech.mj.util.DateUtils;
import ch.openech.server.EchServer;
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
				addLink("Person anzeigen", PageLink.link(PersonViewPage.class, echSchema.getVersion(), contactPerson.person.getId()));
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
			DateUtils.formatCH(contactPerson.validTill);
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
			super(getComponent(), new FulltextIndexSearch<>(Person.class, EchServer.getInstance().getPersistence().personIndex(), SearchPersonPage.FIELD_NAMES));
		}
		
		@Override
		protected void save(Person person) {
			if (person != null) {
				ContactPerson contactPerson = ContactPersonField.this.getObject();
				
				contactPerson.person = person.personIdentification;
				if (person.dwellingAddress != null) {
					contactPerson.address = person.dwellingAddress.mailAddress;
					if (contactPerson.address != null) {
						contactPerson.address.lastName = person.personIdentification.officialName;
						contactPerson.address.firstName = person.personIdentification.firstName;
						if (person.isMale()) contactPerson.address.mrMrs = MrMrs.Frau;
						if (person.isFemale()) contactPerson.address.mrMrs = MrMrs.Herr;
					}
				}
				fireObjectChange();
			}
		}

	};
    
    // Identifikationen der Kontaktpersonen frei erfassen
	public class EnterPersonContactEditor extends ObjectFieldPartEditor<PersonIdentification> {
		@Override
		public IForm<PersonIdentification> createForm() {
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
		public void action(IComponent context) {
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
		public IForm<Address> createForm() {
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
		public void action(IComponent pageContext) {
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
	public IForm<ContactPerson> createFormPanel() {
		// not used
		return null;
	}

}
