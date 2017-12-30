package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;

import ch.openech.model.estate.Building;
import ch.openech.model.estate.PlanningPermissionApplication;
import ch.openech.model.estate.PlanningPermissionApplication.Publication;
import ch.openech.model.estate.PlanningPermissionApplication.Remark;
import ch.openech.model.estate.PlanningPermissionApplication.Zone;
import ch.openech.model.estate.PlanningPermissionApplicationEvent.SubmitPlanningPermissionApplication;
import ch.openech.model.estate.RealestateInformation;

public class WriterEch0211 extends DeliveryWriter {

	public final String URI;
	
	public final WriterEch0007 ech7;
	public final WriterEch0010 ech10;
	public final WriterEch0044 ech44;
	public final WriterEch0046 ech46;
	public final WriterEch0097 ech97;
	public final WriterEch0098 ech98;
	// public final WriterEch0102 ech102;
	public final WriterEch0108 ech108;
	public final WriterEch0129 ech129;
	
	public WriterEch0211(EchSchema context) {
		super(context);
		
		URI = context.getNamespaceURI(211);
		ech7 = new WriterEch0007(context);
		ech10 = new WriterEch0010(context);
		ech46 = new WriterEch0046(context);
		ech44 = new WriterEch0044(context);
		ech97 = new WriterEch0097(context);
		ech98 = new WriterEch0098(context);
//		ech102 = new WriterEch0102(context);
		ech108 = new WriterEch0108(context);
		ech129 = new WriterEch0129(context);
	}
	
	@Override
	protected int getSchemaNumber() {
		return 211;
	}
	
	@Override
	protected String getNamespaceURI() {
		return URI;
	}
	
	// 

	public void planningPermissionApplication(WriterElement parent, PlanningPermissionApplication application) throws Exception {
		WriterElement writer = parent.create(URI, PLANNING_PERMISSION_APPLICATION);
		planningPermissionApplicationIdentification(writer, application);
		writer.values(application, DESCRIPTION, APPLICATION_TYPE);
		for (Remark remark : application.remark) {
			writer.text(REMARK, remark.token);
		}
		writer.values(application, PROCEEDING_TYPE);
		for (Publication publication: application.publication) {
			publication(writer, publication);
		}
		ech129.namedMetaData(writer, application.namedMetaData);
		ech7.municipality(writer, MUNICIPALITY, application.municipality);
		ech10.address(writer, LOCATION_ADDRESS, application.locationAddress);
		for (RealestateInformation realestateInformation : application.realestateInformation) {
			realestateInformation(writer, realestateInformation);
		}
		for (Zone zone : application.zone) {
			zone(writer, zone);
		}
		// TODO constructionProject !
		// TODO directive
		// TODO decisionRuling
		// TODO document
	}
	
	public void planningPermissionApplicationIdentification(WriterElement parent, PlanningPermissionApplication application) throws Exception {
		WriterElement writer = parent.create(URI, PLANNING_PERMISSION_APPLICATION_IDENTIFICATION);
		ech129.namedId(writer, application.localID, LOCAL_I_D);
		ech129.namedId(writer, application.otherID, OTHER_I_D);
	}

	public void publication(WriterElement parent, Publication publication) throws Exception {
		WriterElement writer = parent.create(URI, PUBLICATION);
		writer.values(writer, OFFICIAL_GAZETTE, PUBLICATION_TEXT, PUBLICATION_DATE, PUBLICATION_TILL);
	}
	
	public void zone(WriterElement parent, Zone zone) throws Exception {
		WriterElement writer = parent.create(URI, ZONE);
		writer.values(writer, ABBREVIATED_DESIGNATION, ZONE_DESIGNATION, ZONE_TYPE);
	}
		
	public void realestateInformation(WriterElement parent, RealestateInformation realestateInformation) throws Exception {
		WriterElement writer = parent.create(URI, REALESTATE_INFORMATION);
		ech129.realestate(writer, realestateInformation.realestate);
		ech7.municipality(writer, MUNICIPALITY, realestateInformation.municipality);
		for (Building building : realestateInformation.realestate.building) {
			ech129.building(writer, building);
		}
		// ech129.placeName(realestateInformation.placeName);
		// owner
	}

	
	// Elemente für alle Events
	
	private WriterElement event(String eventName) throws Exception {
		WriterElement event = delivery().create(URI, eventName);
        return event;
	}
	
	
	public String submitPlanningPermissionApplication(SubmitPlanningPermissionApplication application) throws Exception {
		WriterElement event = event(EVENT_SUBMIT_PLANNING_PERMISSION_APPLICATION);
		planningPermissionApplication(event, application.planningPermissionApplication);
        return result();
	}
	
	public static void main(String[] args) throws Exception {
		final EchSchema echSchema = EchSchema.getNamespaceContext(211, "1.0");
		final WriterEch0211 writer = new WriterEch0211(echSchema);
		System.out.println(writer.submitPlanningPermissionApplication(null));
	}
	
}
