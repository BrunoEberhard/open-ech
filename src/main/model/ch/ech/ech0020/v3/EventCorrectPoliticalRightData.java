package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.842334500")
public class EventCorrectPoliticalRightData {
	public static final EventCorrectPoliticalRightData $ = Keys.of(EventCorrectPoliticalRightData.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctPoliticalRightDataPerson;
	public ch.ech.ech0021.PoliticalRightData politicalRightData;
}