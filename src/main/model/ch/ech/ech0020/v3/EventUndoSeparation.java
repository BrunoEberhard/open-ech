package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventUndoSeparation {
	public static final EventUndoSeparation $ = Keys.of(EventUndoSeparation.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification undoSeparationPerson;
	public LocalDate separationValidTill;
}