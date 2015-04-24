package ch.openech.frontend.e10;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.mock.Mocking;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.common.Address;

public class AddressFormElement extends ObjectPanelFormElement<Address> implements Mocking {
	private final boolean swiss;
	private final boolean person;
	private final boolean organisation;
	private boolean enabled = true;

	public AddressFormElement(Address key, boolean editable) {
		this(Keys.getProperty(key), editable, false, false, false);
	}

	public AddressFormElement(PropertyInterface property, boolean editable) {
		this(property, editable, false, false, false);
	}

	public AddressFormElement(Address key, boolean swiss, boolean person, boolean organisation) {
		this(Keys.getProperty(key), swiss, person, organisation);
	}
	
	public AddressFormElement(PropertyInterface property, boolean swiss, boolean person, boolean organisation) {
		this(property, true, swiss, person, organisation);
	}
	
	public AddressFormElement(PropertyInterface property, boolean editable, boolean swiss, boolean person, boolean organisation) {
		super(property, editable);
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
	public void mock() {
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
		// Was war hier gemeint? Einen default value aus den Preferences auslesen?
		// context.getApplicationContext();
		return address;
	}
	
	
}
