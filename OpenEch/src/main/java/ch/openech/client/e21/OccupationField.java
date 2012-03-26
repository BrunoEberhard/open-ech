package ch.openech.client.e21;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.dm.person.Occupation;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.resources.ResourceAction;

public class OccupationField extends MultiLineObjectField<List<Occupation>> implements Indicator {
	
	public OccupationField(Object key, boolean editable) {
		super(key, editable);
	}

	public class AddOccupationEditor extends ObjectFieldPartEditor<Occupation> {

		@Override
		protected Occupation getPart(List<Occupation> object) {
			return new Occupation();
		}

		@Override
		protected void setPart(List<Occupation> object, Occupation p) {
			List<Occupation> occupations = OccupationField.this.getObject();
			occupations.add(p);
			fireObjectChange();
		}
		
		@Override
		public FormVisual<Occupation> createForm() {
			return new OccupationPanel();
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
	public FormVisual<List<Occupation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void display(List<Occupation> object) {
		clearVisual();
		for (Occupation occupation : object) {
			addObject(occupation);
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
