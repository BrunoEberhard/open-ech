package  ch.openech.model.person;

import org.threeten.bp.LocalDate;
import org.minimalj.model.annotation.Enabled;

public class MaritalStatus {

	public  ch.openech.model.person.types.MaritalStatus maritalStatus =  ch.openech.model.person.types.MaritalStatus.ledig;
	@Enabled("!isLedig")
	public LocalDate dateOfMaritalStatus;

	public boolean isLedig() {
		return  ch.openech.model.person.types.MaritalStatus.ledig.equals(maritalStatus);
	}

	public boolean isVerheiratet() {
		return  ch.openech.model.person.types.MaritalStatus.verheiratet.equals(maritalStatus);
	}

	public boolean isVerwitwet() {
		return  ch.openech.model.person.types.MaritalStatus.verwitwet.equals(maritalStatus);
	}

	public boolean isGeschieden() {
		return  ch.openech.model.person.types.MaritalStatus.geschieden.equals(maritalStatus);
	}

	public boolean isUngueltigeEhe() {
		return  ch.openech.model.person.types.MaritalStatus.ungueltig.equals(maritalStatus);
	}

	public boolean isPartnerschaft() {
		return  ch.openech.model.person.types.MaritalStatus.partnerschaft.equals(maritalStatus);
	}

	public boolean isPartnerschaftAufgeloest() {
		return  ch.openech.model.person.types.MaritalStatus.aufgeloeste_partnerschaft.equals(maritalStatus);
	}
	
}
