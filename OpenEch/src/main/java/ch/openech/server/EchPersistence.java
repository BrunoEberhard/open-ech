package ch.openech.server;

import static ch.openech.dm.organisation.Organisation.*;
import static ch.openech.dm.person.Person.*;

import java.util.List;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.joda.time.ReadablePartial;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.organisation.OrganisationIdentification;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.tpn.ThirdPartyMove;
import ch.openech.mj.db.ColumnIndexUnqiue;
import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.HistorizedTable;
import ch.openech.mj.db.MultiIndex;
import ch.openech.mj.db.Table;
import ch.openech.mj.model.Codes;
import ch.openech.mj.util.StringUtils;

public class EchPersistence extends DbPersistence {

	public final HistorizedTable<Person> person;
	public final MultiIndex<Person> personFulltextIndex;
	public final ColumnIndexUnqiue<Person> personVnIndex;
	public final ColumnIndexUnqiue<Person> personLocalPersonIdIndex;
	
	public final HistorizedTable<Organisation> organisation;
	public final MultiIndex<Organisation> organisationFulltextIndex;
	public final ColumnIndexUnqiue<Organisation> organisationLocalIdIndex;
	
	private final Table<ThirdPartyMove> thirdPartyMove = null;

	private static final Object[] PERSON_INDEX_KEYS = {
		PERSON.personIdentification.officialName, //
		PERSON.personIdentification.firstName, //
		PERSON.personIdentification.dateOfBirth, //
		PERSON.aliasName, //
		PERSON.personIdentification.vn.value, //
	};
	
	private static final Object[] ORGANISATION_INDEX_KEYS = {
		ORGANISATION.identification.organisationName, //
	};

	static {
		// the addCodes may only be executed once, even if more
		// than one EchPersistence is created (happens in JUnit Tests)
		Codes.addCodes(ResourceBundle.getBundle("ch.openech.dm.organisation.types.ech_organisation"));
		Codes.addCodes(ResourceBundle.getBundle("ch.openech.dm.person.types.ech_person"));
	}
	
	public EchPersistence(DataSource dataSource) {
		super(dataSource);

		addImmutableClass(PersonIdentification.class);
		addImmutableClass(MunicipalityIdentification.class);
		addImmutableClass(CountryIdentification.class);
		
		person = addHistorizedClass(Person.class);
		personFulltextIndex = person.createFulltextIndex(PERSON_INDEX_KEYS);
		personVnIndex = person.createIndexUnique(PERSON.personIdentification.vn.value);
		personLocalPersonIdIndex = person.createIndexUnique(PERSON.personIdentification.technicalIds.localId.personId);
		
		addImmutableClass(OrganisationIdentification.class);
		
		organisation = addHistorizedClass(Organisation.class);
		organisationFulltextIndex = organisation.createFulltextIndex(ORGANISATION_INDEX_KEYS);
		organisationLocalIdIndex = organisation.createIndexUnique(ORGANISATION.identification.technicalIds.localId.personId);

		// addClass(ThirdPartyMove.class);
	}

	@Override
	protected boolean createTablesOnInitialize() {
		return true;
	}

	public HistorizedTable<Person> person() {
		return person;
	}

	public MultiIndex<Person> personIndex() {
		return personFulltextIndex;
	}

	public ColumnIndexUnqiue<Person> personVnIndex() {
		return personVnIndex;
	}

	public ColumnIndexUnqiue<Person> personLocalPersonIdIndex() {
		return personLocalPersonIdIndex;
	}

	public Person getByName(String name, String firstName, ReadablePartial dateOfBirth) {
		List<Person> persons = personIndex().findObjects(name);
		for (int i = persons.size()-1; i>= 0; i--) {
			Person person = persons.get(i);
			if (!StringUtils.isBlank(firstName) && !StringUtils.equals(firstName, person.personIdentification.firstName)) {
				persons.remove(i);
			} else if (dateOfBirth != null && !dateOfBirth.equals(person.personIdentification.dateOfBirth)) {
				persons.remove(i);
			}
		}
		if (persons.size() > 1) {
			throw new IllegalArgumentException("Zuweisung über Namen und Geburtstag nicht eindeutig für: " + name + ", " + firstName + ", " + dateOfBirth);
		}
		if (!persons.isEmpty()) {
			return persons.get(0);
		} else {
			return null;
		}
	}
	
	public Person getByIdentification(PersonIdentification personIdentification) {
		if (personIdentification.technicalIds.localId.personId != null) {
			Person person = personLocalPersonIdIndex.find(personIdentification.technicalIds.localId.personId);
			if (person != null) return person;
		} 
		if (personIdentification.vn != null) {
			Person person = personVnIndex().find(personIdentification.vn.value);
			if (person != null) return person;
		} 
		// TODO getByIdentification für Sedex verbessern
		List<Person> persons = personIndex().findObjects(personIdentification.firstName + " " + personIdentification.officialName);
		if (!persons.isEmpty()) {
			return persons.get(0);
		} else {
			return null;
		}
	}

	public HistorizedTable<Organisation> organisation() {
		return organisation;
	}

	public ColumnIndexUnqiue<Organisation> organisationLocalIdIndex() {
		return organisationLocalIdIndex;
	}

	public MultiIndex<Organisation> organisationIndex() {
		return organisationFulltextIndex;
	}
	
	public Table<ThirdPartyMove> thirdPartyMove() {
		return thirdPartyMove;
	}

}
