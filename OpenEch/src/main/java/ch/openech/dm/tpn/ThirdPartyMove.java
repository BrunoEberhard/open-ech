package ch.openech.dm.tpn;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.Date;
import static ch.openech.mj.db.model.annotation.PredefinedFormat.DatePartially;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.contact.Contact;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;

// e46
public class ThirdPartyMove {

	public static final ThirdPartyMove THIRD_PARTY_MOVE = Constants.of(ThirdPartyMove.class);
	
	public Contact contractor;
	
	// Person
	public String officialName, callName;
	@Is(DatePartially)
	public String dateOfBirth;
	public String sex;
	public CountryIdentification nationality;
	
	@Is(Date)
	public String beginOfContract, endOfContract;
	
	// Building
	public String EGID;
	public final Address address = new Address();
	
	// Dwelling
	public String EWID;
	public String administrativeDwellingId, floor, positionOnFloor, roomCount;
	
	// comes/goes
	public Address comesFrom, goesTo;
	
}
