package ch.openech.dm.person;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Date;

public class Separation {

	public static final Separation SEPARATION = Constants.of(Separation.class);
	
	public String separation;
	
	@Date
	public String dateOfSeparation, separationTill;
	
	public void clear() {
		separation = null;
		dateOfSeparation = null;
		separation = null;
	}
}
