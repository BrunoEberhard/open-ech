package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

// handmade
public class ContactData {
	public static final ContactData $ = Keys.of(ContactData.class);

	public ch.ech.ech0044.PersonIdentification personIdentification;
	public ch.ech.ech0044.PersonIdentificationLight personIdentificationPartner;
	public PartnerIdOrganisation partnerIdOrganisation;
	@NotEmpty
	public ch.ech.ech0010.MailAddress contactAddress;
	public LocalDate contactValidFrom;
	public LocalDate contactValidTill;
	
	// support typo in ech 11 v5
	
	public PartnerIdOrganisation getPartnerIdOrgnisation() {
		return partnerIdOrganisation;
	}
	
	public void setPartnerIdOrgnisation(PartnerIdOrganisation partnerIdOrganisation) {
		this.partnerIdOrganisation = partnerIdOrganisation;
	}
}