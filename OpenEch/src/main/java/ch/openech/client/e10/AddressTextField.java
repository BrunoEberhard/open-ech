package ch.openech.client.e10;

import ch.openech.dm.common.Address;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.TextField;

public class AddressTextField extends ObjectField<Address> implements Indicator {
	private final boolean swiss;
	private final boolean person;
	private final boolean organisation;
	
	private final TextField text;
	
	public AddressTextField(Object key, boolean editable) {
		this(key, editable, false, false, false);
	}

	public AddressTextField(Object key, boolean swiss, boolean person, boolean organisation) {
		this(key, true, swiss, person, organisation);
	}
	
	public AddressTextField(Object key, boolean editable, boolean swiss, boolean person, boolean organisation) {
		super(key, editable);

		this.swiss = swiss;
		this.person = person;
		this.organisation = organisation;
	
		text = ClientToolkit.getToolkit().createReadOnlyTextField();
		
		if (editable) {
			addContextAction(new ObjectFieldEditor());
			addContextAction(new RemoveObjectAction());
		} 
	}

	@Override
	public Object getComponent() {
		return decorateWithContextActions(text);
	}
	
	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
	}

	protected FormVisual<Address> createEditFrame() {
		return new AddressPanel(swiss, person, organisation);
	}
	
	@Override
	public FormVisual<Address> createFormPanel() {
		return new AddressPanel(swiss, person, organisation);
	}

	public static String format(Address a) {
		String s = a.toHtml();
		s = s.replaceAll("&nbsp;", " ");
		s = s.replaceAll("<BR>", ", ");
		s = s.replaceAll("<HTML>", "");
		s = s.replaceAll("</HTML>", "");
		s = s.trim();
		if (s.endsWith(",")) {
			s = s.substring(0, s.length()-1);
		}
		return s;
	}

	@Override
	protected void show(Address address) {
		text.setText(format(address));
	}

}
