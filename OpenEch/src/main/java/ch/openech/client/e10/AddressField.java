package ch.openech.client.e10;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.common.Address;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;

public class AddressField extends MultiLineObjectField<Address> implements DemoEnabled {
	private final boolean swiss;
	private final boolean person;
	private final boolean organisation;
	
	public AddressField(Object key, boolean editable) {
		this(key, editable, false, false, false);
	}

	public AddressField(Object key, boolean swiss, boolean person, boolean organisation) {
		this(key, true, swiss, person, organisation);
	}
	
	public AddressField(Object key, boolean editable, boolean swiss, boolean person, boolean organisation) {
		super(key, editable);
		this.swiss = swiss;
		this.person = person;
		this.organisation = organisation;
	
	}

	public FormVisual<Address> createEditFrame() {
		return new AddressPanel(swiss, person, organisation);
	}
	
	@Override
	public void fillWithDemoData() {
		setObject(DataGenerator.address(true, true, false));
	}

	@Override
	public FormVisual<Address> createFormPanel() {
		return new AddressPanel(swiss, person, organisation);
	}

	@Override
	public void display(Address address) {
		if (address != null) {
			addHtml(address.toHtml());
		}
		if (isEditable()) {
			addAction(new ObjectFieldEditor());
			addAction(new RemoveObjectAction());
		}
	}

}
