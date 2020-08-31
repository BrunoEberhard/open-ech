package ch.ech.ech0211.v1;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.468262")
public class SpecialistDepartment {
	public static final SpecialistDepartment $ = Keys.of(SpecialistDepartment.class);

	@NotEmpty
	public ch.ech.ech0097.OrganisationIdentification specialistDepartmentIdentification;
}