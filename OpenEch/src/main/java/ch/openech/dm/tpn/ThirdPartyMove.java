package ch.openech.dm.tpn;

import org.joda.time.LocalDate;

import ch.openech.dm.common.Address;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.contact.Contact;
import ch.openech.mj.model.Constants;

// e46
public class ThirdPartyMove {

	public static final ThirdPartyMove THIRD_PARTY_MOVE = Constants.of(ThirdPartyMove.class);
	
	public Contact contractor;
	
	// Person
	public String officialName, callName;
	// (partialAllowed = false)
	public LocalDate dateOfBirth;
	public String sex;
	public CountryIdentification nationality;
	public LocalDate beginOfContract, endOfContract;
	
	// Building
	public String EGID;
	public final Address address = new Address();
	
	// Dwelling
	public String EWID;
	public String administrativeDwellingId, floor, positionOnFloor, roomCount;
	
	// comes/goes
	public Address comesFrom, goesTo;
	
}
