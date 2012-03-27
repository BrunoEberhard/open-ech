package ch.openech.client.e46;

import java.awt.event.ActionEvent;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;

public class ContactField extends MultiLineObjectField<Contact> {
	
	public ContactField(Object key) {
		this(key, true);
	}
	
	public ContactField(Object key, boolean editable) {
		super(key, editable);
	}
	
	@Override
	public void setObject(Contact object) {
		super.setObject(object != null ? object : new Contact());
	}

	public class AddContactEntryEditor extends ObjectFieldPartEditor<ContactEntry> {
		private final String type;
		private final boolean person;
		
		public AddContactEntryEditor(boolean person) {
			this.type = "A";
			this.person = person;
		}

		public AddContactEntryEditor(String type) {
			this.type = type;
			this.person = true;
		}
		
		@Override
		public FormVisual<ContactEntry> createForm() {
			return new ContactEntryPanel(type, person);
		}

		@Override
		protected ContactEntry getPart(Contact contact) {
			ContactEntry contactEntry = new ContactEntry();
			contactEntry.typeOfContact = type;
			if ("I".equals(type)) {
				contactEntry.value = "http://";
			}
			return contactEntry;
		}

		@Override
		protected void setPart(Contact contact, ContactEntry p) {
			contact.entries.add(p);
		}
    };

	private class RemoveContactEntryAction extends ResourceAction {
		private final ContactEntry contactEntry;
		
		private RemoveContactEntryAction(ContactEntry contactEntry) {
			this.contactEntry = contactEntry;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			getObject().entries.remove(contactEntry);
			fireObjectChange();
		}
    };
	
	//
	
	@Override
	public FormVisual<Contact> createFormPanel() {
		// unused
		return null;
	}

	@Override
	protected void display(Contact contact) {
		if (contact != null) {
			for (ContactEntry contactEntry : contact.entries) {
				addHtml(contactEntry.toHtml());
				addAction(new RemoveContactEntryAction(contactEntry));
				addGap();
			}
		}
		addAction(new AddContactEntryEditor(true), "AddAddressPerson");
		addAction(new AddContactEntryEditor(false), "AddAddressOrganisation");
		addAction(new AddContactEntryEditor("E"), "AddEmail");
		addAction(new AddContactEntryEditor("P"), "AddPhone");
		addAction(new AddContactEntryEditor("I"), "AddInternet");
	}

}
