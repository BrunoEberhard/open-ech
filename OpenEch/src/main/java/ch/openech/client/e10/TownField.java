package ch.openech.client.e10;

import java.util.logging.Logger;

import ch.openech.dm.common.Address;
import ch.openech.dm.common.Plz;
import ch.openech.dm.common.Zip;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.Formats;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.util.PlzImport;

public class TownField extends ObjectField<String> implements DemoEnabled, DependingOnFieldAbove<Zip> {
	private static final Logger logger = Logger.getLogger(TownField.class.getName());
	
	private final SwitchLayout switchLayout;
	private final TextField textFieldSwiss;
	private final TextField textFieldZipForeign;
	private final TextField textFieldEmpty;
	
	public TownField(Object key) {
		super(Constants.getConstant(key));
		
		textFieldSwiss = ClientToolkit.getToolkit().createReadOnlyTextField();
		
		textFieldZipForeign = ClientToolkit.getToolkit().createTextField(listener(), Formats.getInstance().getFormat(Zip.class, Zip.ZIP_TOWN.foreignZipCode).getSize());
		textFieldEmpty = ClientToolkit.getToolkit().createReadOnlyTextField();
		
		textFieldEmpty.setText("-");
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(textFieldEmpty);
	}
	
	@Override
	public Object getComponent() {
		return switchLayout;
	}

	@Override
	public String getObject() {
		if (switchLayout.getShownComponent() == textFieldSwiss) {
			return textFieldSwiss.getText();
		} else if (switchLayout.getShownComponent() == textFieldZipForeign) {
			return textFieldZipForeign.getText();
		} else {
			return null;
		}
	}

	@Override
	protected void show(String town) {
//		if (switchLayout.getShownComponent() == textFieldSwiss) {
//			textFieldSwiss.setText(town);
//		} else if (switchLayout.getShownComponent() == textFieldZipForeign) {
//			textFieldZipForeign.setText(town);
//		} else {
//			return null;
//		}
	}
	
	
	//
	
	@Override
	public void fillWithDemoData() {
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
	}

	@Override
	public String getNameOfDependedField() {
		return Constants.getConstant(Address.ADDRESS.zip);
	}

	@Override
	public void setDependedField(EditField<Zip> field) {
		ZipField zipfield = (ZipField) field;
		Zip zip = zipfield.getObject();
		
		if (zipfield.isSwiss()) {
			if (zip.swissZipCodeId != null) {
				Integer onrp = Integer.parseInt(zip.swissZipCodeId);
				for (Plz plz : PlzImport.getInstance().getZipCodes()) {
					if (plz.onrp == onrp) {
						textFieldSwiss.setText(plz.ortsbezeichnung);
						switchLayout.show(textFieldSwiss);
						return;
					}
				}
				switchLayout.show(textFieldEmpty);
				logger.warning("Townfield could not found value of ZipField: " + zip.swissZipCodeId);
			} else {
				switchLayout.show(textFieldEmpty);
			}
		} else if (zipfield.isForeign()) {
			if (switchLayout.getShownComponent() != textFieldZipForeign) {
				switchLayout.show(textFieldZipForeign);
				textFieldZipForeign.setText("");
			}
		} else {
			switchLayout.show(textFieldEmpty);
		}
		fireChange();
	}

}
