package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.backend.Backend;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

import ch.ech.ech0010.MailAddress;
import ch.ech.ech0010.PersonMailAddressInfo;
import ch.ech.ech0098.Organisation;
import ch.openech.frontend.ech0011.ContactReference;

// handmade
public class ContactData {
	public static final ContactData $ = Keys.of(ContactData.class);

	public ch.ech.ech0044.PersonIdentification personIdentification;
	public ch.ech.ech0044.PersonIdentification personIdentificationPartner; // Light
	public PartnerIdOrganisation partnerIdOrganisation;
	@NotEmpty
	public ch.ech.ech0010.MailAddress contactAddress;
	public LocalDate contactValidFrom;
	public LocalDate contactValidTill;

	private transient ContactReference reference;

	public ContactReference getReference() {
		if (Keys.isKeyObject(this))
			return Keys.methodOf(this, "reference");

		if (reference == null) {
			reference = new ContactReference();
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
		}
		return reference;
	}

	public void setReference(ContactReference reference) {
		if (this.reference != reference) {
			if (reference.person != null) {
				Person person = reference.person;
				personIdentificationPartner = null;
				partnerIdOrganisation = null;
				personIdentification = person.personIdentification;
				contactAddress = new MailAddress();
				contactAddress.organisation = null;
				contactAddress.person = new PersonMailAddressInfo();
				contactAddress.person.firstName = person.nameData.firstName;
				contactAddress.person.lastName = person.nameData.officialName;
				contactAddress.person.mrMrs = person.personAdditionalData.mrMrs;
				contactAddress.person.title = person.personAdditionalData.title;
				if (person.residenceData != null) {
					contactAddress.addressInformation.street = person.residenceData.dwellingAddress.address.street;
					// TODO rest of address
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
	}

	// support typo in ech 11 v5

	public PartnerIdOrganisation getPartnerIdOrgnisation() {
		return partnerIdOrganisation;
	}

	public void setPartnerIdOrgnisation(PartnerIdOrganisation partnerIdOrganisation) {
		this.partnerIdOrganisation = partnerIdOrganisation;
	}

}