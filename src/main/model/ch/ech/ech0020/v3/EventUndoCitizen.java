package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.655")
public class EventUndoCitizen {
	public static final EventUndoCitizen $ = Keys.of(EventUndoCitizen.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification undoCitizenPerson;
	public final ch.ech.ech0011.PlaceOfOrigin placeOfOrigin = new ch.ech.ech0011.PlaceOfOrigin();
	public ch.ech.ech0021.PlaceOfOriginAddonData placeOfOriginAddon;
}