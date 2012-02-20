package ch.openech.client;

import ch.openech.dm.code.CodeWithOther;
import ch.openech.mj.db.model.Code;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponentDelegate;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;

/**
 * Special CodeField in the case where there is a Code Attribute
 * and additionally a Other - Attribute. Built for eCH 46.
 * 
 */
public class CodeWithOtherReadOnlyField implements IComponentDelegate, FormField<CodeWithOther> {
	private final String name;
	private final Code code;
	private CodeWithOther codeWithOther;
	
	private TextField textField;

	public CodeWithOtherReadOnlyField(Object key, Code code) {
		this.name = Constants.getConstant(key);
		this.code = code;
		
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getComponent() {
		return textField;
	}

	public CodeWithOther getObject() {
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
			textField.setText(code.getText(index));
		} else if (!StringUtils.isEmpty(codeWithOther.code)) {
			textField.setText(code.getUnknownText());
		} else if (!StringUtils.isEmpty(codeWithOther.other)) {
			textField.setText(codeWithOther.other);
		} else {
			textField.setText(code.getUnknownText());
		}
	}
	
}
