package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventCorrectResidencePermit {
	public static final EventCorrectResidencePermit $ = Keys.of(EventCorrectResidencePermit.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctResidencePermitPerson;
	public ch.ech.ech0011.ResidencePermitData residencePermitData;
}