package ch.openech.frontend.estate.event;

import java.util.List;

import org.minimalj.frontend.editor.Editor.SimpleEditor;

import ch.openech.model.estate.PlanningPermissionApplication;

public abstract class PlanningPermissionApplicationEditor<T> extends SimpleEditor<T> {

	protected PlanningPermissionApplication application;
	
	public void setPlanningPermissionApplication(List<PlanningPermissionApplication> selection) {
		if (selection != null && selection.size() == 1) {
			this.application = selection.get(0);
		} else {
			this.application = null;
		}
		setEnabled(application != null);
	}

}
