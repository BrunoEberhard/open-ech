package ch.openech.frontend.e10;

import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.DemoEnabled;

import ch.openech.datagenerator.DataGenerator;
import  ch.openech.model.common.Address;

public class AddressField extends ObjectFlowField<Address> implements DemoEnabled {
	// TODO private final PageContext context;
	private final boolean swiss;
	private final boolean person;
	private final boolean organisation;
	private boolean enabled = true;

	public AddressField(Address key, boolean editable) {
		this(Keys.getProperty(key), editable, false, false, false);
	}

	public AddressField(PropertyInterface property, boolean editable) {
		this(property, editable, false, false, false);
	}

	public AddressField(Address key, boolean swiss, boolean person, boolean organisation) {
		this(Keys.getProperty(key), swiss, person, organisation);
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

	public Form<Address> createEditFrame() {
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
	public Form<Address> createFormPanel() {
		return new AddressPanel(swiss, person, organisation);
	}

	@Override
	public void show(Address address) {
		if (enabled) {
			addText(address.toHtml());
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
