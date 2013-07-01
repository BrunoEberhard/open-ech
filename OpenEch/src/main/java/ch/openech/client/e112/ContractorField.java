package ch.openech.client.e112;

import ch.openech.client.e46.ContactField;
import ch.openech.client.ewk.event.EchForm;
import ch.openech.dm.common.Address;
import ch.openech.dm.contact.Contact;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.PropertyInterface;

public class ContractorField extends ObjectFlowField<Contact> {
	
	public ContractorField(PropertyInterface property) {
		super(property, true);
	}
	
	@Override
	protected void show(Contact contact) {
		String s = "Id: " + contact.stringId;
		if (contact.getAddressList().size() > 0) {
			Address address = contact.getAddressList().get(0).address;
			if (address != null) {
				s += address.toString();
			}
		}
		addObject(s);
	}

	@Override
	protected void showActions() {
		addAction(new ObjectFieldEditor());
		addAction(new RemoveObjectAction());
	}

	@Override
	public IForm<Contact> createFormPanel() {
		Form<Contact> form = new EchForm<Contact>();
		form.line(Contact.CONTACT.stringId);
		form.line(new ContactField(null));
		return form;
	}
	
}
