package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.834333700")
public class EventCorrectParentalRelationship {
	public static final EventCorrectParentalRelationship $ = Keys.of(EventCorrectParentalRelationship.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctParentalRelationshipPerson;
	public List<ch.ech.ech0021.ParentalRelationship> parentalRelationship;
}