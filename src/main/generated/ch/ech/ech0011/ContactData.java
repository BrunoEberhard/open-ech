package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.backend.Backend;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.DateUtils;

import ch.ech.ech0010.MailAddress;
import ch.ech.ech0044.PersonIdentification;
import ch.ech.ech0098.Organisation;
import ch.openech.frontend.ech0011.ContactReference;

// handmade
public class ContactData implements Rendering {
	public static final ContactData $ = Keys.of(ContactData.class);

	public PersonIdentification personIdentification;
	public PersonIdentification personIdentificationPartner; // Light
	public PartnerIdOrganisation partnerIdOrganisation;
	@NotEmpty
	public MailAddress contactAddress;
	public LocalDate contactValidFrom;
	public LocalDate contactValidTill;

	public ContactReference getReference() {
		if (Keys.isKeyObject(this))
			return Keys.methodOf(this, "reference");

		ContactReference reference = new ContactReference();
		if (personIdentification != null) {
			String id = personIdentification.localPersonId.namedId;
			reference.person = Backend.read(Person.class, id);
		} else if (personIdentificationPartner != null) {
			String id = personIdentificationPartner.localPersonId.namedId;
			reference.person = Backend.read(Person.class, id);
		} else if (partnerIdOrganisation != null) {
			String id = partnerIdOrganisation.localPersonId.namedId;
			reference.organisation = Backend.read(Organisation.class, id);
		}
		return reference;
	}

	public void setReference(ContactReference reference) {
		if (reference.person != null) {
			Person person = reference.person;
			personIdentificationPartner = null;
			partnerIdOrganisation = null;
			personIdentification = person.personIdentification;
			contactAddress = new MailAddress();
			contactAddress.names.firstName = person.nameData.firstName;
			contactAddress.names.lastName = person.nameData.officialName;
			contactAddress.names.mrMrs = person.personAdditionalData.mrMrs;
			contactAddress.names.title = person.personAdditionalData.title;
			if (person.residenceData != null) {
				CloneHelper.deepCopy(person.residenceData.dwellingAddress.address, contactAddress.addressInformation);
			}
		} else if (reference.organisation != null) {
			personIdentification = null;
			personIdentificationPartner = null;
			Organisation organisation = reference.organisation;
			partnerIdOrganisation = new PartnerIdOrganisation();
			partnerIdOrganisation.localPersonId.namedIdCategory = organisation.organisationIdentification.localOrganisationId.namedIdCategory;
			partnerIdOrganisation.localPersonId.namedId = organisation.organisationIdentification.localOrganisationId.namedId;
			// TODO rest of localPersonId
		}
	}

	@Override
	public CharSequence render() {
		StringBuilder s = new StringBuilder();
		ContactReference reference = getReference();
		if (reference != null) {
			reference.render(s);
		}
		if (contactAddress != null) {
			contactAddress.addressInformation.render(s);
			appendRange(s, contactValidFrom, contactValidTill);
		}
		return s.toString();
	}

	public static void appendRange(StringBuilder stringBuilder, LocalDate from, LocalDate to) {
		if (from != null || to != null) {
			if (from != null && to != null) {
				stringBuilder.append(DateUtils.format(from)).append(" - ").append(DateUtils.format(to));
			} else if (from != null) {
				stringBuilder.append("Ab ").append(DateUtils.format(from));
			} else {
				stringBuilder.append("Bis").append(DateUtils.format(to));
			}
		}
	}

	// support typo in ech 11 v5

	public PartnerIdOrganisation getPartnerIdOrgnisation() {
		return partnerIdOrganisation;
	}

	public void setPartnerIdOrgnisation(PartnerIdOrganisation partnerIdOrganisation) {
		this.partnerIdOrganisation = partnerIdOrganisation;
	}

}