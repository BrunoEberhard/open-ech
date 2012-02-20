package ch.openech.dm.common;

import java.util.List;

import ch.openech.dm.code.EchCodes;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.Int;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

public class DwellingAddress {

	public static final DwellingAddress DWELLING_ADDRESS = Constants.of(DwellingAddress.class);
	
	@Int(9)
	public String EGID;
	@Int(3)
	public String EWID;
	@Int(12) // ist nicht bekannt aus Schema
	public String householdID; // not for organisation
	public Address mailAddress;
	public String typeOfHousehold; // not for organisation
	@Date
	public String movingDate;
	
	public void validate(List<ValidationMessage> resultList) {
		if (!StringUtils.isBlank(EGID)) {
			if (!StringUtils.isBlank(EWID) && !StringUtils.isBlank(householdID)) {
				resultList.add(new ValidationMessage(DWELLING_ADDRESS.householdID, "Bei gesetzter EGID können nicht EWID und Haushalt ID gesetzt sein"));
			}
		} else {
			if (StringUtils.isBlank(householdID)) {
				resultList.add(new ValidationMessage(DWELLING_ADDRESS.householdID, "Bei fehlender EGID muss die Haushalt ID gesetzt sein"));
			}
		}
		if (mailAddress == null ||  mailAddress.isEmpty()) {
			resultList.add(new ValidationMessage(DWELLING_ADDRESS.mailAddress, "Postadresse muss gesetzt sein"));
		}
	}
	
	public void toHtml(StringBuilder s) {
		s.append("<HTML>");
		if (mailAddress != null) {
			mailAddress.toHtml(s); s.append("<BR>");
		}
		s.append("EGID: " );
		if (!StringUtils.isBlank(EGID)) s.append(EGID); else s.append("- ");
		s.append("<BR>EWID: ");
		if (!StringUtils.isBlank(EWID)) s.append(EWID); else s.append("- ");
		s.append("<BR>Haushalt ID: ");
		if (!StringUtils.isBlank(householdID)) s.append(householdID); else s.append("- ");
		s.append("<BR>");
		if (StringUtils.isBlank(typeOfHousehold)) {
			s.append("Haushaltsart: -");
		} else if (typeOfHouseHoldIsCode()) {
			// Die Codes der Haushaltsart sind sprechend genug, man kann sich daher das Textlabel sparen
			s.append(EchCodes.typeOfHousehold.getText(typeOfHousehold));
		} else {
			s.append("Haushaltsart: ");
			s.append(typeOfHousehold);
		}
		s.append("<BR>");
		if (!StringUtils.isBlank(movingDate)) {
			s.append("Umzugsdatum: " ); s.append(DateUtils.formatCH(movingDate));
		}
		s.append("</HTML>");
	}
	
	private boolean typeOfHouseHoldIsCode() {
		return !StringUtils.isBlank(typeOfHousehold) && "0".compareTo(typeOfHousehold)  <= 0 && "3".compareTo(typeOfHousehold) >=0; 
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
		if (!StringUtils.isBlank(typeOfHousehold)) s.append(EchCodes.typeOfHousehold.getText(typeOfHousehold)); else s.append("- ");
		s.append("\n");
		if (!StringUtils.isBlank(movingDate)) {
			s.append("Umzugsdatum: " ); s.append(DateUtils.formatCH(movingDate));
		}
	}

}
