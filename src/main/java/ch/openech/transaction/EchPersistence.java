package ch.openech.transaction;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.IdUtils;
import org.minimalj.util.StringUtils;

import ch.openech.model.organisation.Organisation;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;

public class EchPersistence {

	public static Person getByIdentification(Backend backend, PersonIdentification personIdentification) {
		String localId = null;
		if (personIdentification.technicalIds.localId.openEch()) {
			localId = personIdentification.technicalIds.localId.personId;
			if (StringUtils.isBlank(localId)) {
				localId = null;
			}
			if (localId != null && localId.length() == 36) {
				Person person = backend.read(Person.class, localId);
				if (person != null) {
					return person;
				}
			}
		}
		if (personIdentification.vn != null) {
			List<Person> persons = backend.read(Person.class, Criteria.search(personIdentification.vn.value, Person.SEARCH_BY_VN) , 1);
			if (localId != null) {
				for (Person person : persons) {
					if (IdUtils.getCompactIdString(person).startsWith(localId)) {
						return person;
					}
				}
			} else {
				if (!persons.isEmpty()) return persons.get(0);
			}
		} 
		List<Person> persons = backend.read(Person.class, Criteria.search(personIdentification.officialName), 500);
		for (Person person : persons) {
			if (localId == null || IdUtils.getCompactIdString(person).startsWith(localId)) {
				if (StringUtils.equals(person.firstName, personIdentification.firstName)) {
					if (StringUtils.equals(person.officialName, personIdentification.officialName)) {
						return backend.read(Person.class, person.id);
					}
				}
			}
		}
		return null;
	}
	
	public static Organisation getByIdentification(Backend backend, OrganisationIdentification organisationIdentification) {
		String localId = null;
		if (organisationIdentification.technicalIds.localId.openEch()) {
			localId = organisationIdentification.technicalIds.localId.personId;
			if (StringUtils.isBlank(localId)) {
				localId = null;
			}
			if (localId != null && localId.length() == 36) {
				Organisation organisation = backend.read(Organisation.class, localId);
				if (organisation != null) {
					return organisation;
				}
			}
		}
		List<Organisation> organisations = backend.read(Organisation.class, Criteria.equals(Organisation.$.uid.value, organisationIdentification.uid.value), 2);
		if (organisations.isEmpty()) {
			organisations = backend.read(Organisation.class, Criteria.equals(Organisation.$.organisationName, organisationIdentification.organisationName), 2);
		}
		for (Organisation organisation : organisations) {
			if (localId == null || IdUtils.getCompactIdString(organisation).startsWith(localId)) {
				return organisation;
			}
		}
		return null;
	}
		
}
