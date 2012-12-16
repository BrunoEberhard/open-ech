package ch.openech.dm.person;

import ch.openech.dm.EchFormats;
import ch.openech.dm.code.BasedOnLaw;
import ch.openech.dm.common.Address;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.dm.types.YesNo;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.EnumUtils;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.util.StringUtils;

public class Relation implements Validatable {

	public static final Relation RELATION = Constants.of(Relation.class);
	
	@Required
	public TypeOfRelationship typeOfRelationship;
	public BasedOnLaw basedOnLaw;
	public YesNo care = YesNo.No;
	@Required
	public PersonIdentification partner;
	
	public Address address;
	
	@Size(EchFormats.baseName)
	public String firstNameAtBirth, officialNameAtBirth; // only for parent
	
	// 
	
	public void clear() {
		typeOfRelationship = null;
		basedOnLaw = null;
		care = YesNo.No;
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
		// mit dem Property geben, da dann die Methode als getter-Methode
		// für die Variable Partner angesehen würde.
		// Daher heisst diese Methode als einzige isXyType() statt isXy()
		return typeOfRelationship != null && typeOfRelationship.isPartner();
	}
	
	public boolean isParent() {
		return typeOfRelationship != null && typeOfRelationship.isParent();
	}

	public boolean isCareRelation() {
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
		
		if (address != null && !address.isEmpty()) {
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

		if (typeOfRelationship != null) {
			String text = EnumUtils.getText(typeOfRelationship);
			if (!StringUtils.isBlank(text)) {
				if (isParent()) {
					if ("1".equals(care)) {
						text += " (mit Sorgerecht)";
					} else 	if ("0".equals(care)) {
						text += " (ohne Sorgerecht)";
					} else if (care != null) {
						text += " (Sorgerecht: " + care + ")";
					}
				} else if (isCareRelation() && basedOnLaw != null) {
					text += " (§ " + basedOnLaw + ")";
				}
				StringUtils.appendLine(s, text);
			} else {
				StringUtils.appendLine(s, "Typ der Beziehung:", EnumUtils.getText(typeOfRelationship));
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
		if (isParent() && care != null) {
			String text = EnumUtils.getText(care);
			if (!StringUtils.isBlank(text)) StringUtils.appendLine(s, "Sorgerecht:", text);
			else StringUtils.appendLine(s, "Sorgerecht:", EnumUtils.getText(care));
		}
	}

	@Override
	public String validate() {
		if (partner == null) {
			if (!isParent()) {
				return "Person muss gesetzt sein";
			} else if (address != null) {
				return "Adresse darf nur gesetzt sein, wenn Person gesetzt ist";
			}
		}
		if (!isParent()) {
			if (!StringUtils.isBlank(firstNameAtBirth) || !StringUtils.isBlank(officialNameAtBirth)) {
				return "\"Name:\" darf nur bei Mutter oder Vater gesetzt sein";
			}
		}
		return null;
	}
	
}
