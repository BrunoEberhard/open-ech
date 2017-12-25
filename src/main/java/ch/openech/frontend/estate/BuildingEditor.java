package ch.openech.frontend.estate;

import org.minimalj.frontend.editor.Editor.SimpleEditor;
import org.minimalj.frontend.form.Form;

import ch.openech.model.estate.Building;

public class BuildingEditor extends SimpleEditor<Building> {

	@Override
	protected Building createObject() {
		return new Building();
	}

	@Override
	protected Form<Building> createForm() {
		return new BuildingForm(true);
	}

	@Override
	protected Building save(Building object) {
		return null;
	}

}
