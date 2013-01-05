package ch.openech.client.e10;

import java.util.logging.Logger;

import ch.openech.dm.EchFormats;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.autofill.NameGenerator;
import ch.openech.mj.db.model.PropertyInterface;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;

public class TownField extends AbstractEditField<String> implements DemoEnabled {
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

}
