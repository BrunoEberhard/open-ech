package ch.openech.client.e11;

import java.awt.event.ActionEvent;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.person.Nationality;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.read.StaxEch0072;

public class NationalityField extends ObjectField<Nationality> {
	private final ComboBox comboBox;
	private final TextField textField;
	private final TextField textFieldWithout;
	private final TextField textFieldUnknown;
	
	private final SwitchLayout switchLayout;
	
	public NationalityField(Object key) {
		super(key);
		
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		comboBox.setObjects(StaxEch0072.getInstance().getCountryIdentifications());
		
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		textFieldWithout = ClientToolkit.getToolkit().createReadOnlyTextField();
		textFieldWithout.setText("Staatenlos");
		
		textFieldUnknown = ClientToolkit.getToolkit().createReadOnlyTextField();
		textFieldUnknown.setText("Staatsangehörigkeit unbekannt");
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(comboBox);
    	
		addAction(new NationalitySelectAction());
		addAction(new NationalityRemoveAction());
		addAction(new NationalityUnknownAction());
		addAction(new ObjectFieldEditor());
	}
	
	@Override
	protected IComponent getComponent0() {
		return switchLayout;
	}

	private final class NationalitySelectAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			modeSelectCountry();
		}
	}
	
	private final class NationalityRemoveAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			modeStaatenlos();
		}
	}
	
	private final class NationalityUnknownAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			modeUnknown();
		}
	}

	@Override
	public FormVisual<Nationality> createFormPanel() {
		return new NationalityFreePanel();
	}
	
	private String visualizeFreeEntry() {
		Nationality nationality = getObject();
		StringBuilder s = new StringBuilder();
		s.append("Status = "); s.append(nationality.nationalityStatus); s.append(", ");
		s.append(nationality.nationalityCountry.toStringReadable());
		return s.toString();
	}
	
	@Override
	public void setObject(Nationality nationality) {
		if (nationality == null) {
			nationality = new Nationality();
			nationality.nationalityStatus = "0";
		}
		super.setObject(nationality);
	}
	
	@Override
	protected void display(Nationality nationality) {
		String status = nationality.nationalityStatus;
		if ("2".equals(status)) {
			CountryIdentification countryIdentification = nationality.nationalityCountry;
			int index = StaxEch0072.getInstance().getCountryIdentifications().indexOf(countryIdentification);
			if (index < 0) {
				modeFree();
			} else {
				comboBox.setSelectedObject(countryIdentification);
				modeSelectCountry();
			}
		} else if ("1".equals(status)) {
			modeStaatenlos();
		} else if ("0".equals(status) || StringUtils.isBlank(status)) {
			modeUnknown();
		} else {
			modeFree();
		}
	}

	@Override
	public Nationality getObject() {
		Nationality nationality = super.getObject();
		if (switchLayout.getShownComponent() == comboBox) {
			CountryIdentification countryIdentification = (CountryIdentification) comboBox.getSelectedObject();
			if (countryIdentification != null) {
				nationality.nationalityStatus = "2";
				countryIdentification.copyTo(nationality.nationalityCountry);
			} else {
				nationality.nationalityStatus = "0";
				nationality.nationalityCountry.clear();
			}
		} else if (switchLayout.getShownComponent() == textFieldUnknown) {
			nationality.nationalityStatus = "0";
			nationality.nationalityCountry.clear();
		} else if (switchLayout.getShownComponent() == textFieldWithout) {
			nationality.nationalityStatus = "1";
			nationality.nationalityCountry.clear();
		} else {
			if (nationality.nationalityCountry.isEmpty()) {
				nationality.nationalityStatus = "0";
			} else {
				nationality.nationalityStatus = "2";
			}
		}
		return nationality;
	}

	
	// modes

	private void modeSelectCountry() {
		switchLayout.show(comboBox);
		comboBox.requestFocus();
	}

	private void modeStaatenlos() {
		switchLayout.show(textFieldWithout);
	}
	
	private void modeUnknown() {
		switchLayout.show(textFieldUnknown);
	}
	
	private void modeFree() {
		textField.setText(visualizeFreeEntry());
		switchLayout.show(textField);
	}

}
