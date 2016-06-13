package ch.openech.model.person;

import java.time.LocalDate;

import org.minimalj.model.Rendering;

import ch.openech.model.organisation.Organisation;
import ch.openech.model.person.types.Vn;
import ch.openech.model.types.Sex;

/**
 * Wird zum Beispiel bei einer Beziehung verwendet. Es kann nur eine der
 * drei Möglichkeiten verwenden werden:
 * 
 * <UL>
 * <LI>Eine Referenz zu einer Person im System</LI>
 * <LI>Freie Eingaben der Identifikatoren</LI>
 * <LI>Eine Referenz zu einer Firma im System</LI>
 * </UL>
 * 
 */
public class PartnerIdentification implements Rendering {

	// only one is set
	public PersonIdentification personIdentification;
	public PersonIdentificationLight personIdentificationLight;
	public Organisation organisation;

	public boolean isEmpty() {
		return personIdentification == null && personIdentificationLight == null && organisation == null;
	}
	
	public void clear() {
		personIdentification = null;
		personIdentificationLight = null;
		organisation = null;
	}
	
	@Override
	public String render(RenderType renderType) {
		return toHtml();
	}
	
	@Override
	public RenderType getPreferredRenderType(RenderType firstType, RenderType... otherTypes) {
		return RenderType.HMTL;
	}
	
	@Deprecated
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString();
	}
	
	public void toHtml(StringBuilder s) {
		if (personIdentification != null) {
			personIdentification.toHtml(s);
		} else if (personIdentificationLight != null) {
			s.append(personIdentificationLight.render(RenderType.HMTL));
		} else if (organisation != null) {
			// TODO organisation.toString();
		}
	}

	public void setValue(Person person) {
		clear();
		this.personIdentification = person.personIdentification();
	}
	
	public LocalDate dateOfBirth() {
		if (personIdentification != null) {
			return personIdentification.dateOfBirth.toLocalDate();
		} else if (personIdentificationLight != null) {
			return personIdentificationLight.dateOfBirth.toLocalDate();
		} else {
			return null;
		}
	}

	public Sex sex() {
		if (personIdentification != null) {
			return personIdentification.sex;
		} else if (personIdentificationLight != null) {
			return personIdentificationLight.sex;
		} else {
			return null;
		}
	}

	public Vn vn() {
		if (personIdentification != null) {
			return personIdentification.vn;
		} else if (personIdentificationLight != null) {
			return personIdentificationLight.vn;
		} else {
			return null;
		}
	}

}
