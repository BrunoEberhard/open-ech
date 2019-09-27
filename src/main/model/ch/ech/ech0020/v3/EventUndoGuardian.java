package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.905")
public class EventUndoGuardian {
	public static final EventUndoGuardian $ = Keys.of(EventUndoGuardian.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification undoGuardianPerson;
	@NotEmpty
	@Size(36)
	public String guardianRelationshipId;
	public LocalDate undoGuardianValidFrom;
}