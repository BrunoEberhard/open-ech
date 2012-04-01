package ch.openech.dm.person;

import ch.openech.mj.db.model.annotation.Date;


public class MaritalStatus {

	public String maritalStatus = ch.openech.dm.code.MaritalStatus.Ledig.value;
	@Date
	public String dateOfMaritalStatus;

	public boolean isLedig() {
		return ch.openech.dm.code.MaritalStatus.Ledig.value.equals(maritalStatus);
	}

	public boolean isVerheiratet() {
		return ch.openech.dm.code.MaritalStatus.Verheiratet.value.equals(maritalStatus);
	}

	public boolean isVerwitwet() {
		return ch.openech.dm.code.MaritalStatus.Verwitwet.value.equals(maritalStatus);
	}

	public boolean isGeschieden() {
		return ch.openech.dm.code.MaritalStatus.Geschieden.value.equals(maritalStatus);
	}

	public boolean isUngueltigeEhe() {
		return ch.openech.dm.code.MaritalStatus.Unverheiratet.value.equals(maritalStatus);
	}

	public boolean isPartnerschaft() {
		return ch.openech.dm.code.MaritalStatus.Partnerschaft.value.equals(maritalStatus);
	}

	public boolean isPartnerschaftAufgeloest() {
		return ch.openech.dm.code.MaritalStatus.AufgeloestePartnerschaft.value.equals(maritalStatus);
	}

//	@Override
//	protected MaritalStatus clone() {
//		MaritalStatus maritalStatus = new MaritalStatus();
//		maritalStatus.maritalStatus = this.maritalStatus;
//		maritalStatus.dateOfMaritalStatus = dateOfMaritalStatus;
//		return maritalStatus;
//	}
	
}
