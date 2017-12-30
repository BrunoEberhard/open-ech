package ch.openech.model.estate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.View;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;

import ch.openech.model.EchFormats;
import ch.openech.model.common.Canton;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.NamedId;

@Sizes(EchFormats.class)
public class ConstructionProject {

	// public List<NamedId> localId = new ArrayList<>();
	public String EPROID;
	public String officialConstructionProjectFileNo, extensionOfOfficialConstructionProjectFileNo;
	
	public Integer newBuildingsForResidentialPurposeCompleted;
	public Integer newBuildingsForResidentialPurposeTotal;
	public Integer newBuildingsWithoutResidentialPurposeCompleted;
	public Integer newBuildingsWithoutResidentialPurposeTotal;
	public Integer newDwellingsCompleted;
	public Integer newDwellingsTotal;

	public final ConstructionLocalisation constructionLocalisation = new ConstructionLocalisation();
	
	public LocalDate permitExpirationDate, permitIssueDate, announcementDate, declinationDate, startDate,
			completionDate, suspensionDate, withdrawlDate;

	public Integer constructionSurveyDeptNo;
	public Integer totalCostsOfProject;
	@Size(EchFormats.constructionProjectDescription)
	public String description;
	
	public static class ConstructionLocalisation {
		public MunicipalityIdentification municipality;
		public Canton canton;
		public CountryIdentification country;
	}
	
	public static class ConstructionProjectIdentification implements View<ConstructionProject> {

		public List<NamedId> localId = new ArrayList<>();
		public String EPROID;
		public String officialConstructionProjectFileNo, extensionOfOfficialConstructionProjectFileNo;

	}
}
