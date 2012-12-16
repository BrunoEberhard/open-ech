package ch.openech.client.e10;

import java.util.logging.Logger;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.Zip;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.autofill.NameGenerator;
import ch.openech.mj.db.model.PropertyInterface;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.util.Plz;
import ch.openech.util.PlzImport;

public class TownField extends AbstractEditField<String> implements DemoEnabled, DependingOnFieldAbove<Zip> {
	private static final Logger logger = Logger.getLogger(TownField.class.getName());
	
	private final SwitchLayout switchLayout;
	private final TextField textFieldSwiss;
	private final TextField textFieldZipForeign;
	
	public TownField(PropertyInterface property) {
		super(property, true);
		
		textFieldSwiss = ClientToolkit.getToolkit().createReadOnlyTextField();
		textFieldZipForeign = ClientToolkit.getToolkit().createTextField(listener(), EchFormats.foreignZipCode);
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(textFieldSwiss);
	}
	
	@Override
	public IComponent getComponent() {
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
	public Zip getKeyOfDependedField() {
		return Address.ADDRESS.zip;
	}

	@Override
	public void valueChanged(Zip zip) {		
		if (zip.isSwiss()) {
			switchLayout.show(textFieldSwiss);
			if (zip.swissZipCodeId != null) {
				Integer onrp = zip.swissZipCodeId;
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
