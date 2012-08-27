package ch.openech.dm.person;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.Date;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;

public class Separation {

	public static final Separation SEPARATION = Constants.of(Separation.class);
	
	public String separation;
	
	@Is(Date)
	public String dateOfSeparation, separationTill;
	
	public void clear() {
		separation = null;
		dateOfSeparation = null;
		separation = null;
	}
}
