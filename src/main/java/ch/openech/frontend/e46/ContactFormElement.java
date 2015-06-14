package ch.openech.frontend.e46;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.contact.ContactEntry;
import ch.openech.model.contact.ContactEntryType;

public class ContactFormElement extends ListFormElement<ContactEntry> {
	
	public ContactFormElement(PropertyInterface property) {
		this(property, true);
	}
	
	public ContactFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}
	
	public class AddContactEntryEditor extends AddListEntryAction {
		private final ContactEntryType type;
		private final boolean person;
		
		public AddContactEntryEditor(boolean person, String name) {
			super(name);
			this.type = ContactEntryType.Address;
			this.person = person;
		}

		public AddContactEntryEditor(ContactEntryType type, String name) {
			super(name);
			this.type = type;
			this.person = true;
		}
		
		@Override
		public Form<ContactEntry> createForm() {
			return new ContactEntryPanel(type, person);
		}

		@Override
		protected ContactEntry createObject() {
			ContactEntry contactEntry = new ContactEntry();
			contactEntry.typeOfContact = type;
			if ("I".equals(type)) {
				contactEntry.value = "http://";
			}
			return contactEntry;
		}

		@Override
		protected void addEntry(ContactEntry entry) {
			getValue().add(entry);
		}
    };

	private class RemoveContactEntryAction extends Action {
		private final ContactEntry contactEntry;
		
		private RemoveContactEntryAction(ContactEntry contactEntry) {
			this.contactEntry = contactEntry;
		}
		
		@Override
		public void action() {
			getValue().remove(contactEntry);
			handleChange();
		}
    };
	
	//
	
	@Override
	public Form<List<ContactEntry>> createFormPanel() {
		// unused
		return null;
	}

	@Override
	protected void showEntry(ContactEntry entry) {
		add(entry, new RemoveContactEntryAction(entry));
	}

	@Override
	protected Action[] getActions() {
		return new Action[] {
			new AddContactEntryEditor(true, "AddAddressPerson"),
			new AddContactEntryEditor(false, "AddAddressOrganisation"),
			new AddContactEntryEditor(ContactEntryType.Email, "AddEmail"),
			new AddContactEntryEditor(ContactEntryType.Phone, "AddPhone"),
			new AddContactEntryEditor(ContactEntryType.Internet, "AddInternet")
		};
	}

}
