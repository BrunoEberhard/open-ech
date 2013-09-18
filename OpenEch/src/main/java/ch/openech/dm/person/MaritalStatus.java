package ch.openech.dm.person;

import org.joda.time.LocalDate;

import ch.openech.mj.model.annotation.Enabled;

public class MaritalStatus {

	public ch.openech.dm.person.types.MaritalStatus maritalStatus = ch.openech.dm.person.types.MaritalStatus.ledig;
	@Enabled("!isLedig")
	public LocalDate dateOfMaritalStatus;

	public boolean isLedig() {
		return ch.openech.dm.person.types.MaritalStatus.ledig.equals(maritalStatus);
	}

	public boolean isVerheiratet() {
		return ch.openech.dm.person.types.MaritalStatus.verheiratet.equals(maritalStatus);
	}

	public boolean isVerwitwet() {
		return ch.openech.dm.person.types.MaritalStatus.verwitwet.equals(maritalStatus);
	}

	public boolean isGeschieden() {
		return ch.openech.dm.person.types.MaritalStatus.geschieden.equals(maritalStatus);
	}

	public boolean isUngueltigeEhe() {
		return ch.openech.dm.person.types.MaritalStatus.ungueltig.equals(maritalStatus);
	}

	public boolean isPartnerschaft() {
		return ch.openech.dm.person.types.MaritalStatus.partnerschaft.equals(maritalStatus);
	}

	public boolean isPartnerschaftAufgeloest() {
		return ch.openech.dm.person.types.MaritalStatus.aufgeloeste_partnerschaft.equals(maritalStatus);
	}
	
}
