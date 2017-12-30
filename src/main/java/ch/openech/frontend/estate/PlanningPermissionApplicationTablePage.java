package ch.openech.frontend.estate;

import static ch.openech.model.estate.PlanningPermissionApplication.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.TablePage.SimpleTablePageWithDetail;
import org.minimalj.repository.query.By;

import ch.openech.frontend.estate.event.SubmitPlanningPermissionApplicationEditor;
import ch.openech.model.estate.PlanningPermissionApplication;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0211;

public class PlanningPermissionApplicationTablePage extends SimpleTablePageWithDetail<PlanningPermissionApplication> {

	public static final Object[] COLUMNS = new Object[] {
		$.applicationType,
		$.description,
		$.locationAddress.street, $.locationAddress.houseNumber.houseNumber,
		$.locationAddress.locality
	};
	
	public PlanningPermissionApplicationTablePage() {
		super(COLUMNS);
		
		EchSchema schema = EchSchema.getNamespaceContext(211, "1.0");
		WriterEch0211 writer = new WriterEch0211(schema);
	}
	
//	@Override
//	public List<Action> getActions() {
//		return actionGroup.getItems();
//	}
//	
//	@Override
//	public void selectionChanged(List<PlanningPermissionApplication> selectedObjects) {
//		for (Action action : actionGroup.getItems()) {
//			((PlanningPermissionApplicationEditor<?>) action).setPlanningPermissionApplication(selectedObjects);
//		}
//	}
	
	@Override
	protected Form<PlanningPermissionApplication> createForm(boolean editable) {
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
			actionGroup.add(new DetailPageEditor());
			actionGroup.addSeparator();
			actionGroup.add(new SubmitPlanningPermissionApplicationEditor(detail));		
		}
	
		@Override
		public List<Action> getActions() {
			return actionGroup.getItems();
		}
		
	}

}
