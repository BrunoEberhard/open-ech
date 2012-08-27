package ch.openech.dm.person;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.Date;
import ch.openech.dm.common.Address;
import ch.openech.mj.db.model.annotation.Is;

public class ContactPerson {

	public PersonIdentification person;
	public Address address;
	@Is(Date)
	public String validTill;

}
