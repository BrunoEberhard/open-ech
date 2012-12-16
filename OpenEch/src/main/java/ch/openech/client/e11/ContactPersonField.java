package ch.openech.client.e11;

import java.awt.event.ActionEvent;
import java.util.List;

import page.PersonViewPage;
import page.SearchPersonPage;
import ch.openech.client.e10.AddressPanel;
import ch.openech.client.e44.PersonIdentificationPanel;
import ch.openech.dm.common.Address;
import ch.openech.dm.person.ContactPerson;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.types.MrMrs;
import ch.openech.mj.db.model.PropertyInterface;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.PageContextHelper;
import ch.openech.mj.resources.ResourceAction;
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
			addObject("Kontaktperson");
			addHtml(contactPerson.person.toHtml());
			if (isEditable()) {
				addAction(new RemovePersonContactAction());
			} else {
				addAction(new PersonContactViewAction(contactPerson.person));
			}
			addGap();
		}
		if (contactPerson.address != null) {
			addObject("Kontaktadresse");
			addHtml(contactPerson.address.toHtml());
			if (isEditable()) {
				addAction(new RemoveAddressContactAction());
			}		
			addGap();
		}
		if (contactPerson.validTill != null) {
			addObject("Gültig bis");
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
	
	private class PersonContactViewAction extends ResourceAction {
		private final PersonIdentification person;
		
		private PersonContactViewAction(PersonIdentification person) {
			this.person = person;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			PageContext context = PageContextHelper.findContext(e.getSource());
			context.show(Page.link(PersonViewPage.class, echSchema.getVersion(), person.getId()));
		}
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

		@Override
		protected List<Person> search(String text) {		
			List<Person> resultList = EchServer.getInstance().getPersistence().person().find(text);
			return resultList;
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
		public void actionPerformed(ActionEvent e) {
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
		public void actionPerformed(ActionEvent e) {
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
