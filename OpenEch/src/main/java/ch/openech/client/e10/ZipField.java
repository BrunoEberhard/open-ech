package ch.openech.client.e10;

import ch.openech.dm.common.Address;
import ch.openech.dm.common.Plz;
import ch.openech.dm.common.Zip;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.Formats;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;
import ch.openech.util.PlzImport;

// TODO implement ZipTownField
public class ZipField extends ObjectField<Zip> implements DependingOnFieldAbove<String> {
	private final ComboBox<Plz> comboBoxSwiss;
	private final TextField textFieldForeign;
	private final TextField textFieldEmpty;
	private final SwitchLayout switchLayout;
	
	public ZipField(Object key) {
		super(Constants.getConstant(key));
		
		comboBoxSwiss = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxSwiss.setObjects(PlzImport.getInstance().getZipCodes());

		textFieldForeign = ClientToolkit.getToolkit().createTextField(listener(), Formats.getInstance().getFormat(Zip.class, Zip.ZIP_TOWN.foreignZipCode).getSize());

		textFieldEmpty = ClientToolkit.getToolkit().createReadOnlyTextField();
		textFieldEmpty.setText("-");
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(comboBoxSwiss);
	}
	
	@Override
	public Object getComponent() {
		return switchLayout;
	}
	
	@Override
	public Zip getObject() {
		Zip zipTown = super.getObject();
		
		zipTown.clear();
		if (switchLayout.getShownComponent() == comboBoxSwiss) {
			if (comboBoxSwiss.getSelectedObject() != null) {
				Plz plz = (Plz) comboBoxSwiss.getSelectedObject();
				if (plz.postleitzahl > 0) zipTown.swissZipCode = Integer.toString(plz.postleitzahl);
				if (plz.zusatzziffern > 0) zipTown.swissZipCodeAddOn = Integer.toString(plz.zusatzziffern);
				if (plz.onrp > 0) zipTown.swissZipCodeId = Integer.toString(plz.onrp);
			}
		} else if (switchLayout.getShownComponent() == textFieldForeign) {
			zipTown.foreignZipCode = textFieldForeign.getText();
		}

		return zipTown;
	}
	
	boolean isSwiss() {
		return switchLayout.getShownComponent() == comboBoxSwiss;
	}

	boolean isForeign() {
		return switchLayout.getShownComponent() == textFieldForeign;
	}
	
	@Override
	protected void show(Zip zip) {
		// muss man das wirklich implementieren?
//		if (zip.isSwiss()) {
//			switchLayout.show(comboBoxSwiss);
//			
//		} else {
//			
//		}
	}

	@Override
	public String getNameOfDependedField() {
		return Address.ADDRESS.country;
	}

	@Override
	public void setDependedField(EditField<String> field) {
		String coutryIso2 = field.getObject();
		if ("CH".equals(coutryIso2) || "LI".equals(coutryIso2)) {
			if (switchLayout.getShownComponent() != comboBoxSwiss) {
				comboBoxSwiss.setSelectedObject(null);
				switchLayout.show(comboBoxSwiss);
			}
		} else if (!StringUtils.isBlank(coutryIso2)) {
			if (switchLayout.getShownComponent() != textFieldForeign) {
				textFieldForeign.setText("");
				switchLayout.show(textFieldForeign);
			}
		} else {
			switchLayout.show(textFieldEmpty);
		}
		fireChange();
	}
	
	//

//	@Override
//	public void fillWithDemoData() {
//		int index = (int)(Math.random() * (double)PlzImport.getInstance().getZipCodes().size());
//		Plz plz = PlzImport.getInstance().getZipCodes().get(index);
//
//		Zip zipTown = getObject();
//		if (zipTown == null) {
//			// TODO ist das wirklich nötig?
//			zipTown = new Zip();
//		}
//		zipTown.town = plz.ortsbezeichnung;
//		zipTown.swissZipCode = "" + plz.postleitzahl;
//		zipTown.swissZipCodeAddOn = "" + plz.zusatzziffern;
//		zipTown.swissZipCodeId = "" + plz.onrp;
//		
//		setObject(zipTown);
//	}

}
