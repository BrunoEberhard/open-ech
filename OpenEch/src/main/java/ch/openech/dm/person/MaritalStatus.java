package ch.openech.dm.person;

import ch.openech.mj.db.model.annotation.Date;


public class MaritalStatus {

	public String maritalStatus = "1";
	@Date
	public String dateOfMaritalStatus;

	public boolean isLedig() {
		return "1".equals(maritalStatus);
	}

	public boolean isVerheiratet() {
		return "2".equals(maritalStatus);
	}

	public boolean isVerwitwet() {
		return "3".equals(maritalStatus);
	}

	public boolean isGeschieden() {
		return "4".equals(maritalStatus);
	}

	public boolean isUngueltigeEhe() {
		return "5".equals(maritalStatus);
	}

	public boolean isPartnerschaft() {
		return "6".equals(maritalStatus);
	}

	public boolean isPartnerschaftAufgeloest() {
		return "7".equals(maritalStatus);
	}

//	@Override
//	protected MaritalStatus clone() {
//		MaritalStatus maritalStatus = new MaritalStatus();
//		maritalStatus.maritalStatus = this.maritalStatus;
//		maritalStatus.dateOfMaritalStatus = dateOfMaritalStatus;
//		return maritalStatus;
//	}
	
}
