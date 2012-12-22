package ch.openech.client.e11;

import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Nationality;
import ch.openech.mj.db.model.PropertyInterface;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.IForm;
import ch.openech.xml.write.EchSchema;

public class ForeignField extends ObjectFlowField<Foreign> implements DependingOnFieldAbove<Nationality> {
	private final EchSchema echSchema;
	private boolean swiss = true;
	
	public ForeignField(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
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
	public void valueChanged(Nationality nationality) {
		swiss = nationality.isSwiss();
		setEnabled(!swiss);
		fireObjectChange();
	}

}
