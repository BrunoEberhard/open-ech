package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventCorrectMaritalInfo {
	public static final EventCorrectMaritalInfo $ = Keys.of(EventCorrectMaritalInfo.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctMaritalDataPerson;
	public final MaritalInfo maritalInfo = new MaritalInfo();
	public ch.ech.ech0021.MaritalRelationship maritalRelationship;
}