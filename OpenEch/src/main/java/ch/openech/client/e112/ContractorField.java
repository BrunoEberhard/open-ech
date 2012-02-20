package ch.openech.client.e112;

import ch.openech.client.e10.AddressTextField;
import ch.openech.client.e46.ContactField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.common.Address;
import ch.openech.dm.contact.Contact;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;

public class ContractorField extends ObjectField<Contact> implements Indicator {
	private final TextField text;
	
	public ContractorField(Object key) {
		super(key);
	
		text = ClientToolkit.getToolkit().createReadOnlyTextField();
		
		addAction(new ObjectFieldEditor());
		addAction(new RemoveObjectAction());
	}
	
	@Override
	protected IComponent getComponent0() {
		return text;
	}

	@Override
	protected void display(Contact contact) {
		String s = "Id: " + contact.stringId;
		if (contact.getAddressList().size() > 0) {
			Address address = contact.getAddressList().get(0).address;
			if (address != null) {
				s += AddressTextField.format(address);
			}
		}
		text.setText(s);
	}

	@Override
	public FormVisual<Contact> createFormPanel() {
		AbstractFormVisual<Contact> form = new EchFormPanel<Contact>(Contact.class);
		form.line(Contact.CONTACT.stringId);
		form.area(new ContactField(null));
		return form;
	}
	
}