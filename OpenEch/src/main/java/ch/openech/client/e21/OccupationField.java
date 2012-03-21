package ch.openech.client.e21;

import java.awt.event.ActionEvent;
import java.util.List;

import ch.openech.dm.person.Occupation;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.VisualList;

public class OccupationField extends ObjectField<List<Occupation>> implements Indicator {
	private VisualList list;
	
	public OccupationField(Object key, boolean editable) {
		super(key);
		
		list = ClientToolkit.getToolkit().createVisualList();
		
		if (editable) {
			addAction(new AddOccupationEditor());
			addAction(new RemoveOccupationAction());
		}
	}
	
	@Override
	protected IComponent getComponent0() {
		return list;
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
		@Override
		public void actionPerformed(ActionEvent e) {
			Object selectedObject = list.getSelectedObject();
			if (selectedObject != null) {
				getObject().remove(selectedObject);
				fireObjectChange();
			}
			
//			int[] selectedIndices = listOccupation.getSelectedIndices();
//			if (selectedIndices.length > 0) {
//				List<Occupation> selectedValues = new ArrayList<Occupation>(selectedIndices.length);
//				for (int index : selectedIndices) {
//					selectedValues.add(getObject().get(index));
//				}
//				for (Occupation occupation : selectedValues) {
//					getObject().remove(occupation);
//				}
//				fireChange();
//			}
		}
	}
	
	@Override
	public FormVisual<List<Occupation>> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void display(List<Occupation> object) {
		list.setObjects(object);
	}
}
