package ch.openech.frontend.e11;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.person.Foreign;
import ch.openech.xml.write.EchSchema;

public class ForeignFormElement extends ObjectFormElement<Foreign> {
	private final EchSchema echSchema;
	
	public ForeignFormElement(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}
	
	@Override
	protected Action[] getActions() {
		return new Action[] { getEditorAction() };
	}

	@Override
	protected void show(Foreign foreign) {
		add(foreign);
	}
	
	@Override
	public Form<Foreign> createForm() {
		return new ForeignPanel(echSchema);
	}

}
