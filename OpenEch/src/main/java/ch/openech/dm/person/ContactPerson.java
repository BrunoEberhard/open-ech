package ch.openech.dm.person;

import ch.openech.dm.common.Address;
import ch.openech.mj.db.model.annotation.Date;

public class ContactPerson {

	public PersonIdentification person;
	public Address address;
	@Date
	public String validTill;

}
