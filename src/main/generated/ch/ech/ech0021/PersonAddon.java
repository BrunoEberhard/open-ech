package ch.ech.ech0021;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

// handmade
public class PersonAddon {
	public static final PersonAddon $ = Keys.of(PersonAddon.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0044.PersonIdentification personidentification;
	public PersonAdditionalData personAdditionalData;
	public PoliticalRightData politicalRightData;
	public final BirthAddonData birthAddonData = new BirthAddonData();
	public LockData lockData;
	public List<PlaceOfOriginAddonData> placeOfOriginAddonData;
	public JobData jobData;
	public MaritalRelationship maritalRelationship;
	public List<ParentalRelationship> parentalRelationship;
	public List<GuardianRelationship> guardianRelationship;
	public ArmedForcesData armedForcesData;
	public CivilDefenseData civilDefenseData;
	public FireServiceData fireServiceData;
	public HealthInsuranceData healthInsuranceData;
	public MatrimonialInheritanceArrangementData matrimonialInheritanceArrangementData;

	//

	public NameOfParent getNameOfFatherAtBirth() {
		return birthAddonData.nameOfFather;
	}

	public void setNameOfFatherAtBirth(NameOfParent nameOfFather) {
		birthAddonData.nameOfFather = nameOfFather;
	}

	public NameOfParent getNameOfMotherAtBirth() {
		return birthAddonData.nameOfMother;
	}

	public void setNameOfMotherAtBirth(NameOfParent nameOfMother) {
		birthAddonData.nameOfMother = nameOfMother;
	}
	
}