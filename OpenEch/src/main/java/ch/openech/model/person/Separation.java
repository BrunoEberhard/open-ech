package  ch.openech.model.person;

import java.time.LocalDate;

import org.minimalj.model.Keys;

public class Separation {

	public static final Separation $ = Keys.of(Separation.class);
	
	public  ch.openech.model.person.types.Separation separation;
	public LocalDate dateOfSeparation, separationTill;
	
	public void clear() {
		separation = null;
		dateOfSeparation = null;
		separation = null;
	}
}
