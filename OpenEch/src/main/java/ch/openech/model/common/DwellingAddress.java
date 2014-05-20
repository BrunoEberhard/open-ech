package  ch.openech.model.common;

import java.util.List;

import org.joda.time.LocalDate;
import org.minimalj.model.Codes;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Code;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import ch.openech.xml.write.EchSchema;

public class DwellingAddress implements Validation {

	public static final DwellingAddress DWELLING_ADDRESS = Keys.of(DwellingAddress.class);
	
	public transient EchSchema echSchema;
	
	@Size(9)
	public String EGID;
	@Size(3)
	public String EWID;
	@Size(12) // ist nicht bekannt aus Schema
	public String householdID; // not for organisation
	public Address mailAddress;
	@Code
	public String typeOfHousehold; // not for organisation
	public LocalDate movingDate;

	public String toHtml() {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString();
	}
		
	public void toHtml(StringBuilder s) {
		if (mailAddress != null) {
			mailAddress.toHtml(s); s.append("<BR>");
		}
		if (!StringUtils.isBlank(EGID)) {
			s.append("EGID: " );
			s.append(EGID); 
		}
		if (!StringUtils.isBlank(EWID)) {
			s.append("<BR>EWID: ");
			s.append(EWID);
		}
		
		if (!StringUtils.isBlank(householdID)) {
			s.append("<BR>Haushalt ID: ");
			s.append(householdID);
		}
		s.append("<BR>");
		if (typeOfHousehold != null) {
			s.append(Codes.getCode("typeOfHousehold").getText(typeOfHousehold));
		}
		s.append("<BR>");
		if (movingDate != null) {
			s.append("Umzugsdatum: " ); s.append(DateUtils.formatCH(movingDate));
		}
	}
	
	public void displayWidth(StringBuilder s) {
		s.append("EGID: " );
		if (!StringUtils.isBlank(EGID)) s.append(EGID); else s.append("- ");
		s.append(", EWID: ");
		if (!StringUtils.isBlank(EWID)) s.append(EWID); else s.append("- ");
		s.append(", Haushalt ID: ");
		if (!StringUtils.isBlank(householdID)) s.append(householdID); else s.append("- ");
		s.append("\n");
		if (mailAddress != null) {
			mailAddress.toHtml(s);
		}
		s.append("Haushaltsart: ");
		if (typeOfHousehold != null) s.append(Codes.getCode("typeOfHousehold").getText(typeOfHousehold)); else s.append("- ");
		s.append("\n");
		if (movingDate != null) {
			s.append("Umzugsdatum: " ); s.append(DateUtils.formatCH(movingDate));
		}
	}
	
	public void validate(List<ValidationMessage> resultList) {
		if (!echSchema.addressesAreBusiness()) {
			if (!StringUtils.isBlank(EGID)) {
				if (!StringUtils.isBlank(EWID) && !StringUtils.isBlank(householdID)) {
					resultList.add(new ValidationMessage(DWELLING_ADDRESS.householdID, "Bei gesetzter EGID können nicht EWID und Haushalt ID gesetzt sein"));
				}
			} else {
				if (StringUtils.isBlank(householdID)) {
					resultList.add(new ValidationMessage(DWELLING_ADDRESS.householdID, "Bei fehlender EGID muss die Haushalt ID gesetzt sein"));
				}
			}
		}
		if (mailAddress == null || mailAddress.isEmpty()) {
			resultList.add(new ValidationMessage(DWELLING_ADDRESS.mailAddress, "Postadresse erforderlich"));
		}
	}

}
