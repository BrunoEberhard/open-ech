package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.608")
public class BaseDeliveryPerson {
	public static final BaseDeliveryPerson $ = Keys.of(BaseDeliveryPerson.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0044.PersonIdentification personIdentification;
	public final NameInfo nameInfo = new NameInfo();
	public final BirthInfo birthInfo = new BirthInfo();
	public final ch.ech.ech0011.ReligionData religionData = new ch.ech.ech0011.ReligionData();
	public final MaritalInfo maritalInfo = new MaritalInfo();
	public final ch.ech.ech0011.NationalityData nationalityData = new ch.ech.ech0011.NationalityData();
	public ch.ech.ech0011.DeathData deathData;
	public ch.ech.ech0011.ContactData contactData;
	public ch.ech.ech0021.PersonAdditionalData personAdditionalData;
	public ch.ech.ech0021.PoliticalRightData politicalRightData;
	public List<PlaceOfOriginInfo> placeOfOriginInfo;
	public ch.ech.ech0011.ResidencePermitData residencePermitData;
	public final ch.ech.ech0021.LockData lockData = new ch.ech.ech0021.LockData();
	public ch.ech.ech0021.JobData jobData;
	public ch.ech.ech0021.MaritalRelationship maritalRelationship;
	public List<ch.ech.ech0021.ParentalRelationship> parentalRelationship;
	public List<ch.ech.ech0021.GuardianRelationship> guardianRelationship;
	public ch.ech.ech0021.ArmedForcesData armedForcesData;
	public ch.ech.ech0021.CivilDefenseData civilDefenseData;
	public ch.ech.ech0021.FireServiceData fireServiceData;
	public ch.ech.ech0021.HealthInsuranceData healthInsuranceData;
	public ch.ech.ech0021.MatrimonialInheritanceArrangementData matrimonialInheritanceArrangementData;
}