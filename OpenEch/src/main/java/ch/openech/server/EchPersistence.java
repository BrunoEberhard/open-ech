package ch.openech.server;

import java.sql.SQLException;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.tpn.ThirdPartyMove;
import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.HistorizedTable;
import ch.openech.mj.db.Table;

public class EchPersistence extends DbPersistence {

	private final HistorizedTable<PersonIdentification> personIdentification;
	private final PersonTable person;
	private final HistorizedTable<Contact> contact;
	private final OrganisationTable organisation;
	private final Table<ThirdPartyMove> thirdPartyMove = null;

	public EchPersistence() throws SQLException {
		personIdentification = addHistorizedClass(PersonIdentification.class);
		
		person = new PersonTable(this);
		add(person);
		
		contact = addHistorizedClass(Contact.class);
		add(contact);

		organisation = new OrganisationTable(this);
		add(organisation);
		// thirdPartyMove = addClass(ThirdPartyMove.class);
		
		connect();
	}

	public HistorizedTable<PersonIdentification> personIdentification() {
		return personIdentification;
	}

	public PersonTable person() {
		return person;
	}

	public HistorizedTable<Contact> contact() {
		return contact;
	}

	public OrganisationTable organisation() {
		return organisation;
	}
	
	public Table<ThirdPartyMove> thirdPartyMove() {
		return thirdPartyMove;
	}

//	personIdentification = getImmutableTable(PersonIdentification.class);
//
//public ImmutableTable<PersonIdentification> personIdentification() {
//	return personIdentification;
//}
	
}
