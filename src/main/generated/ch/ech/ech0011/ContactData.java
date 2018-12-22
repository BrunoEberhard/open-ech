package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;

import ch.ech.ech0010.MailAddress;
import ch.ech.ech0098.Organisation;

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

	public class ContactReference implements Rendering {

		@Override
		public CharSequence render() {
			if (personIdentification != null) {
				return personIdentification.officialName;
			} else if (personIdentificationPartner != null) {
				return personIdentificationPartner.officialName;
			} else if (partnerIdOrganisation != null) {
				return partnerIdOrganisation.id.toString();
			} else {
				return "-";
			}
		}

		public void setPerson(Person person) {
			personIdentification = person.personIdentification;
			personIdentificationPartner = null;
			partnerIdOrganisation = null;
			contactAddress = new MailAddress();
			contactAddress.names.firstName = person.nameData.firstName;
			contactAddress.names.lastName = person.nameData.officialName;
			contactAddress.names.mrMrs = person.personAdditionalData.mrMrs;
			contactAddress.names.title = person.personAdditionalData.title;
			if (person.residenceData != null) {
				contactAddress.addressInformation.street = person.residenceData.dwellingAddress.address.street;
				// TODO rest of address
			}
		}

		public void setOrganisation(Organisation organisation) {
			personIdentification = null;
			personIdentificationPartner = null;
			partnerIdOrganisation = new PartnerIdOrganisation();
			partnerIdOrganisation.localPersonId.namedIdCategory = organisation.organisationIdentification.localOrganisationId.namedIdCategory;
			partnerIdOrganisation.localPersonId.namedId = organisation.organisationIdentification.localOrganisationId.namedId;
			// TODO rest of localPersonId
		}
	}

	public ContactReference getReference() {
		if (Keys.isKeyObject(this))
			return Keys.methodOf(this, "reference");

		if (reference == null) {
			reference = new ContactReference();
		}
		return reference;
	}

	// support typo in ech 11 v5

	public PartnerIdOrganisation getPartnerIdOrgnisation() {
		return partnerIdOrganisation;
	}

	public void setPartnerIdOrgnisation(PartnerIdOrganisation partnerIdOrganisation) {
		this.partnerIdOrganisation = partnerIdOrganisation;
	}

}