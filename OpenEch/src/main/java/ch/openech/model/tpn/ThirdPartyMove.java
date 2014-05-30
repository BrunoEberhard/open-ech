package  ch.openech.model.tpn;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.threeten.bp.LocalDate;

import ch.openech.model.common.Address;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.contact.ContactEntry;

// e46
public class ThirdPartyMove {

	public static final ThirdPartyMove THIRD_PARTY_MOVE = Keys.of(ThirdPartyMove.class);
	
	public final List<ContactEntry> contractor = new ArrayList<ContactEntry>();
	
	// Person
	public String officialName, callName;
	public DatePartiallyKnown dateOfBirth = new DatePartiallyKnown();
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
