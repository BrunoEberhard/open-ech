package ch.openech.client.e11;

import ch.openech.dm.person.Foreign;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.xml.write.EchSchema;

public class ForeignField extends ObjectFlowField<Foreign> {
	private final EchSchema echSchema;
	
	public ForeignField(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}

	@Override
	protected void show(Foreign foreign) {
		if (!foreign.isEmpty()) {
			addText(foreign.toHtml());
		}
	}

	@Override
	protected void showActions() {
		addAction(new ObjectFieldEditor());
	}
	
	@Override
	public IForm<Foreign> createFormPanel() {
		return new ForeignPanel(echSchema);
	}

}
