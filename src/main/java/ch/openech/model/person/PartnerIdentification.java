package ch.openech.model.person;

import java.time.LocalDate;

import org.minimalj.model.Rendering.RenderType;
import org.minimalj.model.annotation.Reference;

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
public class PartnerIdentification {

	@Reference
	public Person person;
	
	@Reference
	public PersonIdentificationLight personIdentification;

	@Reference
	public Organisation organisation;

	public boolean isEmpty() {
		return person == null && personIdentification == null && organisation == null;
	}
	
	public void clear() {
		person = null;
		personIdentification = null;
		organisation = null;
	}
	
	@Deprecated
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString();
	}
	
	public void toHtml(StringBuilder s) {
		if (person != null) {
			person.toHtml(s);
		} else if (personIdentification != null) {
			s.append(personIdentification.render(RenderType.HMTL));
		} else if (organisation != null) {
			// TODO organisation.toString();
		}
	}

	public void setValue(Person person) {
		clear();
		this.person = person;
	}
	
	public void setValue(PersonIdentification personIdentification) {
		clear();
		this.personIdentification = new PersonIdentificationLight();
		this.personIdentification.otherId.addAll(personIdentification.technicalIds.otherId);
		this.personIdentification.vn.value = personIdentification.vn.value;
		this.personIdentification.firstName = personIdentification.firstName;
		this.personIdentification.officialName = personIdentification.officialName;
		this.personIdentification.sex = personIdentification.sex;
		this.personIdentification.dateOfBirth.value = personIdentification.dateOfBirth.value;
	}
	
	public LocalDate dateOfBirth() {
		if (person != null) {
			return person.dateOfBirth.toLocalDate();
		} else if (personIdentification != null) {
			return personIdentification.dateOfBirth.toLocalDate();
		} else {
			return null;
		}
	}

	public Sex sex() {
		if (person != null) {
			return person.sex;
		} else if (personIdentification != null) {
			return personIdentification.sex;
		} else {
			return null;
		}
	}

	public Vn vn() {
		if (person != null) {
			return person.vn;
		} else if (personIdentification != null) {
			return personIdentification.vn;
		} else {
			return null;
		}
	}

}
