package ch.ech.ech0021;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;

import ch.ech.ech0044.PersonIdentification;

//handmade
public class MaritalRelationship implements Rendering {
	public static final MaritalRelationship $ = Keys.of(MaritalRelationship.class);

	public static class Partner {
		public static final Partner $ = Keys.of(Partner.class);

		private PersonIdentification person;
		public ch.ech.ech0010.MailAddress address;

		//

		public PersonIdentification getPersonIdentification() {
			if (person != null && !person.isLight()) {
				return person;
			} else {
				return null;
			}
		}

		public void setPersonIdentification(PersonIdentification personIdentification) {
			person = personIdentification;
		}

		public PersonIdentification getPersonIdentificationPartner() {
			if (person != null && person.isLight()) {
				return person;
			} else {
				return null;
			}
		}

		public void setPersonIdentificationPartner(PersonIdentification personIdentification) {
			setPersonIdentification(personIdentification);
		}

	}

	public final RelationshipPartner partner = new RelationshipPartner();
	@NotEmpty
	public TypeOfRelationship typeOfRelationship;

	@Override
	public CharSequence render() {
		return partner.identification.render();
	}
}