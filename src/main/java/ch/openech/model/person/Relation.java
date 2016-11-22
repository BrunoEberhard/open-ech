package  ch.openech.model.person;

import java.util.List;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Enabled;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.StringUtils;

import ch.openech.model.code.BasedOnLaw;
import ch.openech.model.common.Address;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.model.types.YesNo;

public class Relation implements Validation, Rendering {

	public static final Relation $ = Keys.of(Relation.class);
	
	@NotEmpty
	public TypeOfRelationship typeOfRelationship;
	// TODO hier sind am 2.3 mehrere Auswahlen möglich. Die Persistenzschicht
	// sollte mit einer Liste von enums umgehen können ebenso das EditField.
	@Enabled("isCareRelation")
	public BasedOnLaw basedOnLaw;
	@Size(100)
	public String basedOnLawAddOn;
	public YesNo care = YesNo.No;

	public final PartnerIdentification partner = new PartnerIdentification();
	
	public Address address;
	
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
	
	@Override
	public String render(RenderType renderType) {
		return toHtml();
	}

	@Override
	public RenderType getPreferredRenderType(RenderType firstType, RenderType... otherTypes) {
		return RenderType.HMTL;
	}

	public void identificationToHtml(StringBuilder s) {
		if (partner != null) {
			partner.toHtml(s);
		}
		
		if (address != null && !address.isEmpty()) {
			address.toHtml(s);
		}
	}

	@Deprecated
	public String toHtml() {
		StringBuilder s = new StringBuilder();

		if (typeOfRelationship != null) {
			String text = EnumUtils.getText(typeOfRelationship);
			if (!StringUtils.isBlank(text)) {
				if (isParent()) {
					if (care == YesNo.Yes) {
						text += " (mit Sorgerecht)";
					} else 	if (care == YesNo.No) {
						text += " (ohne Sorgerecht)";
					} 
				} else if (isCareRelation() && basedOnLaw != null) {
					if (!StringUtils.isEmpty(basedOnLawAddOn)) {
						text += " (§ " + basedOnLaw + "/"  + basedOnLawAddOn + ")";
					} else {
						text += " (§ " + basedOnLaw + ")";
					}
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
	public List<ValidationMessage> validate() {
		if (partner == null) {
			if (!isParent()) {
				return Validation.message($.partner, "Person muss gesetzt sein");
			} else if (address != null) {
				return Validation.message($.address, "Adresse darf nur gesetzt sein, wenn Person gesetzt ist");
			}
		}
		return null;
	}
	
}
