package ch.openech.client.e11;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.person.MaritalStatus;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Separation;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

public class SeparationField extends ObjectField<Separation> implements DependingOnFieldAbove<MaritalStatus> {

	private final TextField textField;
	private Action separationCancelationAction;
	
	public SeparationField(String name, boolean editable) {
		super(name);
		
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		
		if (editable) {
			addAction(new SeparationCancelationEditor());
			addAction(new SeperationRemoveAction());
		}
	}
	
	@Override
	protected IComponent getComponent0() {
		return textField;
	}

	@Override
	public FormVisual<Separation> createFormPanel() {
		return new SeparationPanel();
	}

	// Eingabe Auflösung Partnerschaft
	public final class SeparationCancelationEditor extends ObjectFieldEditor {
		@Override
		public FormVisual<Separation> createForm() {
			AbstractFormVisual<Separation> form = new EchFormPanel<Separation>(Separation.class);
			form.line(Separation.SEPARATION.cancelationReason);
			return form;
		}
	}

	private final class SeperationRemoveAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			Separation separation = getObject();
			if (separation != null) {
				separation.cancelationReason = null;
				separation.dateOfSeparation = null;
				separation.separation = null;
				separation.separationTill = null;
			}
			setObject(separation);
		}
	}
	
	@Override
	protected void display(Separation separation) {
		StringBuilder s = new StringBuilder();
		if (!StringUtils.isBlank(separation.separation)) {
			String text = EchCodes.separation.getText(separation.separation);
			if (StringUtils.isBlank(text)) {
				text = "Trennung Code " + separation;
			}
			s.append(text);
			s.append(", ");
		}
		if (!StringUtils.isBlank(separation.dateOfSeparation)) {
			s.append(DateUtils.formatCH(separation.dateOfSeparation));
			s.append(", ");
		}
		if (!StringUtils.isBlank(separation.separationTill)) {
			s.append("bis ");
			s.append(DateUtils.formatCH(separation.separationTill));
			s.append(", ");
		}
		if (!StringUtils.isBlank(separation.cancelationReason)) {
			String text = EchCodes.partnerShipAbolition.getText(separation.cancelationReason);
			if (StringUtils.isBlank(text)) {
				text = "Code " + separation.cancelationReason;
			}
			s.append("Auflösungsgrund: ");
			s.append(text);
		}
		if (s.length() >= 2) {
			if (s.charAt(s.length()-1) == ' ') s.deleteCharAt(s.length()-1);
			if (s.charAt(s.length()-1) == ',') s.deleteCharAt(s.length()-1);
		}

		textField.setText(s.toString());
	}	
		
	// DependingOnFieldAbove
	
	@Override
	public String getNameOfDependedField() {
		return Constants.getConstant(Person.PERSON.maritalStatus);
	}
	
	@Override
	public void setDependedField(EditField<MaritalStatus> field) {
		MaritalStatus maritalStatus = field.getObject();
		boolean verheiratet = maritalStatus.isVerheiratet();
		boolean aufgeloest = maritalStatus.isPartnerschaftAufgeloest();
		
		// TODO
		// if (objectEditorAction != null) objectEditorAction.setEnabled(verheiratet);
		if (separationCancelationAction != null) separationCancelationAction.setEnabled(aufgeloest);
		
		if (verheiratet || aufgeloest) {
			textField.setEnabled(true);
		} else {
			textField.setText(null);
			textField.setEnabled(false);
		}
	}

}
