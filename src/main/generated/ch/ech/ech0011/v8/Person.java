package ch.ech.ech0011.v8;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

import ch.ech.ech0021.v7.ArmedForcesData;
import ch.ech.ech0021.v7.BirthAddonData;
import ch.ech.ech0021.v7.CivilDefenseData;
import ch.ech.ech0021.v7.FireServiceData;
import ch.ech.ech0021.v7.GuardianRelationship;
import ch.ech.ech0021.v7.HealthInsuranceData;
import ch.ech.ech0021.v7.JobData;
import ch.ech.ech0021.v7.LockData;
import ch.ech.ech0021.v7.MaritalRelationship;
import ch.ech.ech0021.v7.MatrimonialInheritanceArrangementData;
import ch.ech.ech0021.v7.ParentalRelationship;
import ch.ech.ech0021.v7.PersonAdditionalData;
import ch.ech.ech0021.v7.PlaceOfOriginAddonData;
import ch.ech.ech0021.v7.PoliticalRightData;

//handmade
// Die PersonAddon - Felder können auch gleich hier untergebracht werden
public class Person {
	public static final Person $ = Keys.of(Person.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0044.v4.PersonIdentification personIdentification;
	public final NameData nameData = new NameData();
	public final BirthData birthData = new BirthData();
	public final ReligionData religionData = new ReligionData();
	public final MaritalData maritalData = new MaritalData();
	public final NationalityData nationalityData = new NationalityData();
	public DeathData deathData;
	public ContactData contactData;
	@Size(2)
	public String languageOfCorrespondance;
	public Boolean restrictedVotingAndElectionRightFederation;
	public List<PlaceOfOrigin> placeOfOrigin;
	public ResidencePermitData residencePermit;
	
	// PersonAddOn
	
	public PersonAdditionalData personAdditionalData;
	public PoliticalRightData politicalRightData;
	public BirthAddonData birthAddonData;
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
}