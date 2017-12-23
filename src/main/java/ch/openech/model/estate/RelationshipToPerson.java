package ch.openech.model.estate;

import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

import ch.openech.model.common.Address;
import ch.openech.model.contact.ContactEntry;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.person.PersonIdentificationLight;

public class RelationshipToPerson {

	@Size(100) @NotEmpty
	public String role;
	
	public PersonIdentificationLight personIdentification;
	public OrganisationIdentification organisationIdentification;
	
	@NotEmpty
	public Address address;
	public ContactEntry email, phone;
	public Address deputy;
}
