package ch.openech.dm.person;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.Address;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;
import ch.openech.mj.util.StringUtils;

public class PersonExtendedInformation {

	public static final PersonExtendedInformation PERSON_EXTENDED_INFORMATION = Constants.of(PersonExtendedInformation.class);
	
	public PersonIdentification personIdentification;
	
	@Is(EchFormats.yesNo)
	public String armedForcesService, armedForcesLiability;
	
	@Is(EchFormats.yesNo)
	public String civilDefense;
	
	@Is(EchFormats.yesNo)
	public String fireService, fireServiceLiability;
	
	@Is(EchFormats.yesNo)
	public String healthInsured;
	@Is(EchFormats.baseName) // TODO REMOVE
	public String insuranceName;
	public Address insuranceAddress;
	
	@Is(EchFormats.baseName) // TODO REMOVE
	public String matrimonialInheritanceArrangement;

	public boolean isEmpty() {
		if (!StringUtils.isEmpty(armedForcesService)) return false;
		if (!StringUtils.isEmpty(armedForcesLiability)) return false;
		if (!StringUtils.isEmpty(civilDefense)) return false;
		if (!StringUtils.isEmpty(fireService)) return false;
		if (!StringUtils.isEmpty(fireServiceLiability)) return false;
		if (!StringUtils.isEmpty(insuranceName)) return false;
		if (insuranceAddress != null && !insuranceAddress.isEmpty()) return false;
		if (!StringUtils.isEmpty(matrimonialInheritanceArrangement)) return false;
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
	
	private void yesNoToHtml(StringBuilder s, String label, String value) {
		s.append(label);
		s.append(": ");
		if ("0".equals(value)) {
			s.append("Nein");
		} else if ("1".equals(value)) {
			s.append("Ja");
		} else {
			s.append("-");
		}
		s.append("<BR>");
	}
}
