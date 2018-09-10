package ch.openech.frontend.estate;

import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ListFormElement;
import org.minimalj.model.Keys;

import ch.openech.model.estate.Building;

public class BuildingFormElement extends ListFormElement<Building> {

	public BuildingFormElement(List<Building> key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected Form<Building> createForm(boolean edit) {
		return new BuildingTablePage.BuildingForm(edit);
	}
	
}