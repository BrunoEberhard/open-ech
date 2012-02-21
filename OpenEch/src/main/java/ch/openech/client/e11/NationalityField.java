package ch.openech.client.e11;

import java.awt.event.ActionEvent;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.Swiss;
import ch.openech.dm.person.Nationality;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
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
			Nationality nationality = getObject();
			nationality.nationalityStatus = "2";
			Swiss.createCountryIdentification().copyTo(nationality.nationalityCountry);
			fireObjectChange();
			comboBox.requestFocus();
		}
	}
	
	private final class NationalityRemoveAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			Nationality nationality = getObject();
			nationality.nationalityStatus = "1";
			nationality.nationalityCountry.clear();
			fireObjectChange();		
		}
	}
	
	private final class NationalityUnknownAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			Nationality nationality = getObject();
			nationality.nationalityStatus = "0";
			nationality.nationalityCountry.clear();
			fireObjectChange();
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
	protected void fireChange() {
		Nationality nationality = getObject();
		nationality.nationalityStatus = "2";
		((CountryIdentification) comboBox.getSelectedObject()).copyTo(nationality.nationalityCountry);
		super.fireChange();
	}

	@Override
	protected void display(Nationality nationality) {
		String status = nationality.nationalityStatus;
		if ("2".equals(status)) {
			CountryIdentification countryIdentification = nationality.nationalityCountry;
			int index = StaxEch0072.getInstance().getCountryIdentifications().indexOf(countryIdentification);
			if (index < 0) {
				switchLayout.show(textField);
				textField.setText(visualizeFreeEntry());
			} else {
				comboBox.setSelectedObject(countryIdentification);
				switchLayout.show(comboBox);
			}
		} else if ("1".equals(status)) {
			switchLayout.show(textFieldWithout);
		} else if ("0".equals(status)) {
			switchLayout.show(textFieldUnknown);
		} else {
			switchLayout.show(textField);
			textField.setText(visualizeFreeEntry());
		}
	}

}
