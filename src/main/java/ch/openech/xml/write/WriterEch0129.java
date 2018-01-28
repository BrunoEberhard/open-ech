package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;

import java.io.Writer;
import java.util.List;

import org.minimalj.model.ViewUtil;
import org.minimalj.repository.sql.EmptyObjects;

import ch.openech.model.XmlConstants;
import ch.openech.model.common.NamedId;
import ch.openech.model.common.NamedMetaData;
import ch.openech.model.estate.Building;
import ch.openech.model.estate.BuildingDate;
import ch.openech.model.estate.ConstructionProject;
import ch.openech.model.estate.ConstructionProject.ConstructionLocalisation;
import ch.openech.model.estate.Coordinates;
import ch.openech.model.estate.Coordinates.CoordinatesType;
import ch.openech.model.estate.Realestate;
import ch.openech.model.estate.Realestate.RealestateIdentification;

public class WriterEch0129 {

	public final String URI;
	
	public final WriterEch0007 ech7;
	public final WriterEch0008 ech8;

	private Writer writer;
	
	public WriterEch0129(EchSchema context) {
		URI = context.getNamespaceURI(129);
		ech7 = new WriterEch0007(context);
		ech8 = new WriterEch0008(context);
	}
	
	
	public void realestate(WriterElement parent, Realestate realestate) throws Exception {
		WriterElement writer = parent.create(URI, REALESTATE);
		RealestateIdentification identification = ViewUtil.view(realestate, new RealestateIdentification());
		realestateIdentification(writer, identification);
		writer.values(realestate, AUTHORITY);
		writer.values(realestate, DATE);
		writer.values(realestate, REALESTATE_TYPE);
		writer.values(realestate, "cantonalSubKind");
		writer.values(realestate, STATUS);
		writer.values(realestate, MUTNUMBER);
		writer.values(realestate, "identDN");
		writer.values(realestate, SQUARE_MEASURE);
		writer.values(realestate, "realestateIncomplete");
		coordinates(writer, realestate.coordinates);
		namedMetaData(writer, realestate.namedMetaData);
	}
	
	public void realestateIdentification(WriterElement parent, RealestateIdentification identification) throws Exception {
		WriterElement writer = parent.create(URI, "realestateIdentification");
		writer.values(identification, true);
	}
	
	public void building(WriterElement parent, Building building) throws Exception {
		WriterElement writer = parent.create(URI, REALESTATE);
		buildingIdentification(writer, building);
		writer.values(building, _E_G_I_D, OFFICIAL_BUILDING_NO, NAME);
		buildingDate(writer, XmlConstants.DATE_OF_CONSTRUCTION, building.dateOfConstruction);
		buildingDate(writer, XmlConstants.DATE_OF_RENOVATION, building.dateOfRenovation);
		WriterEch0044.datePartiallyKnownType(writer, URI, XmlConstants.DATE_OF_DEMOLITION, building.dateOfDemolition);
		writer.values(building, NUMBER_OF_FLOORS, NUMBER_OF_SEPARATE_HABITABLE_ROOMS, SURFACE_AREA_OF_BUILDING, SUB_SURFACE_AREA_OF_BUILDING);
		writer.text("surfaceAreaOfBuildingSignaleObject", building.surfaceAreaOfBuildingSignaleObject);
		writer.values(building, BUILDING_CATEGORY, BUILDING_CLASS, STATUS);
		coordinates(writer, building.coordinates);
		namedId(writer, building.localID, OTHER_I_D);
		writer.values(building, "civilDefenseShelter", "quartersCode");
		writer.values(building, "energyRelevantSurface", "thermoTechnicalDeviceYesNo", "volume");
		writer.values(building, "thermotechnicalDevice");
		// TODO Entrance, Person
		namedMetaData(writer, building.namedMetaData);
	}
	
	public void buildingIdentification(WriterElement parent, Building identification) throws Exception {
		WriterElement writer = parent.create(URI, "buildingIdentification");
		switch (identification.identificationType()) {
		case EGID:
			writer.values(identification, _E_G_I_D);
			break;
		case ADDRESS:
			writer.values(identification, STREET, HOUSE_NUMBER, ZIP_CODE, NAME_OF_BUILDING);
			break;
		case EGRID:
			writer.values(identification, _E_G_R_I_D, "officialBuildingNo");
			break;	
		case CADASTER:
			writer.values(identification, CADASTER_AREA_NUMBER, NUMBER, REALESTATE_TYPE, "officialBuildingNo");	
			break;			
		default:
			break;
		}
	}
	
