package ch.openech.model.estate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.View;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.repository.sql.EmptyObjects;

import ch.openech.model.EchFormats;
import ch.openech.model.common.Canton;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.NamedId;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.person.PersonIdentificationLight;
import ch.openech.model.types.EchCode;

@Sizes(EchFormats.class)
public class ConstructionProject {
	public static final ConstructionProject $ = Keys.of(ConstructionProject.class);
	
	public Object id;
	
	@NotEmpty
	public List<NamedId> localId = new ArrayList<>();
	public String EPROID;
	public String officialConstructionProjectFileNo, extensionOfOfficialConstructionProjectFileNo;
	
	public Integer newBuildingsForResidentialPurposeCompleted;
	public Integer newBuildingsForResidentialPurposeTotal;
	public Integer newBuildingsWithoutResidentialPurposeCompleted;
	public Integer newBuildingsWithoutResidentialPurposeTotal;
	public Integer newDwellingsCompleted;
	public Integer newDwellingsTotal;
	public TypeOfConstructionProject typeOfConstructionProject;

	public final ConstructionLocalisation constructionLocalisation = new ConstructionLocalisation();
	
	public TypeOfPermit typeOfPermit;
	
	public LocalDate permitExpirationDate, permitIssueDate, announcementDate, declinationDate, startDate,
			completionDate, suspensionDate, withdrawlDate;

	public Integer constructionSurveyDeptNo;
	public Integer totalCostsOfProject;
	@NotEmpty
	public ProjectStatus status;
	public TypeOfClient typeOfClient;
	public TypeOfConstruction typeOfConstruction;
	@Size(EchFormats.constructionProjectDescription)
	public String description;
	@Size(EchFormats.durationOfConstructionPhase)
	public Integer durationOfConstructionPhase;
	public PersonOrOrganisation planningPermissionApplicant;
	public PersonOrOrganisation planningPermissionApplicantDeputy;
	public PersonOrOrganisation author;
	
	public static class PersonOrOrganisation {
		// eines der zweien muss gesetzt sein, wobei das gesamte Objekt optional ist.
		public PersonIdentificationLight individual;
		public OrganisationIdentification organisation;
	}
	
	public static class ConstructionLocalisation implements Validation {
		public static final ConstructionLocalisation $ = Keys.of(ConstructionLocalisation.class);
		
		// ein von den drei
		public MunicipalityIdentification municipality;
		public Canton canton;
		public CountryIdentification country;
		
		@Override
		public List<ValidationMessage> validate() {
			if (EmptyObjects.isEmpty(municipality) && EmptyObjects.isEmpty(canton) && EmptyObjects.isEmpty(country)) {
				return Validation.message(ConstructionLocalisation.$.municipality, "Gemeinde, Kanton oder Land muss eingegeben werden");
			} else {
				return null;
			}
		}
	}
	
	public static class ConstructionProjectIdentification implements View<ConstructionProject> {

		public List<NamedId> localId = new ArrayList<>();
		public Integer EPROID;
		public String officialConstructionProjectFileNo, extensionOfOfficialConstructionProjectFileNo;

	}
	
	public static enum TypeOfConstructionProject implements EchCode {
		_6010, _6011;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}

	public static enum TypeOfClient implements EchCode {
		_6101, _6103, _6104, _6107, _6108, _6110, _6111, _6115, _6116, _6121, _6122, _6123, _6124, _6131, _6132, _6133, _6141, _6142, _6143, _6161, _6151, _6152, _6162, _6163;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public static enum TypeOfConstruction implements EchCode {
		_6211, _6212, _6213, _6214, _6219, _6221, _6222, _6223, _6231, _6232, _6233, _6234, _6235, _6241, _6242, _6243, _6244, _6245, _6249, _6251, _6252, _6253, _6254, _6255, _6256, _6257, _6258, _6259, _6261, _6262, _6269, _6271, _6272, _6273, _6274, _6276, _6278, _6279, _6281, _6282, _6283, _6291, _6292, _6293, _6294, _6295, _6296, _6299;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public static enum TypeOfPermit implements EchCode {
		_5000, _5001, _5002, _5003, _5004, _5005, _5006, _5007, _5008, _5009, _5011, _5012, _5015, _5021, _5022, _5023, _5031, _5041, _5043, _5044, _5051, _5061, _5062, _5063, _5064, _5071;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public static enum ProjectStatus implements EchCode {
		_6701, _6702, _6703, _6704, _6705, _6706, _6707, _6708, _6709;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
}
