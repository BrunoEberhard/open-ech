package ch.openech.dm.person;

import ch.openech.dm.EchFormats;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.code.TypeOfRelationship;
import ch.openech.dm.common.Address;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.util.StringUtils;

public class Relation {

	public static final Relation RELATION = Constants.of(Relation.class);
	
	public String typeOfRelationship;
	public String basedOnLaw;
	@FormatName(EchFormats.yesNo)
	public String care = "0";
	public PersonIdentification partner;
	
	public Address address;
	
	@FormatName(EchFormats.baseName)
	public String firstNameAtBirth, officialNameAtBirth; // only for parent
	
	// 
	
	public TypeOfRelationship lookupTypeOfRelationship() {
		return TypeOfRelationship.lookup(typeOfRelationship);
	}
	
	public void clear() {
		typeOfRelationship = null;
		basedOnLaw = null;
		care = "0";
		partner = new PersonIdentification();
		address = null;
		firstNameAtBirth = null;
		officialNameAtBirth = null;
	}
	
	public boolean isEmpty() {
		return partner == null || partner.isEmpty();
	}
	
	//
	
	public boolean isPartnerType() {
		// würde die Methode isPartner() heissen würde es einen Konflikt
		// mit dem PropertyAccessor geben, da dann die Methode als getter-Methode
		// für die Variable Partner angesehen würde.
		// Daher heisst diese Methode als einzige isXyType() statt isXy()
		TypeOfRelationship typeOfRelationship = lookupTypeOfRelationship();
		return typeOfRelationship != null && typeOfRelationship.isPartner();
	}
	
	public boolean isParent() {
		TypeOfRelationship typeOfRelationship = lookupTypeOfRelationship();
		return typeOfRelationship != null && typeOfRelationship.isParent();
	}

	public boolean isCareRelation() {
		TypeOfRelationship typeOfRelationship = lookupTypeOfRelationship();
		return typeOfRelationship != null && typeOfRelationship.isCare();
	}

	public boolean isIdentificationSet() {
		return partner != null && address != null;
	}
	
	
	public String identificationToHtml() {
		StringBuilder s = new StringBuilder();
		s.append("<HTML>");
		identificationToHtml(s);
		s.append("</HTML>");
		return s.toString();
	}
	
	public void identificationToHtml(StringBuilder s) {
		if (partner != null) {
			s.append("Person:<BR>");
			partner.toHtml(s);
			s.append("&nbsp<BR>");
		}
		
		if (address != null) {
			s.append("Adresse:<BR>");
			address.toHtml(s);
			s.append("<BR>");
		}
		
		if (!StringUtils.isBlank(firstNameAtBirth) || !StringUtils.isBlank(officialNameAtBirth)) {
			s.append("Name bei Geburt:<BR>");
			StringUtils.appendLine(s, firstNameAtBirth, officialNameAtBirth);
			s.append("<BR>");
		}
	}

	public String toHtml() {
		StringBuilder s = new StringBuilder();
		s.append("<HTML>");

		if (!StringUtils.isBlank(typeOfRelationship)) {
			String text = EchCodes.typeOfRelationship.getText(typeOfRelationship);
			if (!StringUtils.isBlank(text)) {
				if (isParent()) {
					if ("1".equals(care)) {
						text += " (mit Sorgerecht)";
					} else 	if ("0".equals(care)) {
						text += " (ohne Sorgerecht)";
					} else if (care != null) {
						text += " (Sorgerecht: " + care + ")";
					}
				} else if (isCareRelation() && !StringUtils.isBlank(basedOnLaw)) {
					text += " (§ " + basedOnLaw + ")";
				}
				StringUtils.appendLine(s, text);
			} else {
				StringUtils.appendLine(s, "Typ der Beziehung:", typeOfRelationship);
				appendCare(s);
			}
		} else {
			appendCare(s);
		}

		identificationToHtml(s);
		
		s.append("</HTML>");
		return s.toString();
	}
	
	private void appendCare(StringBuilder s) {
		if (isParent() && !StringUtils.isBlank(care)) {
			String text = EchCodes.yesNo.getText(care);
			if (!StringUtils.isBlank(text)) StringUtils.appendLine(s, "Sorgerecht:", text);
			else StringUtils.appendLine(s, "Sorgerecht:", care);
		}
	}
	
}
