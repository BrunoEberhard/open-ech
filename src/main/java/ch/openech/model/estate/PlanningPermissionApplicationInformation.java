package ch.openech.model.estate;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.annotation.NotEmpty;

import ch.openech.model.organisation.OrganisationIdentification;

public class PlanningPermissionApplicationInformation {

	@NotEmpty
	public PlanningPermissionApplication planningPermissionApplication;
	
	//	@NotEmpty
	//	public Dossier dossier;

	public List<RelationshipToPerson> relationshipToPerson = new ArrayList<>();
	
	@NotEmpty
	public OrganisationIdentification buildingAuthority;
	
	public List<OrganisationIdentification> specialistDepartment = new ArrayList<>();
	
}
