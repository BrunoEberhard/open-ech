package ch.ech.ech0011;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.483")
public class SecondaryResidence {
	public static final SecondaryResidence $ = Keys.of(SecondaryResidence.class);

	public final ch.ech.ech0007.SwissMunicipality mainResidence = new ch.ech.ech0007.SwissMunicipality();
	public final ResidenceData secondaryResidence = new ResidenceData();
}