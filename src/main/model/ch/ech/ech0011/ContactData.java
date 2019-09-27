package ch.ech.ech0011;

import java.time.LocalDate;
import java.util.ArrayList;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;

import ch.ech.ech0010.MailAddress;
import ch.ech.ech0097.OrganisationIdentification;
import ch.openech.frontend.ech0011.RangeUtil;
import ch.openech.model.Identification;

// handmade
public class ContactData implements Rendering {
	public static final ContactData $ = Keys.of(ContactData.class);

	public final Identification identification = new Identification();
	@NotEmpty
	public MailAddress contactAddress;
	public LocalDate contactValidFrom;
	public LocalDate contactValidTill;

	@Override
	public CharSequence render() {
		StringBuilder s = new StringBuilder();
		CharSequence identificationStr = identification.render();
		if (identificationStr != null) {
			s.append(identificationStr).append('\n');
		}
		if (contactAddress != null) {
			contactAddress.names.render(s);
			contactAddress.addressInformation.render(s);
			RangeUtil.appendRangeLine(s, contactValidFrom, contactValidTill);
		}
		return s.toString();
	}

	//

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

	// support typo in ech 11 v5

	public PartnerIdOrganisation getPartnerIdOrgnisation() {
		return getPartnerIdOrganisation();
	}

	public void setPartnerIdOrgnisation(PartnerIdOrganisation partnerIdOrganisation) {
		setPartnerIdOrganisation(partnerIdOrganisation);
	}

}