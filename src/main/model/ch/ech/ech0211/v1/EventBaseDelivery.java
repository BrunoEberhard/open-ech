package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.026")
public class EventBaseDelivery {
	public static final EventBaseDelivery $ = Keys.of(EventBaseDelivery.class);

	public Object id;
	public static class PlanningPermissionApplicationInformation {
		public static final PlanningPermissionApplicationInformation $ = Keys.of(PlanningPermissionApplicationInformation.class);

		@NotEmpty
		public PlanningPermissionApplication planningPermissionApplication;
		public List<RelationshipToPerson> relationshipToPerson;
		public final DecisionAuthority decisionAuthority = new DecisionAuthority();
		public final EntryOffice entryOffice = new EntryOffice();
		public List<SpecialistDepartment> specialistDepartment;
		public List<ch.ech.ech0147.t0.Document> document;
		public byte[] extension;
	}
	@NotEmpty
	public List<PlanningPermissionApplicationInformation> planningPermissionApplicationInformation;
}