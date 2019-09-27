package ch.ech.ech0021;

import java.util.ArrayList;

import org.minimalj.model.Keys;

import ch.ech.ech0011.PartnerIdOrganisation;
import ch.ech.ech0097.OrganisationIdentification;
import ch.openech.model.Identification;

// gibt es in ech nicht. Zusammenfassung aus den 3 Relationship klassen
public class RelationshipPartner {
	public static final RelationshipPartner $ = Keys.of(RelationshipPartner.class);

	public final Identification identification = new Identification();
	public ch.ech.ech0010.MailAddress address;

	public ch.ech.ech0044.PersonIdentification getPersonIdentification() {
		if (identification.person != null && !identification.person.isLight()) {
			return identification.person;
		} else {
			return null;
		}
	}

	public void setPersonIdentification(ch.ech.ech0044.PersonIdentification personIdentification) {
		identification.person = personIdentification;
		if (personIdentification != null) {
			identification.organisation = null;
		}
	}

	public ch.ech.ech0044.PersonIdentification getPersonIdentificationPartner() {
		if (identification.person != null && identification.person.isLight()) {
			return identification.person;
		} else {
			return null;
		}
	}

	public void setPersonIdentificationPartner(ch.ech.ech0044.PersonIdentification personIdentification) {
		setPersonIdentification(personIdentification);
	}

	public ch.ech.ech0011.PartnerIdOrganisation getPartnerIdOrganisation() {
		if (identification.organisation != null) {
			PartnerIdOrganisation result = new PartnerIdOrganisation();
			result.localPersonId.namedIdCategory = identification.organisation.localOrganisationId.namedIdCategory;
			result.localPersonId.namedId = identification.organisation.localOrganisationId.namedId;
			if (identification.organisation.OtherOrganisationId != null) {
				result.otherPersonId = new ArrayList<>(identification.organisation.OtherOrganisationId);
			}
			return result;
		}
		return null;
	}

	public void setPartnerIdOrganisation(ch.ech.ech0011.PartnerIdOrganisation partnerIdOrganisation) {
		if (partnerIdOrganisation != null) {
			identification.organisation = new OrganisationIdentification();
			identification.organisation.localOrganisationId.namedIdCategory = partnerIdOrganisation.localPersonId.namedIdCategory;
			identification.organisation.localOrganisationId.namedId = partnerIdOrganisation.localPersonId.namedId;
			if (partnerIdOrganisation.otherPersonId != null) {
				identification.organisation.OtherOrganisationId = new ArrayList<>(partnerIdOrganisation.otherPersonId);
			}
			identification.person = null;
		}
	}
}