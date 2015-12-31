package ch.openech.frontend.e46;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.resources.Resources;

import ch.openech.model.contact.ContactEntry;
import ch.openech.model.contact.ContactEntryType;

public class ContactFormElement extends ListFormElement<ContactEntry> {
	
	public ContactFormElement(PropertyInterface property) {
		this(property, true);
	}
	
	public ContactFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}
	
	public class AddContactEntryEditor extends AddListEntryEditor {
		private final ContactEntryType type;
		private final boolean person;
		
		public AddContactEntryEditor(boolean person, String resourceName) {
			super(Resources.getString(resourceName));
			this.type = ContactEntryType.Address;
			this.person = person;
		}

		public AddContactEntryEditor(ContactEntryType type, String resourceName) {
			super(Resources.getString(resourceName));
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
	public Form<ContactEntry> createForm(boolean edit) {
		// unused
		return null;
	}

	@Override
	protected void showEntry(ContactEntry entry) {
		if (isEditable()) {
			add(entry, new RemoveContactEntryAction(entry));
		} else {
			add(entry);
		}
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
