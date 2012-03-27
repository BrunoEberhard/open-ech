package ch.openech.client.e11;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import ch.openech.client.e10.AddressPanel;
import ch.openech.client.e44.PersonIdentificationPanel;
import ch.openech.client.ewk.PersonViewPage;
import ch.openech.client.ewk.SearchPersonPage;
import ch.openech.dm.common.Address;
import ch.openech.dm.person.ContactPerson;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;

public class ContactPersonField extends MultiLineObjectField<ContactPerson> {
	
	private final PageContext pageContext;
	private final EchNamespaceContext echNamespaceContext;
	
	public ContactPersonField(Object key, EchNamespaceContext echNamespaceContext, boolean editable) {
		this(key, null, echNamespaceContext, editable);
	}

	public ContactPersonField(Object key, PageContext pageContext, EchNamespaceContext echNamespaceContext, boolean editable) {
		super(key, editable);
		this.pageContext = pageContext;
		this.echNamespaceContext = echNamespaceContext;
	}
	
	@Override
	protected void display(ContactPerson contactPerson) {
		if (contactPerson.person != null) {
			addObject("Kontaktperson");
			addHtml(contactPerson.person.toHtml());
			if (isEditable()) {
				addAction(new RemovePersonContactAction());
			}
			addGap();
		}
		if (contactPerson.address != null) {
			addObject("Kontaktadresse");
			addObject(contactPerson.address.toHtml());
			if (isEditable()) {
				addAction(new RemoveAddressContactAction());
			}		
			addGap();
		}
		if (!StringUtils.isBlank(contactPerson.validTill)) {
			addObject("Gültig bis");
			DateUtils.formatCH(contactPerson.validTill);
			addGap();
		}
		if (isEditable()) {
			addAction(new SelectPersonContactEditor());
			addAction(new EnterPersonContactEditor());
			addGap();
			
	        addAction(new AddAddressContactEditor(true), "AddAddressPerson");
	        addAction(new AddAddressContactEditor(false), "AddAddressOrganisation");
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
						if (person.isMale()) contactPerson.address.mrMrs = "2";
						if (person.isFemale()) contactPerson.address.mrMrs = "1";
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
		public FormVisual<PersonIdentification> createForm() {
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
		public FormVisual<Address> createForm() {
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

	private class ContactMouseListener extends MouseAdapter {
	    @Override
		public void mouseClicked(MouseEvent e) {
	    	PersonIdentification contactPerson = getObject().person;
	    	if (contactPerson != null) {
    			pageContext.show(Page.link(PersonViewPage.class, echNamespaceContext.getVersion(), contactPerson.getId()));
	    	}
	    }
	}

	@Override
	public FormVisual<ContactPerson> createFormPanel() {
		// not used
		return null;
	}

}
