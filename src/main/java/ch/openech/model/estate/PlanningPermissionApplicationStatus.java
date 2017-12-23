package ch.openech.model.estate;

import ch.openech.model.types.EchCode;

public enum PlanningPermissionApplicationStatus implements EchCode {

	SUBMITTED, IN_PROGRESS, WITHDRAWN, SUSPENDED, APPELLANT_PROCESS, OBJECTION_PENDING, DECISION_ISSUED, DECISION_LEGALLY_BINDING, BUILDING_FREEZE, UNDER_CONSTRUCTION, BUILDING_CLEARANCE;

	private final String value;

	private PlanningPermissionApplicationStatus() {
		this.value = this.name().toLowerCase().replace('_', '_');
	}

	@Override
	public String getValue() {
		return value;
	}

}
