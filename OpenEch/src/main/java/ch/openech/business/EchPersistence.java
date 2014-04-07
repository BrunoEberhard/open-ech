package ch.openech.business;

import java.util.List;

import org.joda.time.ReadablePartial;

import ch.openech.dm.common.NamedId;
import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.organisation.OrganisationIdentification;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.Table;
import ch.openech.mj.server.DbService;
import ch.openech.mj.util.StringUtils;

public class EchPersistence {

	public static Person getByName(DbPersistence persistence, String name, String firstName, ReadablePartial dateOfBirth) {
		Table<Person> person = (Table<Person>) persistence.getTable(Person.class);

		List<Person> persons = person.search(Person.BY_FULLTEXT, name);
		for (int i = persons.size()-1; i>= 0; i--) {
			Person p = persons.get(i);
			if (!StringUtils.isBlank(firstName) && !StringUtils.equals(firstName, p.firstName)) {
				persons.remove(i);
			} else if (dateOfBirth != null && !dateOfBirth.equals(p.dateOfBirth)) {
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
	
	public static Person getByIdentification(DbService dbService, PersonIdentification personIdentification) {
		if (NamedId.OPEN_ECH_ID_CATEGORY.equals(personIdentification.technicalIds.localId.personIdCategory)) {
			long id = Long.valueOf(personIdentification.technicalIds.localId.personId);
			return dbService.read(Person.class, id);
		}
		if (personIdentification.vn != null) {
			List<Person> persons = dbService.search(Person.BY_VN, personIdentification.vn.value);
			if (!persons.isEmpty()) return persons.get(0);
		} 
		List<Person> persons = dbService.search(Person.BY_FULLTEXT, personIdentification.officialName);
		for (Person p : persons) {
			if (StringUtils.equals(p.firstName, personIdentification.firstName)) {
				if (StringUtils.equals(p.officialName, personIdentification.officialName)) {
					return p;
				}
			}
		}
		return null;
	}
	
	public static Organisation getByIdentification(DbService dbService, OrganisationIdentification organisationIdentification) {
		if (NamedId.OPEN_ECH_ID_CATEGORY.equals(organisationIdentification.technicalIds.localId.personIdCategory)) {
			long id = Long.valueOf(organisationIdentification.technicalIds.localId.personId);
			return dbService.read(Organisation.class, id);
		}
		List<Organisation> organisations = dbService.search(Organisation.BY_FULLTEXT, organisationIdentification.organisationName);
		if (!organisations.isEmpty()) {
			return organisations.get(0);
		} else {
			return null;
		}
	}
		
}
