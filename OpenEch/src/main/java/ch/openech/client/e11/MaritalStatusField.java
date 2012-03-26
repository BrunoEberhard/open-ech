package ch.openech.client.e11;

import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.dm.code.EchCodes;
import ch.openech.dm.person.MaritalStatus;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.fields.CodeEditField;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.edit.fields.TextFormField;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.HorizontalLayout;

// TODO Das MaritalStatus-Field sollte in zwei Felder mit DependingOn aufgeteilt werden.
// das createHorizontalLayout(code, date) mit Namenlosen Feldern ist ein Murks
public class MaritalStatusField extends AbstractEditField<MaritalStatus> implements Validatable, DemoEnabled {
	private final boolean editable;
	private final FormField<String> code;
	private final DateField date;
	private final HorizontalLayout horizontalLayout;
	private DateEnablerChangeListener dateEnablerChangeListener;
	private MaritalStatus maritalStatus;
	
	public MaritalStatusField(Object key, boolean editable) {
		super(key, editable);
		
		this.editable = editable;
		code = editable ? new CodeEditField(key, EchCodes.maritalStatus) : new TextFormField(key, EchCodes.maritalStatus);
		date = new DateField(null, DateField.NOT_REQUIRED, editable);
		
		horizontalLayout = ClientToolkit.getToolkit().createHorizontalLayout(code, date);
		
		dateEnablerChangeListener = new DateEnablerChangeListener();
		// dateEnablerChangeListener.stateChanged(null);
		
		if (editable) {
			((CodeEditField) code).setChangeListener(dateEnablerChangeListener);
		}
		date.setChangeListener(listener());
	}

	@Override
	public Object getComponent() {
		return horizontalLayout;
	}

	// 

	@Override
	public MaritalStatus getObject() {
		if (editable) {
			// Der Cast auf CodeEditField geht schief, falls kein editable, darum das if hier
			maritalStatus.maritalStatus = ((CodeEditField) code).getObject();
			maritalStatus.dateOfMaritalStatus = date.getObject();
		}
		return maritalStatus;
	}

	@Override
	public void setObject(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
		code.setObject(maritalStatus.maritalStatus);
		date.setObject(maritalStatus.dateOfMaritalStatus);
	}
	
	private void enableDate() {
		date.setEnabled(editable && !getObject().isLedig());
	}
	
	private class DateEnablerChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent changeEvent) {
			enableDate();
			listener().stateChanged(changeEvent);
		}
	}

	@Override
	public void validate(List<ValidationMessage> resultList) {
		if (!getObject().isLedig()) {
			date.validate(resultList);
		}
	}

	@Override
	public void fillWithDemoData() {
		int code = (int)(1 + 7 * Math.random());
		this.code.setObject("" + code);
		if (!getObject().isLedig()) {
			date.fillWithDemoData();
		}
	}

	@Override
	protected Indicator[] getIndicatingComponents() {
		return new Indicator[]{date};
	}

}
