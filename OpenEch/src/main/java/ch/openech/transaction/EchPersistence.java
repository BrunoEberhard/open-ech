package ch.openech.transaction;

import java.util.List;

import org.joda.time.ReadablePartial;
import org.minimalj.backend.Backend;
import org.minimalj.util.StringUtils;

import ch.openech.model.common.NamedId;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;

public class EchPersistence {

	public static Person getByName(Backend backend, String name, String firstName, ReadablePartial dateOfBirth) {
		List<Person> persons = backend.search(Person.class, name, 2); // null: default search
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
	
	public static Person getByIdentification(Backend backend, PersonIdentification personIdentification) {
		if (NamedId.OPEN_ECH_ID_CATEGORY.equals(personIdentification.technicalIds.localId.personIdCategory)) {
			long id = Long.valueOf(personIdentification.technicalIds.localId.personId);
			return backend.read(Person.class, id);
		}
		if (personIdentification.vn != null) {
			List<Person> persons = backend.search(Person.class, Person.SEARCH_BY_VN, personIdentification.vn.value, 1);
			if (!persons.isEmpty()) return persons.get(0);
		} 
		List<PersonIdentification> persons = backend.search(PersonIdentification.class, personIdentification.officialName, 500); // null: default search
		for (PersonIdentification p : persons) {
			if (StringUtils.equals(p.firstName, personIdentification.firstName)) {
				if (StringUtils.equals(p.officialName, personIdentification.officialName)) {
					return backend.read(Person.class, p.id);
				}
			}
		}
		return null;
	}
	
	public static Organisation getByIdentification(Backend backend, OrganisationIdentification organisationIdentification) {
		if (NamedId.OPEN_ECH_ID_CATEGORY.equals(organisationIdentification.technicalIds.localId.personIdCategory)) {
			long id = Long.valueOf(organisationIdentification.technicalIds.localId.personId);
			return backend.read(Organisation.class, id);
		}
		List<Organisation> organisations = backend.search(Organisation.class, organisationIdentification.organisationName, 2);
		if (!organisations.isEmpty()) {
			return organisations.get(0);
		} else {
			return null;
		}
	}
		
}
