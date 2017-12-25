package ch.openech.model.estate;

import java.util.List;

import org.minimalj.model.View;

import ch.openech.model.common.NamedId;

public class PlanningPermissionApplicationIdentification implements View<PlanningPermissionApplication> {

	public List<NamedId> localID;
	public List<NamedId> otherID;
	
}
