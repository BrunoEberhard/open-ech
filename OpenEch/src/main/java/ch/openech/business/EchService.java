package ch.openech.business;

import java.io.InputStream;
import java.io.OutputStream;

import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.Person;

public interface EchService {

	public Person process(String... xmlStrings);

	public Organisation processOrg(String... xmlStrings);

	boolean generateDemoPersons(String version, int number);

	boolean generateDemoOrganisations(String version, int number);

	String imprt(InputStream inputStream); // (client überträgt zu server)

	void export(String ewkVersion, OutputStream outputStream); // (server überträgt zu client)

	void exportKeys(String ewkVersion, OutputStream outputStream);
	
	String importOrg(InputStream inputStream);

	void exportOrg(String ewkVersion, OutputStream outputStream); // (server überträgt zu client)

	void exportOrgKeys(String ewkVersion, OutputStream outputStream);
	
}