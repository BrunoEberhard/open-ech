package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventChangeHealthInsurance {
	public static final EventChangeHealthInsurance $ = Keys.of(EventChangeHealthInsurance.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeHealthInsurancePerson;
	public ch.ech.ech0021.HealthInsuranceData healthInsuranceData;
}