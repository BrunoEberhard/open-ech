package ch.openech.client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import ch.openech.dm.code.CodeWithOther;
import ch.openech.mj.db.model.Code;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.toolkit.TextField.TextFieldFilter;
import ch.openech.mj.util.StringUtils;

/**
 * Special CodeField in the case where there is a Code Attribute
 * and additionally a Other - Attribute. Built for eCH 46.
 * 
 * Ignores CodeFree oder CodeClear of the preferences because its given
 * by data model that it is allowed to enter a free value
 * 
 */
public class CodeWithOtherField extends AbstractEditField<CodeWithOther> {
	private final Code code;
	private CodeWithOther codeWithOther;
	
	private final SwitchLayout switchLayout;
	private final ComboBox comboBox;
	private final TextField textField;
	private final TextField textFieldUnknown;
	
	public CodeWithOtherField(Object key, Code code) {
		super(key);
		this.code = code;
		
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		comboBox.setObjects(code.getTexts());
		
		// TODO da stimmt die Size wohl nicht ?
		
		textField = ClientToolkit.getToolkit().createTextField(listener(), new CodeTextFieldFilter(code.getSize()));

		textFieldUnknown = ClientToolkit.getToolkit().createReadOnlyTextField();
		textFieldUnknown.setText(code.getUnknownText());
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout(); // comboBox, textField
		switchLayout.show(comboBox);

		setDefault();
		createMenu();
	}
	
	@Override
	public IComponent getComponent0() {
		return switchLayout;
	}

	private void createMenu() {
		Action select = new AbstractAction("Auswahl " + code.getDisplayName()) {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeChoice();
			}
		};
		addAction(select);

		Action unbekannt = new AbstractAction(code.getDisplayName() + " entfernen") {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeUnknown();
			}
		};
		addAction(unbekannt);

		Action freeEntry = new AbstractAction("Freie Eingabe") {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeFreeEntry();
			}
		};
		addAction(freeEntry);
	}
	
	private void modeChoice() {
		switchLayout.show(comboBox);
		int index = code.indexOf(codeWithOther.code);
		if (index >= 0) {
			comboBox.setSelectedObject(codeWithOther.code);
		} else {
			setDefault();
		}
		comboBox.requestFocus();
	}
	
	private void modeUnknown() {
		switchLayout.show(textFieldUnknown);
	}

	private void modeFreeEntry() {
		switchLayout.show(textField);
		textField.setText(null);
		textField.requestFocus();
	}
	
	private void setDefault() {
    	codeWithOther.code = code.getDefault();
    	codeWithOther.other = null;
    	update();
	}
	
	@Override
	public CodeWithOther getObject() {
		if (switchLayout.getShownComponent() == comboBox) {
			codeWithOther.code = (String) comboBox.getSelectedObject();
			codeWithOther.other = null;
		} else if (switchLayout.getShownComponent() == textField) {
			codeWithOther.code = null;
			codeWithOther.other = textField.getText();
		} else {
			codeWithOther.code = null;
			codeWithOther.other = null;
		}
		return codeWithOther;
	}

	@Override
	public void setObject(CodeWithOther codeWithOther) {
		this.codeWithOther = codeWithOther;
		update();
	}
	
	private void update() {
		int index = code.indexOf(codeWithOther.code);
		if (index >= 0) {
			switchLayout.show(comboBox);
			comboBox.setSelectedObject(codeWithOther.code);
		} else {
			switchLayout.show(textField);
			if (!StringUtils.isEmpty(codeWithOther.code)) {
				modeUnknown();
			} else {
				textField.setText(codeWithOther.other);
				modeFreeEntry();
			}
		}
	}
	
	private static class CodeTextFieldFilter implements TextFieldFilter {
		private int limit;

		public CodeTextFieldFilter(int limit) {
			this.limit = limit;
		}

		@Override
		public String filter(IComponent textField, String str) {
			if (str == null)
				return null;

			if (str.length() <= limit) {
				return str;
			} else {
				showBubble(textField, "Eingabe auf " + limit + " Zeichen beschränkt");
				return str.substring(0, limit);
			}
		}
	}
	
}
