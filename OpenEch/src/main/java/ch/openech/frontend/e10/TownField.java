package ch.openech.frontend.e10;

import org.minimalj.autofill.NameGenerator;
import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.IComponent;
import org.minimalj.frontend.toolkit.SwitchLayout;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.PropertyInterface;
import org.minimalj.util.DemoEnabled;

import ch.openech.model.EchFormats;

public class TownField extends AbstractEditField<String> implements DemoEnabled {
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
