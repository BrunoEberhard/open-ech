package ch.openech.client.e08;

import java.awt.event.ActionEvent;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.Swiss;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.db.model.Formats;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.xml.read.StaxEch0072;

/* Dieses Feld wurde erst mit ech 112 gebraucht. 
 * 
 */
public class CountryField extends ObjectField<CountryIdentification> implements DemoEnabled, Indicator {
	private final SwitchLayout switchLayout;
	private final ComboBox comboBox;
	private final TextField textCountry;
	private final TextField textCountryUnknown;
	
	public CountryField(String name) {
		super(name);
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		comboBox.setObjects(StaxEch0072.getInstance().getCountryIdentifications());

		switchLayout = ClientToolkit.getToolkit().createSwitchLayout(); // comboBoxCountry, textCountry
		switchLayout.show(comboBox);

		textCountry = ClientToolkit.getToolkit().createTextField(listener(), Formats.getInstance().getFormat(CountryIdentification.class, CountryIdentification.COUNTRY_IDENTIFICATION.countryNameShort).getSize());
		textCountryUnknown = ClientToolkit.getToolkit().createReadOnlyTextField();
		textCountryUnknown.setText("-");
		
		addContextAction(new ObjectFieldEditor());
		addContextAction(new CountrySelectAction());
		addContextAction(new CountryRemoveAction());
		
		setObject(Swiss.createCountryIdentification());
	}
	
	@Override
	public Object getComponent() {
		return decorateWithContextActions(switchLayout);
	}
	
	private final class CountrySelectAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			CountryIdentification country = getObject();
			Swiss.createCountryIdentification().copyTo(country);
			fireObjectChange();
			comboBox.requestFocus();
		}
	}

	private final class CountryRemoveAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			CountryIdentification country = getObject();
			country.clear();
		}
	}

	//

	@Override
	public void setObject(CountryIdentification countryIdentification) {
		if (countryIdentification == null) {
			countryIdentification = new CountryIdentification();
		}
		super.setObject(countryIdentification);
	}
		
	@Override
	protected void fireChange() {
		CountryIdentification country = super.getObject();
		if (switchLayout.getShownComponent() == textCountry) {
			String name = textCountry.getText();
			country.clear();
			country.countryNameShort = name;
		} else {
			((CountryIdentification) comboBox).copyTo(country);
		}
	}

	@Override
	public void display(CountryIdentification value) {
		int index = StaxEch0072.getInstance().getCountryIdentifications().indexOf(value);
		if (index >= 0) {
			comboBox.setSelectedObject(value);
			switchLayout.show(comboBox);
		} else if (!value.isEmpty()){
			textCountry.setText(getObject().countryNameShort); // TODO
			switchLayout.show(textCountry);
		} else {
			switchLayout.show(textCountryUnknown);
		}
	}

	@Override
	public void fillWithDemoData() {
		int index = (int) (Math.random() * (double) StaxEch0072.getInstance().getCountryIdentifications().size());
		CountryIdentification country =  StaxEch0072.getInstance().getCountryIdentifications().get(index);
		setObject(CloneHelper.cloneIfPossible(country));
	}

	@Override
	public FormVisual<CountryIdentification> createFormPanel() {
		return new CountryFreePanel();
	}

}
