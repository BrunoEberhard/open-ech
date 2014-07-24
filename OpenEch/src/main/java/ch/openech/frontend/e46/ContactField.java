package ch.openech.frontend.e46;

import java.util.List;

import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.toolkit.ResourceAction;
import org.minimalj.model.PropertyInterface;

import ch.openech.model.contact.ContactEntry;
import ch.openech.model.contact.ContactEntryType;

public class ContactField extends ObjectFlowField<List<ContactEntry>> {
	
	public ContactField(PropertyInterface property) {
		this(property, true);
	}
	
	public ContactField(PropertyInterface property, boolean editable) {
		super(property, editable);
	}
	
	public class AddContactEntryEditor extends ObjectFieldPartEditor<ContactEntry> {
		private final ContactEntryType type;
		private final boolean person;
		
		public AddContactEntryEditor(boolean person) {
			this.type = ContactEntryType.Address;
			this.person = person;
		}

		public AddContactEntryEditor(ContactEntryType type) {
			this.type = type;
			this.person = true;
		}
		
		@Override
		public Form<ContactEntry> createForm() {
			return new ContactEntryPanel(type, person);
		}

		@Override
		protected ContactEntry getPart(List<ContactEntry> contacts) {
			ContactEntry contactEntry = new ContactEntry();
			contactEntry.typeOfContact = type;
			if ("I".equals(type)) {
				contactEntry.value = "http://";
			}
			return contactEntry;
		}

		@Override
		protected void setPart(List<ContactEntry> contacts, ContactEntry p) {
			contacts.add(p);
		}
    };

	private class RemoveContactEntryAction extends ResourceAction {
		private final ContactEntry contactEntry;
		
		private RemoveContactEntryAction(ContactEntry contactEntry) {
			this.contactEntry = contactEntry;
		}
		
		@Override
		public void action() {
			getObject().remove(contactEntry);
			fireObjectChange();
		}
    };
	
	//
	
	@Override
	public Form<List<ContactEntry>> createFormPanel() {
		// unused
		return null;
	}

	@Override
	protected void show(List<ContactEntry> contacts) {
		for (ContactEntry contactEntry : contacts) {
			addText(contactEntry.toHtml());
			if (isEditable()) {
				addAction(new RemoveContactEntryAction(contactEntry));
			}
			addGap();
		}
	}
	
	@Override
	protected void showActions() {
		addAction(new AddContactEntryEditor(true), "AddAddressPerson");
		addAction(new AddContactEntryEditor(false), "AddAddressOrganisation");
		addAction(new AddContactEntryEditor(ContactEntryType.Email), "AddEmail");
		addAction(new AddContactEntryEditor(ContactEntryType.Phone), "AddPhone");
		addAction(new AddContactEntryEditor(ContactEntryType.Internet), "AddInternet");
	}
}
