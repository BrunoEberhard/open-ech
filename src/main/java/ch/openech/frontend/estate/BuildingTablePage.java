package ch.openech.frontend.estate;

import static ch.openech.model.estate.Building.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.repository.query.By;

import ch.openech.frontend.ewk.event.DatePartiallyKnownFormElement;
import ch.openech.model.estate.Building;

public class BuildingTablePage extends SimpleTableEditorPage<Building> {

	public static final Object[] COLUMNS = new Object[] { $.EGID, $.getStreetAndHouseNumber(),
			$.municipality.municipalityName };

	public BuildingTablePage() {
		super(COLUMNS);
	}

	@Override
	protected Form<Building> createForm(boolean editable, boolean newObject) {
		return new BuildingForm(editable);
	}

	@Override
	protected List<Building> load() {
		return Backend.find(Building.class, By.ALL);
	}

	public static class BuildingForm extends Form<Building> {

		public BuildingForm(boolean editable) {
			super(editable, 4);
			addTitle("Identifikation");
			line($.EGID, $.identificationType);
			line($.street, $.houseNumber, $.zipCode, $.nameOfBuilding);
			line($.EGRID, $.cadasterAreaNumber, $.number, $.realestateType);
			line($.officialBuildingNo);
			line($.municipality, new NamedIdFormElement($.localID, editable), new NamedIdFormElement($.otherID, editable));
			addTitle("Beschreibung");
			line($.name, new BuildingDateFormElement($.dateOfConstruction, editable), new BuildingDateFormElement($.dateOfRenovation, editable), new DatePartiallyKnownFormElement($.dateOfDemolition, editable));
			line($.numberOfFloors, $.numberOfSeparateHabitableRooms, $.surfaceAreaOfBuilding, $.subSurfaceAreaOfBuilding);
			line($.surfaceAreaOfBuildingSignaleObject, $.buildingCategory, $.status); // missing buildingClass
			line($.civilDefenseShelter, $.quartersCode);
			line($.energyRelevantSurface, $.volume.volume, $.volume.informationSource, $.volume.norm);
			line(new BuildingEntranceFormElement($.buildingEntrance, editable), new ThermotechnicalDeviceFormElement($.thermotechnicalDevice, editable));
			addTitle("Wohnungen");
			line(new DwellingFormElement($.dwelling, editable));
			// building entrance
			// addTitle("Meta Data");
			// line(new NamedMetaDataFormElement($.namedMetaData, editable));
		}

	}
}
