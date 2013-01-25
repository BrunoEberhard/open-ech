package ch.openech.client.e21;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.dm.person.Occupation;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.resources.ResourceAction;
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
		public IForm<Occupation> createForm() {
			return new OccupationPanel(echSchema);
		}
	}
	
	private class RemoveOccupationAction extends ResourceAction {
		private final Occupation occupation;
		
		private RemoveOccupationAction(Occupation occupation) {
			this.occupation = occupation;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			getObject().remove(occupation);
			fireObjectChange();
		}
	}
	
	@Override
	public IForm<List<Occupation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(List<Occupation> object) {
		for (Occupation occupation : object) {
			addObject(occupation.toHtml());
			if (isEditable()) {
				addAction(new RemoveOccupationAction(occupation));
				addGap();
			}
		}
		if (isEditable()) {
			addAction(new EditorDialogAction(new AddOccupationEditor()));
		}
	}

}
