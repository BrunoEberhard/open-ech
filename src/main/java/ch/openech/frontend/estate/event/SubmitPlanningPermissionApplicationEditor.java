package ch.openech.frontend.estate.event;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.HtmlPage;
import org.minimalj.util.StringUtils;

import ch.openech.model.estate.PlanningPermissionApplication;
import ch.openech.model.estate.PlanningPermissionApplicationEvent.SubmitPlanningPermissionApplication;
import ch.openech.model.estate.PlanningPermissionApplicationEvent.SubmitPlanningPermissionApplication.SubmitEventType;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0211;

public class SubmitPlanningPermissionApplicationEditor extends PlanningPermissionApplicationEditor<SubmitPlanningPermissionApplication> {

	private final PlanningPermissionApplication application;
	
	public SubmitPlanningPermissionApplicationEditor(PlanningPermissionApplication application) {
		this.application = application;
	}

	@Override
	protected Form<SubmitPlanningPermissionApplication> createForm() {
		Form<SubmitPlanningPermissionApplication> form = new Form<>();
		
		return form;
	}

	@Override
	protected SubmitPlanningPermissionApplication createObject() {
		EchSchema schema = EchSchema.getNamespaceContext(211, "1.0");
		WriterEch0211 writer = new WriterEch0211(schema);
		
		SubmitPlanningPermissionApplication object = new SubmitPlanningPermissionApplication();
		object.eventType = SubmitEventType.submit;
		object.planningPermissionApplication = this.application;
		return object;
	}
	
	@Override
	protected SubmitPlanningPermissionApplication save(SubmitPlanningPermissionApplication application) {
		// noch nicht speichern oder senden, nur anzeigen
		return application;
	}
	
	@Override
	protected void finished(SubmitPlanningPermissionApplication application) {
		EchSchema schema = EchSchema.getNamespaceContext(211, "1.0");
		WriterEch0211 writer = new WriterEch0211(schema);
		String result;
		try {
			result = "<html><pre>" + StringUtils.escapeHTML(writer.submitPlanningPermissionApplication(application)) + "</pre></html>";
			// result = writer.result();
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		}
		HtmlPage page = new HtmlPage(result, "Xml - Output");
		Frontend.show(page);
	}

}
