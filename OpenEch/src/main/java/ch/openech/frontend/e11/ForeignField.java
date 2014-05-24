package ch.openech.frontend.e11;

import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.IForm;
import org.minimalj.model.PropertyInterface;

import  ch.openech.model.person.Foreign;
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