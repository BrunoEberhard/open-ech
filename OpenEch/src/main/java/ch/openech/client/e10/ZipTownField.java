package ch.openech.client.e10;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import ch.openech.dm.common.Plz;
import ch.openech.dm.common.ZipTown;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.Formats;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.IntegerUtils;
import ch.openech.util.PlzImport;

public class ZipTownField extends ObjectField<ZipTown> implements DemoEnabled {
	private ComboBox comboBox;
	private HorizontalLayout horizontalLayoutSwiss;
	private HorizontalLayout horizontalLayoutForeign;
	
	private SwitchLayout switchLayout;
	
	private TextField textFieldZipSwiss;
	private TextField textFieldTownSwiss;

	private TextField textFieldZipForeign;
	private TextField textFieldTownForeign;

	private Action select;
	private Action foreignZip;
	
	public ZipTownField() {
		this(null);
	}
		
	public ZipTownField(Object key) {
		super(Constants.getConstant(key));
		
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		comboBox.setObjects(PlzImport.getInstance().getZipCodes());

		textFieldZipSwiss = ClientToolkit.getToolkit().createReadOnlyTextField();
		textFieldTownSwiss = ClientToolkit.getToolkit().createReadOnlyTextField();
		horizontalLayoutSwiss = ClientToolkit.getToolkit().createHorizontalLayout(textFieldZipSwiss, textFieldTownSwiss);

		textFieldZipForeign = ClientToolkit.getToolkit().createTextField(listener(), Formats.getInstance().getFormat(ZipTown.class, ZipTown.ZIP_TOWN.foreignZipCode).getSize());
		textFieldTownForeign = ClientToolkit.getToolkit().createTextField(listener(), Formats.getInstance().getFormat(ZipTown.class, ZipTown.ZIP_TOWN.town).getSize());
		horizontalLayoutForeign = ClientToolkit.getToolkit().createHorizontalLayout(textFieldZipForeign, textFieldTownForeign);
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(comboBox);
		
		createMenu();
	}
	
	@Override
	protected IComponent getComponent0() {
		return switchLayout;
	}

	private void createMenu() {
        select = new AbstractAction("Auswahl CH Postleitzahl") {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeSelectCH();
			}
        };
        addAction(select);

        addAction(new ObjectFieldEditor());

        foreignZip = new AbstractAction("Freie Eingabe ausländischer Ort") {
        	@Override
			public void actionPerformed(ActionEvent e) {				
        		modeFreeForeign();
			}
        };
        addAction(foreignZip);
	}
	
	public void modeSelectCH() {
		switchLayout.show(comboBox);
	}

	public void modeFreeCH() {
		switchLayout.show(horizontalLayoutSwiss);
	}

	public void modeFreeForeign() {
		switchLayout.show(horizontalLayoutForeign);
	}
	
	@Override
	public ZipTown getObject() {
		ZipTown zipTown = super.getObject();
		if (zipTown == null) {
			zipTown = new ZipTown();
		}
		
		zipTown.clear();
		if (switchLayout.getShownComponent() == comboBox) {
			if (comboBox.getSelectedObject() != null) {
				Plz plz = (Plz) comboBox.getSelectedObject();
				if (plz.postleitzahl > 0) zipTown.swissZipCode = Integer.toString(plz.postleitzahl);
				if (plz.zusatzziffern > 0) zipTown.swissZipCodeAddOn = Integer.toString(plz.zusatzziffern);
				if (plz.onrp > 0) zipTown.swissZipCodeId = Integer.toString(plz.onrp);
				zipTown.town = plz.ortsbezeichnung;
			}
		} else if (switchLayout.getShownComponent() == horizontalLayoutSwiss) {
			
		} else if (switchLayout.getShownComponent() == horizontalLayoutForeign) {
			zipTown.foreignZipCode = textFieldZipForeign.getText();
			zipTown.town = textFieldTownForeign.getText();
		}

		return zipTown;
//			toObject.town = actualPlz.ortsbezeichnung;
//			toObject.swissZipCode = actualPlz.postleitzahl > 0 ? "" + actualPlz.postleitzahl : null;
//			toObject.swissZipCodeAddOn = actualPlz.zusatzziffern > 0 ? "" + actualPlz.zusatzziffern : null;
//			toObject.swissZipCodeId = actualPlz.onrp > 0 ? "" + actualPlz.onrp : null;
//		} else {
//			toObject.town = textFieldTown.getText();
//			toObject.foreignZipCode = textFieldZip.getText();
//		}
	}

	@Override
	protected void display(ZipTown zipTown) {
		if (zipTown == null) {
			modeSelectCH();
			return;
		}
		
		if (!zipTown.isSwiss()) {
			modeFreeForeign();
			textFieldZipSwiss.setText(zipTown.foreignZipCode);
			textFieldTownSwiss.setText(zipTown.town);
		} else  {
			Plz plz = new Plz();
			plz.postleitzahl = IntegerUtils.intValue(zipTown.swissZipCode);
			plz.zusatzziffern = IntegerUtils.intValue(zipTown.swissZipCodeAddOn);
			plz.onrp = IntegerUtils.intValue(zipTown.swissZipCodeId);
			plz.ortsbezeichnung = zipTown.town;
			int index = PlzImport.getInstance().getZipCodes().indexOf(plz);
			if (index >= 0) {
				comboBox.setSelectedObject(plz);
				modeSelectCH();
			} else {
				modeFreeCH();
				textFieldZipSwiss.setText(zipTown.swissZipCode);
				textFieldTownSwiss.setText(zipTown.town);
			}
		}
	}
	
	
	//

	public void setForeign(boolean foreign) {
		if (foreign) {
			modeFreeForeign();
		} else {
			modeSelectCH();
		}
		fireChange();
	}

	@Override
	public void fillWithDemoData() {
		int index = (int)(Math.random() * (double)PlzImport.getInstance().getZipCodes().size());
		Plz plz = PlzImport.getInstance().getZipCodes().get(index);

		ZipTown zipTown = getObject();
		if (zipTown == null) {
			// TODO ist das wirklich nötig?
			zipTown = new ZipTown();
		}
		zipTown.town = plz.ortsbezeichnung;
		zipTown.swissZipCode = "" + plz.postleitzahl;
		zipTown.swissZipCodeAddOn = "" + plz.zusatzziffern;
		zipTown.swissZipCodeId = "" + plz.onrp;
		
		setObject(zipTown);
	}

	@Override
	public FormVisual<ZipTown> createFormPanel() {
		return new ZipTownFreePanel();
	}

}
