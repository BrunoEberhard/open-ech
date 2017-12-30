package ch.openech.frontend.estate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.EnumUtils;

import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.estate.BuildingDate;
import ch.openech.model.estate.PeriodOfConstruction;

public class BuildingDateFormElement extends AbstractFormElement<BuildingDate> {

	private final Input<String> textField;
	private final LinkedHashMap<PeriodOfConstruction, String> periods = new LinkedHashMap<>();
	private final Map<String, PeriodOfConstruction> names = new HashMap<>();
	private final List<String> nameList = new ArrayList<>();
	
	public BuildingDateFormElement(BuildingDate key, boolean editable) {
		super(key);
		initializePeriods();
		if (editable) {
			textField = Frontend.getInstance().createTextField(13, null, new BuildingDateSuggestion(), listener());
		} else {
			textField = Frontend.getInstance().createReadOnlyTextField();
		}
	}
	
	private void initializePeriods() {
		List<PeriodOfConstruction> values = EnumUtils.valueList(PeriodOfConstruction.class);
		for (PeriodOfConstruction v : values) {
			String name = EnumUtils.getText(v);
			periods.put(v, name);
			names.put(name, v);
			nameList.add(name);
		}
	}

	@Override
	public void setValue(BuildingDate buildingDate) {
		if (buildingDate != null && buildingDate.date != null && buildingDate.date.value != null) {
			textField.setValue(buildingDate.date.value);
		} else if (buildingDate != null && buildingDate.periodOfConstruction != null) {
			textField.setValue(periods.get(buildingDate.periodOfConstruction));
		} else {
			textField.setValue(null);
		}
	}

	@Override
	public BuildingDate getValue() {
		BuildingDate buildingDate = new BuildingDate();
		String text = textField.getValue();
		if (names.containsKey(text)) {
			buildingDate.periodOfConstruction = names.get(text);
		} else {
			buildingDate.date = new DatePartiallyKnown();
			buildingDate.date.value = text;
		}
		return buildingDate;
	}

	@Override
	public IComponent getComponent() {
		return textField;
	}

	private class BuildingDateSuggestion implements Search<String> {

		@Override
		public List<String> search(String query) {
			return nameList;
		}
	}
}
