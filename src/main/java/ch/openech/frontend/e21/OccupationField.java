package ch.openech.frontend.e21;

import java.util.List;

import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.person.Occupation;
import ch.openech.xml.write.EchSchema;

public class OccupationField extends ObjectFlowField<List<Occupation>> {
	
	private final EchSchema echSchema;
	
	public OccupationField(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}

	public class AddOccupationEditor extends ObjectFieldPartEditor<Occupation> {

		@Override
		protected Occupation getPart(List<Occupation> object) {
			return new Occupation(echSchema);
		}

		@Override
		protected void setPart(List<Occupation> object, Occupation p) {
			List<Occupation> occupations = OccupationField.this.getObject();
			occupations.add(p);
			fireObjectChange();
		}
		
		@Override
		public Form<Occupation> createForm() {
			return new OccupationPanel(echSchema);
		}
	}
	
	private class RemoveOccupationAction extends Action {
		private final Occupation occupation;
		
		private RemoveOccupationAction(Occupation occupation) {
			this.occupation = occupation;
		}
		
		@Override
		public void action() {
			getObject().remove(occupation);
			fireObjectChange();
		}
	}
	
	@Override
	public Form<List<Occupation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(List<Occupation> object) {
		for (Occupation occupation : object) {
			addText(occupation.toHtml());
			if (isEditable()) {
				addAction(new RemoveOccupationAction(occupation));
				addGap();
			}
		}
		if (isEditable()) {
			addAction(new AddOccupationEditor());
		}
	}

}
