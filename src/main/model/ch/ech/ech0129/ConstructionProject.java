package ch.ech.ech0129;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.503")
public class ConstructionProject {
	public static final ConstructionProject $ = Keys.of(ConstructionProject.class);

	public Object id;
	public ConstructionProjectIdentification constructionProjectIdentification;
	@Size(4)
	public Integer newBuildingsForResidentialPurposeCompleted;
	@Size(4)
	public Integer newBuildingsForResidentialPurposeTotal;
	@Size(4)
	public Integer newBuildingsWithoutResidentialPurposeCompleted;
	@Size(4)
	public Integer newBuildingsWithoutResidentialPurposeTotal;
	@Size(4)
	public Integer newDwellingsCompleted;
	@Size(4)
	public Integer newDwellingsTotal;
	public TypeOfConstructionProject typeOfConstructionProject;
	public ConstructionLocalisation constructionLocalisation;
	public TypeOfPermit typeOfPermit;
	public LocalDate permitExpirationDate;
	public LocalDate permitIssueDate;
	public LocalDate announcementDate;
	public LocalDate declinationDate;
	public LocalDate startDate;
	public LocalDate completionDate;
	public LocalDate suspensionDate;
	public LocalDate withdrawlDate;
	@NotEmpty
	@Size(6)
	public Integer constructionSurveyDeptNo;
	@Size(12)
	public Integer totalCostsOfProject;
	@NotEmpty
	public ProjectStatus status;
	public TypeOfClient typeOfClient;
	public TypeOfConstruction typeOfConstruction;
	@NotEmpty
	@Size(1000)
	public String description;
	@Size(3)
	public Integer durationOfConstructionPhase;
	public List<PersonOrOrganisation> planningPermissionApplicant;
	public PersonOrOrganisation planningPermissionApplicantDeputy;
	public PersonOrOrganisation author;
}