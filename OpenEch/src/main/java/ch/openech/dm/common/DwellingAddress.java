package ch.openech.dm.common;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.Date;
import static ch.openech.mj.db.model.annotation.PredefinedFormat.Int12;
import static ch.openech.mj.db.model.annotation.PredefinedFormat.Int3;
import static ch.openech.mj.db.model.annotation.PredefinedFormat.Int9;
import ch.openech.dm.code.EchCodes;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

public class DwellingAddress {

	public static final DwellingAddress DWELLING_ADDRESS = Constants.of(DwellingAddress.class);
	
	@Is(Int9)
	public String EGID;
	@Is(Int3)
	public String EWID;
	@Is(Int12) // ist nicht bekannt aus Schema
	public String householdID; // not for organisation
	public Address mailAddress;
	public String typeOfHousehold; // not for organisation
	@Is(Date)
	public String movingDate;

	public String toHtml() {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString();
	}
		
	public void toHtml(StringBuilder s) {
		s.append("<HTML>");
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
		if (StringUtils.isBlank(typeOfHousehold)) {
			// print nothing
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
