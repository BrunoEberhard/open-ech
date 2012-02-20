package ch.openech.dm.person;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.FormatName;

public class Separation {

	public static final Separation SEPARATION = Constants.of(Separation.class);
	
	public String separation;
	
	@Date
	public String dateOfSeparation, separationTill;
	
	@FormatName("partnerShipAbolition")
	public String cancelationReason;

//	@Override
//	public Separation clone() {
//		Separation clone = new Separation();
//		clone.separation = this.separation;
//		clone.dateOfSeparation = this.dateOfSeparation;
//		clone.separationTill = this.separationTill;
//		clone.cancelationReason = this.cancelationReason;
//		return clone;
//	}
}
