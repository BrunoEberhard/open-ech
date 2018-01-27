package ch.openech.model.estate;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

public class PlanningPermissionApplicationEvent {

	public static enum EventType {
		submit, file_subsequently, applicant_request, withdraw_planning_permission_application, claim, task, notice_ruling, status_notification, close_dossier, archive_dossier, notice_involved_party, notice_kind_of_proceedings, change_contact, accompanying_report, change_Responsibility;
	}
	
	public static class BaseDelivery {
		public static final BaseDelivery $ = Keys.of(BaseDelivery.class);

		@NotEmpty
		public List<PlanningPermissionApplicationInformation> applications = new ArrayList<>();
	}
	
	
	public static class SubmitPlanningPermissionApplication {
		public static final SubmitPlanningPermissionApplication $ = Keys.of(SubmitPlanningPermissionApplication.class);

		public static enum SubmitEventType {
			submit, file_subsequently;
		}
		
		@NotEmpty
		public SubmitEventType eventType;
		@NotEmpty
		public PlanningPermissionApplication planningPermissionApplication;
		
		public List<RelationshipToPerson> relationshipToPerson = new ArrayList<>();
	}
	
	public static class ChangeContact {
		public static final ChangeContact $ = Keys.of(ChangeContact.class);

		public final EventType eventType = EventType.change_contact;
		@NotEmpty
		public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
		public Directive directive;
		public List<RelationshipToPerson> relationshipToPerson = new ArrayList<>();
	}
	
	/*
	 * TODO restliche Events
	public static class Request {
		public static final Request $ = Keys.of(Request.class);

		@NotEmpty
		public EventType eventType;
		@NotEmpty
		public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
		@NotEmpty
		public Directive directive;
		public List<Document> document = new ArrayList<>();
		public Dossier dossier;
	}
	*/
}
