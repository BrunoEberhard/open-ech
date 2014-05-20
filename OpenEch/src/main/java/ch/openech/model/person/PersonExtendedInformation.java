package  ch.openech.model.person;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

import  ch.openech.model.EchFormats;
import  ch.openech.model.common.Address;
import  ch.openech.model.types.YesNo;

public class PersonExtendedInformation {

	public static final PersonExtendedInformation PERSON_EXTENDED_INFORMATION = Keys.of(PersonExtendedInformation.class);
	
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
