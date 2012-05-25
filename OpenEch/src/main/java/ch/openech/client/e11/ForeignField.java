package ch.openech.client.e11;

import java.util.List;

import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Nationality;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchSchema;

public class ForeignField extends ObjectFlowField<Foreign> implements Validatable, DependingOnFieldAbove<Nationality> {
	private final EchSchema echSchema;
	private boolean swiss = true;
	
	public ForeignField(Object key, EchSchema echSchema, boolean editable) {
		super(key, editable);
		this.echSchema = echSchema;
	}

	@Override
	protected void show(Foreign foreign) {
		if (!foreign.isEmpty()) {
			addHtml(foreign.toHtml());
		}
	}

	@Override
	protected void showActions() {
		if (!swiss) {
			addAction(new ObjectFieldEditor());
		}
	}
	
	@Override
	public IForm<Foreign> createFormPanel() {
		return new ForeignPanel(echSchema);
	}

	@Override
	public void validate(List<ValidationMessage> resultList) {
		if (!swiss) {
			Foreign foreign = getObject();
			if (StringUtils.isBlank(foreign.residencePermit)) {
				resultList.add(new ValidationMessage(getName(), "Ausländerkategorie fehlt"));
			}
		}
	}

	@Override
	public String getNameOfDependedField() {
		return "nationality";
	}

	@Override
	public void setDependedField(EditField<Nationality> field) {
		Nationality nationality = field.getObject();
		swiss = nationality.isSwiss();
		setEnabled(!swiss);
		fireObjectChange();
	}

}
