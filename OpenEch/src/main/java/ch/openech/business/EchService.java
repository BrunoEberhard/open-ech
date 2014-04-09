package ch.openech.business;

import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.Person;

public interface EchService {

	public Person process(String... xmlStrings);

	public Organisation processOrg(String... xmlStrings);

	boolean generateDemoPersons(String version, int number);

	boolean generateDemoOrganisations(String version, int number);

}