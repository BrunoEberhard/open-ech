package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.822334200")
public class EventUndoMarriage {
	public static final EventUndoMarriage $ = Keys.of(EventUndoMarriage.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification undoMarriagePerson;
	public final ch.ech.ech0011.MaritalData maritalData = new ch.ech.ech0011.MaritalData();
}