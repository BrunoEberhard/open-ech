package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.846333800")
public class EventChangeHealthInsurance {
	public static final EventChangeHealthInsurance $ = Keys.of(EventChangeHealthInsurance.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeHealthInsurancePerson;
	public ch.ech.ech0021.HealthInsuranceData healthInsuranceData;
}