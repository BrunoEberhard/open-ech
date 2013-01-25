package ch.openech.dm.person;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.Address;
import ch.openech.dm.types.YesNo;
import ch.openech.mj.model.Constants;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.util.StringUtils;

public class PersonExtendedInformation {

	public static final PersonExtendedInformation PERSON_EXTENDED_INFORMATION = Constants.of(PersonExtendedInformation.class);
	
	public PersonIdentification personIdentification;
	
	public YesNo armedForcesService, armedForcesLiability;
	
	public YesNo civilDefense;
	
	public YesNo fireService, fireServiceLiability;
	
	public YesNo healthInsured;
	@Size(EchFormats.baseName) // TODO
	public String insuranceName;
	public Address insuranceAddress;
	
	public YesNo matrimonialInheritanceArrangement;

	public boolean isEmpty() {
		if (armedForcesService != null) return false;
		if (armedForcesLiability != null) return false;
		if (civilDefense != null) return false;
		if (fireService != null) return false;
		if (fireServiceLiability != null) return false;
		if (!StringUtils.isEmpty(insuranceName)) return false;
		if (insuranceAddress != null && !insuranceAddress.isEmpty()) return false;
		if (matrimonialInheritanceArrangement != null) return false;
		return true;
	}
	
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString();
	}

	public void toHtml(StringBuilder s) {
		s.append("<HTML>");
		yesNoToHtml(s, "Militärdienstpflicht", armedForcesService);
		yesNoToHtml(s, "Militärersatzpflicht", armedForcesLiability);
		s.append("<BR>");

		yesNoToHtml(s, "Zivilschutzdienstpflicht", civilDefense);
		s.append("<BR>");
		
		yesNoToHtml(s, "Feuerwehrdienstpflicht", fireService);
		yesNoToHtml(s, "Feuerwehrersatzpflicht", fireServiceLiability);
		s.append("<BR>");
		
		yesNoToHtml(s, "Krankenversichert", healthInsured);
		if (!StringUtils.isBlank(insuranceName)) {
			s.append("Krankenkasse: " + insuranceName);
			s.append("<BR><BR>");
		}
		if (insuranceAddress != null && !insuranceAddress.isEmpty()) {
			s.append("Krankenkasse:<BR>");
			insuranceAddress.toHtml(s);
			s.append("<BR>");
		}

		yesNoToHtml(s, "Gütererbrechtliche<BR>Vereinbarung", matrimonialInheritanceArrangement);
		s.append("</HTML>");
	}
	
	private void yesNoToHtml(StringBuilder s, String label, YesNo value) {
		s.append(label);
		s.append(": ");
		if (YesNo.No.equals(value)) {
			s.append("Nein");
		} else if (YesNo.Yes.equals(value)) {
			s.append("Ja");
		} else {
			s.append("-");
		}
		s.append("<BR>");
	}
}
