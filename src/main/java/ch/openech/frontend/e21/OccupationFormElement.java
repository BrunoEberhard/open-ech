package ch.openech.frontend.e21;

import java.util.List;

import org.minimalj.frontend.editor.EditorAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.frontend.toolkit.Action;
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
		protected Occupation newInstance() {
			return new Occupation(echSchema);
		}

		@Override
		public Form<Occupation> createForm() {
			return new OccupationPanel(echSchema);
		}

		@Override
		protected void addEntry(Occupation occupation) {
			List<Occupation> occupations = OccupationFormElement.this.getValue();
			occupations.add(occupation);
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
	public Form<List<Occupation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void showEntry(Occupation occupation) {
		add(occupation, new RemoveOccupationAction(occupation));
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new EditorAction(new AddOccupationEditor()) };
	}
	
}
