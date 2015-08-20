package  ch.openech.model.person;

import java.time.LocalDate;

import ch.openech.model.common.Address;

public class ContactPerson {

	public final PartnerIdentification partner = new PartnerIdentification();
	
	public Address address;
	public LocalDate validTill;

}
