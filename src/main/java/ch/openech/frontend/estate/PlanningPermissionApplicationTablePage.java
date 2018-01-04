package ch.openech.frontend.estate;

import static ch.openech.model.estate.PlanningPermissionApplication.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.HtmlPage;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.repository.query.By;
import org.minimalj.util.StringUtils;

import ch.openech.model.estate.PlanningPermissionApplication;
import ch.openech.model.estate.PlanningPermissionApplicationEvent.SubmitPlanningPermissionApplication;
import ch.openech.model.estate.PlanningPermissionApplicationEvent.SubmitPlanningPermissionApplication.SubmitEventType;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0211;

public class PlanningPermissionApplicationTablePage extends SimpleTableEditorPage<PlanningPermissionApplication> {

	public static final Object[] COLUMNS = new Object[] {
		$.applicationType,
		$.description,
		$.locationAddress.street, $.locationAddress.houseNumber.houseNumber,
		$.locationAddress.locality
	};
	
	public PlanningPermissionApplicationTablePage() {
		super(COLUMNS);
	}
	
	@Override
	protected Form<PlanningPermissionApplication> createForm(boolean editable, boolean newObject) {
		return new PlanningPermissionApplicationForm(editable);
	}
		
	@Override
	protected List<PlanningPermissionApplication> load() {
		return Backend.find(PlanningPermissionApplication.class, By.ALL);
	}

	@Override
	protected DetailPage createDetailPage(PlanningPermissionApplication detail) {
		return new PlanningPermissionApplicationPage(detail);
	}

	public class PlanningPermissionApplicationPage extends DetailPage {

		private ActionGroup actionGroup;
		
		public PlanningPermissionApplicationPage(PlanningPermissionApplication detail) {
			super(detail);
			actionGroup = new ActionGroup(null);
			actionGroup.add(new TableEditor());
			actionGroup.addSeparator();
			actionGroup.add(new SubmitPlanningPermissionApplicationEditor(detail));		
		}
	
		@Override
		public List<Action> getActions() {
			return actionGroup.getItems();
		}
	}
	
	// TODO remove
	public class SubmitPlanningPermissionApplicationEditor extends Action {
		
		private final PlanningPermissionApplication application;
		
		public SubmitPlanningPermissionApplicationEditor(PlanningPermissionApplication application) {
			this.application = application;
		}
		
		@Override
		public void action() {
			EchSchema schema = EchSchema.getNamespaceContext(211, "1.0");
			WriterEch0211 writer = new WriterEch0211(schema);
			String result;
			try {
				SubmitPlanningPermissionApplication object = new SubmitPlanningPermissionApplication();
				object.eventType = SubmitEventType.submit;
				object.planningPermissionApplication = application;
				
				result = "<html><pre>" + StringUtils.escapeHTML(writer.submitPlanningPermissionApplication(object)) + "</pre></html>";
				// result = writer.result();
			} catch (Exception e) {
				result = e.getMessage();
				e.printStackTrace();
			}
			HtmlPage page = new HtmlPage(result, "Xml - Output");
			Frontend.show(page);
		}
	}

}
