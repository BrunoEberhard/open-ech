package ch.openech.dm.tpn;

import org.joda.time.LocalDate;
import org.joda.time.ReadablePartial;

import ch.openech.dm.common.Address;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.contact.Contact;
import ch.openech.mj.model.Keys;

// e46
public class ThirdPartyMove {

	public static final ThirdPartyMove THIRD_PARTY_MOVE = Keys.of(ThirdPartyMove.class);
	
	public Contact contractor;
	
	// Person
	public String officialName, callName;
	public ReadablePartial dateOfBirth;
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
