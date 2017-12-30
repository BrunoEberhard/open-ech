package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.REALESTATE;

import java.util.List;

import org.minimalj.model.ViewUtil;

import ch.openech.model.XmlConstants;
import ch.openech.model.common.NamedId;
import ch.openech.model.common.NamedMetaData;
import ch.openech.model.estate.Building;
import ch.openech.model.estate.Realestate;
import ch.openech.model.estate.Realestate.RealestateIdentification;

public class WriterEch0129 {

	public final String URI;
	
	public WriterEch0129(EchSchema context) {
		URI = context.getNamespaceURI(129);
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
