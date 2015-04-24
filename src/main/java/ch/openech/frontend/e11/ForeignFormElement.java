package ch.openech.frontend.e11;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.model.properties.PropertyInterface;

import  ch.openech.model.person.Foreign;
import ch.openech.xml.write.EchSchema;

public class ForeignFormElement extends ObjectPanelFormElement<Foreign> {
	private final EchSchema echSchema;
	
	public ForeignFormElement(PropertyInterface property, EchSchema echSchema, boolean editable) {
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
	public Form<Foreign> createFormPanel() {
		return new ForeignPanel(echSchema);
	}

}
