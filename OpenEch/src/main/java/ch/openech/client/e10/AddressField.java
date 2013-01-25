package ch.openech.client.e10;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.common.Address;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Constants;
import ch.openech.mj.model.PropertyInterface;

public class AddressField extends ObjectFlowField<Address> implements DemoEnabled {
	// TODO private final PageContext context;
	private final boolean swiss;
	private final boolean person;
	private final boolean organisation;
	private boolean enabled = true;

	public AddressField(Address key, boolean editable) {
		this(Constants.getProperty(key), editable, false, false, false);
	}

	public AddressField(PropertyInterface property, boolean editable) {
		this(property, editable, false, false, false);
	}

	public AddressField(Address key, boolean swiss, boolean person, boolean organisation) {
		this(Constants.getProperty(key), swiss, person, organisation);
	}
	
	public AddressField(PropertyInterface property, boolean swiss, boolean person, boolean organisation) {
		this(property, true, swiss, person, organisation);
	}
	
	public AddressField(PropertyInterface property, boolean editable, boolean swiss, boolean person, boolean organisation) {
		super(property, editable);
//		this.context = context;
		this.swiss = swiss;
		this.person = person;
		this.organisation = organisation;
	
	}

	public IForm<Address> createEditFrame() {
		return new AddressPanel(swiss, person, organisation);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public void fillWithDemoData() {
		setObject(DataGenerator.address(true, true, false));
	}

	@Override
	public IForm<Address> createFormPanel() {
		return new AddressPanel(swiss, person, organisation);
	}

	@Override
	public void show(Address address) {
		if (enabled) {
			addHtml(address.toHtml());
		}
	}

	@Override
	public void showActions() {
		if (enabled) {
			addAction(new ObjectFieldEditor());
			addAction(new RemoveObjectAction());
		}
	}

	@Override
	protected Address newInstance() {
		Address address = new Address();
		// context.getApplicationContext();
		return address;
	}
	
	
}
