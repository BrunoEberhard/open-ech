package ch.openech.client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.VisualList;


public class RemoveEntriesListField<T> extends AbstractEditField<List<T>> {

	private List<T> values = new ArrayList<T>();
	private List<T> selectedValues = new ArrayList<T>();
	private final VisualList list;

	public RemoveEntriesListField(Object key) {
		super(Constants.getConstant(key));

		list = ClientToolkit.getToolkit().createVisualList();
		addAction(new RemoveSelectedObjectAction());
		addAction(new AddAllAction());
	}

	public void setValues(List<T> values) {
		this.values = values;
		selectedValues.clear();
		selectedValues.addAll(values);
		list.setObjects(selectedValues);
	}
	
	@Override
	public List<T> getObject() {
		return selectedValues;
	}

	@Override
	public void setObject(List<T> object) {
		this.selectedValues.clear();
		// Mit der for Schleife wird sichergestellt, dass die Liste die richtigen Objekte enthält
		for (T value : object) {
			selectedValues.add(value);
		}
		list.setObjects(selectedValues);
	}

	@Override
	protected IComponent getComponent0() {
		return list;
	}
	
	private class RemoveSelectedObjectAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object selectedObject = list.getSelectedObject();
			if (selectedObject != null) {
				selectedValues.remove(selectedObject);
				list.setObjects(selectedValues);
			}
		}
	}

	private class AddAllAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			selectedValues.clear();
			selectedValues.addAll(values);
			list.setObjects(selectedValues);
		}
	}

}
