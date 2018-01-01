package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.REALESTATE;

import java.util.List;

import org.minimalj.model.ViewUtil;
import org.minimalj.repository.sql.EmptyObjects;

import ch.openech.model.XmlConstants;
import ch.openech.model.common.NamedId;
import ch.openech.model.common.NamedMetaData;
import ch.openech.model.estate.Building;
import ch.openech.model.estate.ConstructionProject;
import ch.openech.model.estate.ConstructionProject.ConstructionLocalisation;
import ch.openech.model.estate.Realestate;
import ch.openech.model.estate.Realestate.RealestateIdentification;

public class WriterEch0129 {

	public final String URI;
	
	public final WriterEch0007 ech7;
	public final WriterEch0008 ech8;
	
	public WriterEch0129(EchSchema context) {
		URI = context.getNamespaceURI(129);
		ech7 = new WriterEch0007(context);
		ech8 = new WriterEch0008(context);
	}
	
	public void realestate(WriterElement parent, Realestate realestate) throws Exception {
		WriterElement writer = parent.create(URI, REALESTATE);
		RealestateIdentification identification = ViewUtil.view(realestate, new RealestateIdentification());
		realestateIdentification(writer, identification);
		writer.values(realestate, XmlConstants.AUTHORITY);
		writer.values(realestate, XmlConstants.DATE);
		writer.values(realestate, XmlConstants.REALESTATE_TYPE);
		writer.values(realestate, "cantonalSubKind");
		writer.values(realestate, XmlConstants.STATUS);
		writer.values(realestate, XmlConstants.MUTNUMBER);
		writer.values(realestate, "identDN");
		writer.values(realestate, XmlConstants.SQUARE_MEASURE);
		// TODO realestateIncomplete, coordinates;
		namedMetaData(writer, realestate.namedMetaData);
	}
	
	public void realestateIdentification(WriterElement parent, RealestateIdentification identification) throws Exception {
		WriterElement writer = parent.create(URI, "realestateIdentification");
		writer.values(identification);
	}
	
	public void building(WriterElement parent, Building building) throws Exception {
		WriterElement writer = parent.create(URI, REALESTATE);
		buildingIdentification(writer, building);
		writer.values(building, XmlConstants.AUTHORITY);
		writer.values(building, XmlConstants.DATE);
		writer.values(building, XmlConstants.REALESTATE_TYPE);
		writer.values(building, "cantonalSubKind");
		writer.values(building, XmlConstants.STATUS);
		writer.values(building, XmlConstants.MUTNUMBER);
		writer.values(building, "identDN");
		writer.values(building, XmlConstants.SQUARE_MEASURE);
		// TODO buildingIncomplete, coordinates;
		namedMetaData(writer, building.namedMetaData);
	}
	
	public void buildingIdentification(WriterElement parent, Building identification) throws Exception {
		WriterElement writer = parent.create(URI, "buildingIdentification");
		switch (identification.identificationType()) {
		case EGID:
			writer.values(identification, XmlConstants._E_G_I_D);
			break;
		case ADDRESS:
			writer.values(identification, XmlConstants.STREET, XmlConstants.HOUSE_NUMBER, XmlConstants.ZIP_CODE, XmlConstants.NAME_OF_BUILDING);
			break;
		case EGRID:
			writer.values(identification, XmlConstants._E_G_R_I_D, "officialBuildingNo");
			break;	
		case CADASTER:
			writer.values(identification, XmlConstants.CADASTER_AREA_NUMBER, XmlConstants.NUMBER, XmlConstants.REALESTATE_TYPE, "officialBuildingNo");	
			break;			
		default:
			break;
		}
	}
	
	public void constructionProject(WriterElement parent, ConstructionProject project) throws Exception {
		WriterElement writer = parent.create(URI, XmlConstants.CONSTRUCTION_PROJECT);
		constructionProjectIdentification(writer, project);
		writer.values(project, XmlConstants.NEW_BUILDINGS_FOR_RESIDENTIAL_PURPOSE_COMPLETED,
				XmlConstants.NEW_BUILDINGS_FOR_RESIDENTIAL_PURPOSE_TOTAL,
				XmlConstants.NEW_BUILDINGS_WITHOUT_RESIDENTIAL_PURPOSE_COMPLETED,
				XmlConstants.NEW_BUILDINGS_WITHOUT_RESIDENTIAL_PURPOSE_TOTAL,
				XmlConstants.NEW_DWELLINGS_COMPLETED,
				XmlConstants.NEW_DWELLINGS_TOTAL,
				XmlConstants.TYPE_OF_CONSTRUCTION_PROJECT);
		constructionLocalisation(writer, project.constructionLocalisation);
		writer.values(project, 
				"permitExpirationDate", "permitIssueDate", "announcementDate", "declinationDate", "startDate",
				"completionDate", "suspensionDate", "withdrawlDate");
		// TODO rest
		
	}
	
	public void constructionProjectIdentification(WriterElement parent, ConstructionProject identification) throws Exception {
		WriterElement writer = parent.create(URI, "constructionProjectIdentification");
		namedId(writer, identification.localId, XmlConstants.LOCAL_I_D);
		writer.values(identification, "EPROID", XmlConstants.OFFICIAL_CONSTRUCTION_PROJECT_FILE_NO, XmlConstants.EXTENSION_OF_OFFICIAL_CONSTRUCTION_PROJECT_FILE_NO);
	}
	
	public void constructionLocalisation(WriterElement parent, ConstructionLocalisation constructionLocalisation) throws Exception {
		WriterElement writer = parent.create(URI, XmlConstants.CONSTRUCTION_LOCALISATION);
		if (!EmptyObjects.isEmpty(constructionLocalisation.municipality)) {
			ech7.municipality(writer, XmlConstants.MUNICIPALITY, constructionLocalisation.municipality);
		} else if (!EmptyObjects.isEmpty(constructionLocalisation.canton)) {
			writer.text(XmlConstants.CANTON, constructionLocalisation.canton.id);
		} else if (!EmptyObjects.isEmpty(constructionLocalisation.country)) {
			ech8.country(writer, XmlConstants.COUNTRY, constructionLocalisation.country);
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
