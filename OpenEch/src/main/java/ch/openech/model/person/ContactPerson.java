package  ch.openech.model.person;

import org.threeten.bp.LocalDate;

import ch.openech.model.common.Address;

public class ContactPerson {

	public Object id;
	
	public PersonIdentification person;
	public Address address;
	public LocalDate validTill;

}
