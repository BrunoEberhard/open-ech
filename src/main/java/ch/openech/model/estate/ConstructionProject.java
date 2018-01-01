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
	@Size(EchFormats.constructionProjectDescription)
	public String description;
	
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
	
	public static enum TypeOfPermit implements EchCode {
		_5000, _5001, _5002, _5003, _5004, _5005, _5006, _5007, _5008, _5009, _5011, _5012, _5015, _5021, _5022, _5023, _5031, _5041, _5043, _5044, _5051, _5061, _5062, _5063, _5064, _5071;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
}
