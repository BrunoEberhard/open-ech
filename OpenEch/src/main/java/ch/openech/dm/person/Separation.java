package ch.openech.dm.person;

import org.joda.time.LocalDate;

import ch.openech.mj.model.Constants;

public class Separation {

	public static final Separation SEPARATION = Constants.of(Separation.class);
	
	public ch.openech.dm.person.types.Separation separation;
	public LocalDate dateOfSeparation, separationTill;
	
	public void clear() {
		separation = null;
		dateOfSeparation = null;
		separation = null;
	}
}
