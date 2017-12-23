package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.EVENT_SUBMIT_PLANNING_PERMISSION_APPLICATION;

import ch.openech.model.estate.PlanningPermissionApplication;

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
	
	// Elemente für alle Events
	
	private WriterElement event(String eventName) throws Exception {
		WriterElement event = delivery().create(URI, eventName);
        return event;
	}
	
	
	public String submitPlanningPermissionApplication(PlanningPermissionApplication application) throws Exception {
		WriterElement event = event(EVENT_SUBMIT_PLANNING_PERMISSION_APPLICATION);
		
        return result();
	}
	
	public static void main(String[] args) throws Exception {
		final EchSchema echSchema = EchSchema.getNamespaceContext(211, "1.0");
		final WriterEch0211 writer = new WriterEch0211(echSchema);
		System.out.println(writer.submitPlanningPermissionApplication(null));
	}
	
}
