package ch.ech.ech0011;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.789605800")
public class SecondaryResidence {
	public static final SecondaryResidence $ = Keys.of(SecondaryResidence.class);

	public final ch.ech.ech0007.SwissMunicipality mainResidence = new ch.ech.ech0007.SwissMunicipality();
	public final ResidenceData secondaryResidence = new ResidenceData();
}