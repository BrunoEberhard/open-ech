package ch.ech.ech0211.v1;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class DecisionAuthority {
	public static final DecisionAuthority $ = Keys.of(DecisionAuthority.class);

	@NotEmpty
	public ch.ech.ech0097.OrganisationIdentification decisionAuthorityIdentification;
	public ch.ech.ech0007.SwissMunicipality municipality;
}