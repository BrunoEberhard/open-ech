package ch.openech.server;

import java.sql.SQLException;

import ch.openech.dm.EchFormats;
import ch.openech.dm.contact.Contact;
import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.tpn.ThirdPartyMove;
import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.Table;

public class EchPersistence extends DbPersistence {

	private final Table<PersonIdentification> personIdentification;
	private final PersonTable person;
	private final Table<Contact> contact;
	private final Table<Organisation> organisation = null;
	private final Table<ThirdPartyMove> thirdPartyMove = null;

	public EchPersistence() throws SQLException {
		EchFormats.initialize();
		
		personIdentification = addClass(PersonIdentification.class);
		
		person = new PersonTable(this);
		add(person);
		
		contact = addClass(Contact.class);
		add(contact);

		// organisation = addClass(Organisation.class);
		// thirdPartyMove = addClass(ThirdPartyMove.class);
		
		connect();
	}

	public Table<PersonIdentification> personIdentification() {
		return personIdentification;
	}

	public PersonTable person() {
		return person;
	}

	public Table<Contact> contact() {
		return contact;
	}

	public Table<Organisation> organisation() {
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