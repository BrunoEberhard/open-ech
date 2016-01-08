package ch.openech.frontend.e21;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.person.Occupation;
import ch.openech.xml.write.EchSchema;

public class OccupationFormElement extends ListFormElement<Occupation> {
	
	private final EchSchema echSchema;
	
	public OccupationFormElement(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}
	
	public class AddOccupationEditor extends AddListEntryEditor {

		@Override
		protected Occupation createObject() {
			return new Occupation(echSchema);
		}
	}

	private class RemoveOccupationAction extends Action {
		private final Occupation occupation;
		
		private RemoveOccupationAction(Occupation occupation) {
			this.occupation = occupation;
		}
		
		@Override
		public void action() {
			getValue().remove(occupation);
			handleChange();
		}
	}
	
	@Override
	public Form<Occupation> createForm(boolean edit) {
		return new OccupationPanel(echSchema);
	}

	@Override
	protected void showEntry(Occupation occupation) {
		if (isEditable()) {
			add(occupation, new RemoveOccupationAction(occupation));
		} else {
			add(occupation);
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new AddOccupationEditor() };
	}
	
}
