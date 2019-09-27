package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.577")
public class BirthPerson {
	public static final BirthPerson $ = Keys.of(BirthPerson.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0044.PersonIdentification personIdentification;
	public final NameInfo nameInfo = new NameInfo();
	public final BirthInfo birthInfo = new BirthInfo();
	public final ch.ech.ech0011.ReligionData religionData = new ch.ech.ech0011.ReligionData();
	public final ch.ech.ech0011.MaritalData maritalData = new ch.ech.ech0011.MaritalData();
	public final ch.ech.ech0011.NationalityData nationalityData = new ch.ech.ech0011.NationalityData();
	public ch.ech.ech0011.ContactData contactData;
	public ch.ech.ech0021.PersonAdditionalData personAdditionalData;
	public List<PlaceOfOriginInfo> placeOfOriginInfo;
	public ch.ech.ech0011.ResidencePermitData residencePermitData;
	public final ch.ech.ech0021.LockData lockData = new ch.ech.ech0021.LockData();
	public List<ch.ech.ech0021.ParentalRelationship> parentalRelationship;
	public ch.ech.ech0021.HealthInsuranceData healthInsuranceData;
}