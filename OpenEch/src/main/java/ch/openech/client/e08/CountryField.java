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
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.xml.read.StaxEch0072;

/* Dieses Feld wurde erst mit ech 112 gebraucht. Dazu wurde 
 * das PlaceField eingedampft (alles was mit Municipality zu tun hatte entfernt).
 * 
 */
public class CountryField extends ObjectField<CountryIdentification> implements DemoEnabled, Indicator {
	private final SwitchLayout switchLayout;
	private final ComboBox comboBoxCountry;
	private final TextField textCountry;
	private final TextField textCountryUnknown;
	
	public CountryField(String name) {
		super(name);
		comboBoxCountry = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxCountry.setObjects(StaxEch0072.getInstance().getCountryIdentifications());

		switchLayout = ClientToolkit.getToolkit().createSwitchLayout(); // comboBoxCountry, textCountry
		switchLayout.show(comboBoxCountry);

		textCountry = ClientToolkit.getToolkit().createTextField(listener(), Formats.getInstance().getFormat(CountryIdentification.class, CountryIdentification.COUNTRY_IDENTIFICATION.countryNameShort).getSize());
		textCountryUnknown = ClientToolkit.getToolkit().createReadOnlyTextField();
		textCountryUnknown.setText("-");
		
		addAction(new ObjectFieldEditor());
		addAction(new CountrySelectAction());
		addAction(new CountryRemoveAction());
		
		setObject(Swiss.createCountryIdentification());
	}
	
	@Override
	protected IComponent getComponent0() {
		return switchLayout;
	}
	
	private final class CountrySelectAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			modeSelectCountry();
			comboBoxCountry.requestFocus();
		}
	}

	private final class CountryRemoveAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			modeUnknown();
		}
	}

	private void modeSelectCountry() {
		switchLayout.show(comboBoxCountry);
		fireChange();
	}

	private void modeFreeCountry() {
		textCountry.setText(getObject().countryNameShort); // TODO
		switchLayout.show(textCountry);
		fireChange();
	}

	private void modeUnknown() {
		switchLayout.show(textCountryUnknown);
		fireChange();
	}

	//

	@Override
	public CountryIdentification getObject() {
		CountryIdentification countryIdentification = super.getObject();
		countryIdentification.clear();
		if (switchLayout.getShownComponent() == comboBoxCountry) {
			CountryIdentification c = (CountryIdentification) comboBoxCountry.getSelectedObject();
			if (c != null) {
				c.copyTo(countryIdentification);
			}
		} else if (switchLayout.getShownComponent() == textCountry) {
			countryIdentification.countryNameShort = textCountry.getText();
		}
		return countryIdentification;
	}

	@Override
	public void setObject(CountryIdentification countryIdentification) {
		if (countryIdentification == null) {
			countryIdentification = new CountryIdentification();
		}
		super.setObject(countryIdentification);
	}
		
	@Override
	public void display(CountryIdentification value) {
		int index = StaxEch0072.getInstance().getCountryIdentifications().indexOf(value);
		if (index >= 0) {
			comboBoxCountry.setSelectedObject(value);
			modeSelectCountry();
		} else {
			modeFreeCountry();
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
