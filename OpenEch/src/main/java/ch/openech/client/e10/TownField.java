package ch.openech.client.e10;

import java.util.logging.Logger;

import ch.openech.dm.common.Address;
import ch.openech.dm.common.Plz;
import ch.openech.dm.common.Zip;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.autofill.NameGenerator;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.Formats;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.util.PlzImport;

public class TownField extends AbstractEditField<String> implements DemoEnabled, DependingOnFieldAbove<Zip> {
	private static final Logger logger = Logger.getLogger(TownField.class.getName());
	
	private final SwitchLayout switchLayout;
	private final TextField textFieldSwiss;
	private final TextField textFieldZipForeign;
	
	public TownField(Object key) {
		super(Constants.getConstant(key), true);
		
		textFieldSwiss = ClientToolkit.getToolkit().createReadOnlyTextField();
		textFieldZipForeign = ClientToolkit.getToolkit().createTextField(listener(), Formats.getInstance().getFormat(Zip.class, Zip.ZIP_TOWN.foreignZipCode).getSize());
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(textFieldSwiss);
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
	public void setObject(String town) {
		if (switchLayout.getShownComponent() == textFieldSwiss) {
			textFieldSwiss.setText(town);
		} else if (switchLayout.getShownComponent() == textFieldZipForeign) {
			textFieldZipForeign.setText(town);
		}
	}
	
	//
	
	@Override
	public void fillWithDemoData() {
		if (switchLayout.getShownComponent() == textFieldZipForeign) {
			textFieldZipForeign.setText(NameGenerator.officialName() + "Town");
			fireChange();
		}
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
			switchLayout.show(textFieldSwiss);
			if (zip.swissZipCodeId != null) {
				Integer onrp = Integer.parseInt(zip.swissZipCodeId);
				Plz plz = PlzImport.getInstance().getPlz(onrp);
				if (plz != null) {
					textFieldSwiss.setText(plz.ortsbezeichnung);
					switchLayout.show(textFieldSwiss);
					fireChange();
					return;
				}
				logger.warning("Townfield could not found value of ZipField: " + zip.swissZipCodeId);
				textFieldSwiss.setText("");
				fireChange();
			}
		} else {
			if (switchLayout.getShownComponent() != textFieldZipForeign) {
				switchLayout.show(textFieldZipForeign);
				textFieldZipForeign.setText("");
				fireChange();
			}
		}
	}

}
