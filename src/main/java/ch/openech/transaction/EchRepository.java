package ch.openech.transaction;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.repository.query.By;
import org.minimalj.security.Subject;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.IdUtils;
import org.minimalj.util.StringUtils;

import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;

public class EchRepository {

	public static Person getByIdentification(PersonIdentification personIdentification) {
		String localId = null;
		if (personIdentification.technicalIds.localId.openEch()) {
			localId = personIdentification.technicalIds.localId.personId;
			if (StringUtils.isBlank(localId)) {
				localId = null;
			}
			if (localId != null && localId.length() == 36) {
				Person person = Backend.read(Person.class, localId);
				if (person != null) {
					return person;
				}
			}
		}
		if (personIdentification.vn != null && personIdentification.vn.value != null) {
			List<Person> persons = Backend.find(Person.class, By.search(personIdentification.vn.value, Person.SEARCH_BY_VN).limit(1));
			if (localId != null) {
				for (Person person : persons) {
					if (getCompactIdString(person).startsWith(localId)) {
						return person;
					}
				}
			} else {
				if (!persons.isEmpty()) return persons.get(0);
			}
		} 
		List<Person> persons = Backend.find(Person.class, By.search(personIdentification.officialName).limit(500));
		for (Person person : persons) {
			if (localId == null || getCompactIdString(person).startsWith(localId)) {
				if (StringUtils.equals(person.firstName, personIdentification.firstName)) {
					if (StringUtils.equals(person.officialName, personIdentification.officialName)) {
						return Backend.read(Person.class, person.id);
					}
				}
			}
		}
		return null;
	}
	
	public static Organisation getByIdentification(OrganisationIdentification organisationIdentification) {
		String localId = null;
		if (organisationIdentification.technicalIds.localId.openEch()) {
			localId = organisationIdentification.technicalIds.localId.personId;
			if (StringUtils.isBlank(localId)) {
				localId = null;
			}
			if (localId != null && localId.length() == 36) {
				Organisation organisation = Backend.read(Organisation.class, localId);
				if (organisation != null) {
					return organisation;
				}
			}
		}
		List<Organisation> organisations = Backend.find(Organisation.class, By.field(Organisation.$.uid.value, organisationIdentification.uid.value).limit(2));
		if (organisations.isEmpty()) {
			organisations = Backend.find(Organisation.class, By.field(Organisation.$.organisationName, organisationIdentification.organisationName).limit(2));
		}
		for (Organisation organisation : organisations) {
			if (localId == null || getCompactIdString(organisation).startsWith(localId)) {
				return organisation;
			}
		}
		return null;
	}
	
	public static OpenEchPreferences getPreferences() {
		Subject subject = Subject.getCurrent();
		if (subject != null && !StringUtils.isEmpty(subject.getName())) {
			List<OpenEchPreferences> preferences = Backend.find(OpenEchPreferences.class, By.field(OpenEchPreferences.$.user, subject.getName()).limit(2));
			if (preferences.size() > 1) {
				throw new IllegalStateException("Too many preference rows for " + subject.getName());
			} else if (preferences.size() == 1) {
				return preferences.get(0);
			}
		}
		return CloneHelper.newInstance(OpenEchPreferences.class);
	}
	
	public static void savePreferences(OpenEchPreferences preferences) {
		if (Subject.getCurrent() != null) {
			preferences.user = Subject.getCurrent().getName();
			if (preferences.id != null) {
				Backend.update(preferences);
			} else {
				Backend.insert(preferences);
			}
		}
	}
	
	/**
	 * Get the value of the <code>id</code> field as String.
	 * Leaves out all 'filler' characters. For an UUID this would
	 * be the '-' characters
	 * 
	 * @param object object containing the id. Must not be <code>null</code>
	 * @return the value of the <code>id</code> field as String
	 */
	public static String getCompactIdString(Object object) {
		return IdUtils.getIdString(object).replace("-", "");
	}
		
}