package ch.openech.client.e10;

import java.util.List;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.Zip;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.db.model.PropertyInterface;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;
import ch.openech.util.PlzImport;

public class ZipField extends AbstractEditField<Zip> implements DependingOnFieldAbove<String>, DemoEnabled {
	private final ComboBox<Zip> comboBoxSwiss;
	private final TextField textFieldForeign;
	private final SwitchLayout switchLayout;
	
	public ZipField(PropertyInterface property) {
		super(property, true);
		
		comboBoxSwiss = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxSwiss.setObjects(PlzImport.getInstance().getZipList());

		textFieldForeign = ClientToolkit.getToolkit().createTextField(listener(), EchFormats.foreignZipCode);

		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(comboBoxSwiss);
	}
	
	@Override
	public IComponent getComponent() {
		return switchLayout;
	}
	
	@Override
	public Zip getObject() {
		Zip zip = new Zip();
		if (switchLayout.getShownComponent() == comboBoxSwiss) {
			if (comboBoxSwiss.getSelectedObject() != null) {
				CloneHelper.deepCopy(comboBoxSwiss.getSelectedObject(), zip);
			}
		} else if (switchLayout.getShownComponent() == textFieldForeign) {
			zip.foreignZipCode = textFieldForeign.getText();
		}
		return zip;
	}
	
	boolean isSwiss() {
		return switchLayout.getShownComponent() == comboBoxSwiss;
	}

	boolean isForeign() {
		return switchLayout.getShownComponent() == textFieldForeign;
	}
	
	@Override
	public void setObject(Zip zip) {
		if (zip.isForeign()) {
			switchLayout.show(textFieldForeign);
			textFieldForeign.setText(zip.foreignZipCode);
		} else {
			switchLayout.show(comboBoxSwiss);
			comboBoxSwiss.setSelectedObject(zip);
		}
	}

	@Override
	public void valueChanged(String coutryIso2) {
		if (StringUtils.equals(coutryIso2, "CH", "LI", null)) {
			if (switchLayout.getShownComponent() != comboBoxSwiss) {
				comboBoxSwiss.setSelectedObject(null);
				switchLayout.show(comboBoxSwiss);
				fireChange();
			}
		} else {
			if (switchLayout.getShownComponent() != textFieldForeign) {
				textFieldForeign.setText("");
				switchLayout.show(textFieldForeign);
				fireChange();
			}
		}
	}
	
	//

	@Override
	public void fillWithDemoData() {
		if (switchLayout.getShownComponent() == comboBoxSwiss) {
			List<Zip> zipList = PlzImport.getInstance().getZipList();
			int index = (int)(Math.random() * (double)zipList.size());
			Zip zip = zipList.get(index);
			comboBoxSwiss.setSelectedObject(zip);
		} else {
			textFieldForeign.setText("" + (int)(10000 + 90000 * Math.random()));
		}
		fireChange();
	}

}
