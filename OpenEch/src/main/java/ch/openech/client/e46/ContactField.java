package ch.openech.client.e46;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.VisualList;

public class ContactField extends ObjectField<Contact> {
	private VisualList list;
	
	public ContactField(Object key) {
		this(key, true);
	}
	
	public ContactField(Object key, boolean editable) {
		super(key);
		
		list = ClientToolkit.getToolkit().createVisualList();
		// list.setFixedCellHeight(-1); // unfixed cell height
		// add(new SizedScrollPane(list, 7, 20));
		if (editable) {
			addAction(new AddContactEntryEditor(true));
			addAction(new AddContactEntryEditor(false));
			addAction(new AddContactEntryEditor("E"));
			addAction(new AddContactEntryEditor("P"));
			addAction(new AddContactEntryEditor("I"));
			addAction(new RemoveContactEntryAction());
		}
	}
	
	@Override
	protected IComponent getComponent0() {
		return list;
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
		@Override
		public void actionPerformed(ActionEvent e) {
			Contact contact = getObject();
			int index = list.getSelectedIndex();
			if (index >= 0) {
				contact.entries.remove(index);
				fireObjectChange();
			}
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
		List<String> htmlStrings = new ArrayList<String>();
		if (contact != null) {
			for (ContactEntry entry : contact.entries) {
				htmlStrings.add(entry.toHtml());
			}
		}
		list.setObjects(htmlStrings);
	}

}
