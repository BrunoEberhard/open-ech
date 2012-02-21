package ch.openech.dm.person;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.Address;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.util.StringUtils;

public class PersonExtendedInformation {

	public static final PersonExtendedInformation PERSON_EXTENDED_INFORMATION = Constants.of(PersonExtendedInformation.class);
	
	public PersonIdentification personIdentification;
	
	@FormatName(EchFormats.yesNo)
	public String armedForcesService, armedForcesLiability;
	
	@FormatName(EchFormats.yesNo)
	public String civilDefense;
	
	@FormatName(EchFormats.yesNo)
	public String fireService, fireServiceLiability;
	
	@FormatName(EchFormats.yesNo)
	public String healthInsured;
	@FormatName(EchFormats.baseName) // TODO REMOVE
	public String insuranceName;
	public Address insuranceAddress;
	
	@FormatName(EchFormats.baseName) // TODO REMOVE
	public String matrimonialInheritanceArrangement;
	
	public void toHtml(StringBuilder s) {
//		s.append("<HTML>");
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
		
//		if (mailAddress != null) {
//			mailAddress.toHtml(s); s.append("<BR>");
//		}
//		s.append("</HTML>");
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