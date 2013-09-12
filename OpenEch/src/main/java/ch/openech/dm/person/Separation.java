package ch.openech.dm.person;

import org.joda.time.LocalDate;

import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Inline;

@Inline
public class Separation {

	public static final Separation SEPARATION = Keys.of(Separation.class);
	
	public ch.openech.dm.person.types.Separation separation;
	public LocalDate dateOfSeparation, separationTill;
	
	public void clear() {
		separation = null;
		dateOfSeparation = null;
		separation = null;
	}
}
