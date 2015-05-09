package ch.openech.transaction;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.StringUtils;

import ch.openech.model.common.NamedId;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;

public class EchPersistence {

	public static Person getByIdentification(Backend backend, PersonIdentification personIdentification) {
		if (NamedId.OPEN_ECH_ID_CATEGORY.equals(personIdentification.technicalIds.localId.personIdCategory)) {
			long id = Long.valueOf(personIdentification.technicalIds.localId.personId);
			return backend.read(Person.class, id);
		}
		if (personIdentification.vn != null) {
			List<Person> persons = backend.read(Person.class, Criteria.search(personIdentification.vn.value, Person.SEARCH_BY_VN) , 1);
			if (!persons.isEmpty()) return persons.get(0);
		} 
		List<PersonIdentification> persons = backend.read(PersonIdentification.class, Criteria.search(personIdentification.officialName), 500);
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
		List<Organisation> organisations = backend.read(Organisation.class, Criteria.equals(Organisation.$.uid.value, organisationIdentification.uid.value), 2);
		if (organisations.isEmpty()) {
			organisations = backend.read(Organisation.class, Criteria.equals(Organisation.$.organisationName, organisationIdentification.organisationName), 2);
		}
		if (!organisations.isEmpty()) {
			return organisations.get(0);
		} else {
			return null;
		}
	}
		
}