	public void buildingDate(WriterElement parent, String tagName, BuildingDate buildingDate) throws Exception {
		if (buildingDate.periodOfConstruction != null) {
			WriterElement writer = parent.create(URI, tagName);
			writer.values(buildingDate.periodOfConstruction);
		} else {
			WriterEch0044.datePartiallyKnownType(parent, URI, tagName, buildingDate.date);
		}
	}
	
	public void constructionProject(WriterElement parent, ConstructionProject project) throws Exception {
		WriterElement writer = parent.create(URI, CONSTRUCTION_PROJECT);
		constructionProjectIdentification(writer, project);
		writer.values(project, NEW_BUILDINGS_FOR_RESIDENTIAL_PURPOSE_COMPLETED,
				NEW_BUILDINGS_FOR_RESIDENTIAL_PURPOSE_TOTAL,
				NEW_BUILDINGS_WITHOUT_RESIDENTIAL_PURPOSE_COMPLETED,
				NEW_BUILDINGS_WITHOUT_RESIDENTIAL_PURPOSE_TOTAL,
				NEW_DWELLINGS_COMPLETED,
				NEW_DWELLINGS_TOTAL,
				TYPE_OF_CONSTRUCTION_PROJECT);
		constructionLocalisation(writer, project.constructionLocalisation);
		writer.values(project, 
				"permitExpirationDate", "permitIssueDate", "announcementDate", "declinationDate", "startDate",
				"completionDate", "suspensionDate", "withdrawlDate");
		writer.values(project, 
				"constructionSurveyDeptNo", "totalCostsOfProject", "status", "typeOfClient", "typeOfConstruction",
				"description", "durationOfConstructionPhase");
		// if (project.)
		
	}
	
	public void constructionProjectIdentification(WriterElement parent, ConstructionProject identification) throws Exception {
		WriterElement writer = parent.create(URI, "constructionProjectIdentification");
		namedId(writer, identification.localId, LOCAL_I_D);
		writer.values(identification, "EPROID", OFFICIAL_CONSTRUCTION_PROJECT_FILE_NO, EXTENSION_OF_OFFICIAL_CONSTRUCTION_PROJECT_FILE_NO);
	}
	
	public void constructionLocalisation(WriterElement parent, ConstructionLocalisation constructionLocalisation) throws Exception {
		WriterElement writer = parent.create(URI, CONSTRUCTION_LOCALISATION);
		if (!EmptyObjects.isEmpty(constructionLocalisation.municipality)) {
			ech7.municipality(writer, MUNICIPALITY, constructionLocalisation.municipality);
		} else if (!EmptyObjects.isEmpty(constructionLocalisation.canton)) {
			writer.text(CANTON, constructionLocalisation.canton.id);
		} else if (!EmptyObjects.isEmpty(constructionLocalisation.country)) {
			ech8.country(writer, COUNTRY, constructionLocalisation.country);
		} 
	}
	
	public void coordinates(WriterElement parent, Coordinates coordinates) throws Exception {
		if (coordinates != null) {
			WriterElement writer = parent.create(URI, coordinates.coordinatesType.name());
			if (coordinates.coordinatesType == CoordinatesType.LV03) {
				writer.values(coordinates, "y", "x");
			} else if (coordinates.coordinatesType == CoordinatesType.LV95) {
				writer.values(coordinates, "east", "west");
			}
			writer.values(coordinates, ORIGIN_OF_COORDINATES);
		}
	}
	
	public void namedId(WriterElement parent, List<NamedId> namedIds, String name) throws Exception {
		for (NamedId namedPersonId : namedIds) {
			namedId(parent, namedPersonId, name);
		}
	}
	
	public void namedId(WriterElement parent, NamedId namedId, String name) throws Exception {
		WriterElement writer = parent.create(URI, name);
		writer.values(namedId);
	}
	
	public void namedMetaData(WriterElement parent, List<NamedMetaData> datas) throws Exception {
		for (NamedMetaData data : datas) {
			namedMetaData(parent, data);
		}
	}
	
	public void namedMetaData(WriterElement parent, NamedMetaData data) throws Exception {
		WriterElement writer = parent.create(URI, "namedMetaData");
		writer.values(data);
	}

}
