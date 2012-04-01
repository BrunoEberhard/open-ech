package ch.openech.client.e11;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import ch.openech.dm.code.EchCodes;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Separation;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;

public class SeparationField extends ObjectFlowField<Separation> implements DependingOnFieldAbove<String> {

	private final EchNamespaceContext namespaceContext;
	private Action separationCancelationAction;
	private boolean verheiratet;
	
	public SeparationField(Object key, EchNamespaceContext namespaceContext, boolean editable) {
		super(key, editable, false);
		this.namespaceContext = namespaceContext;
	}
	
	@Override
	public FormVisual<Separation> createFormPanel() {
		return new SeparationPanel(namespaceContext);
	}

	private final class SeperationRemoveAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			Separation separation = getObject();
			if (separation != null) {
				separation.clear();
			}
			setObject(separation);
		}
	}
	
	@Override
	protected void show(Separation separation) {
		StringBuilder s = new StringBuilder();
		if (!StringUtils.isBlank(separation.separation)) {
			String text = EchCodes.separation.getText(separation.separation);
			if (StringUtils.isBlank(text)) {
				text = "Trennung Code " + separation;
			}
			s.append(text);
		}
		if (!StringUtils.isBlank(separation.dateOfSeparation)) {
			s.append(", ");
			s.append(DateUtils.formatCH(separation.dateOfSeparation));
			if (!StringUtils.isBlank(separation.separationTill)) {
				s.append(" bis ");
				s.append(DateUtils.formatCH(separation.separationTill));
			}
		}
		addObject(s.toString());
		
		if (isEditable() && verheiratet) {
			addGap();
			addAction(new ObjectFieldEditor());
			if (!StringUtils.isBlank(separation.separation)) {
				addAction(new SeperationRemoveAction());
			}
		}
	}	
		
	// DependingOnFieldAbove
	
	@Override
	public String getNameOfDependedField() {
		return Constants.getConstant(Person.PERSON.maritalStatus.maritalStatus);
	}
	
	@Override
	public void setDependedField(EditField<String> field) {
		String status = field.getObject();
		verheiratet = ch.openech.dm.code.MaritalStatus.Verheiratet.value.equals(status);
		if (!verheiratet && getObject() != null) {
			getObject().clear();
		}
		setObject(getObject());
	}
}
