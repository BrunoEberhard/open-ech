package ch.openech.test.persistence;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.server.EchPersistence;

public class PersonExtendedInformationPersistenceTest {

	private static EchPersistence persistence;
	
	@BeforeClass
	public static void beforeClass() throws SQLException {
		persistence = new EchPersistence();
		persistence.connect();
	}

	
	@AfterClass
	public static void afterClass() throws SQLException {
		persistence.commit();
		persistence.disconnect();
	}

	@Test
	public void insertInformationTest() throws Exception {
		Person person = new Person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = "1";
		
		int id = persistence.person().insert(person);
		
		Person readPerson = persistence.person().read(id);
		Assert.assertEquals("1", readPerson.personExtendedInformation.armedForcesLiability);
		persistence.commit();
	}

	@Test
	public void updateInformationTest() throws Exception {
		Person person = new Person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = "1";
		
		int id = persistence.person().insert(person);
		
		Person readPerson = persistence.person().read(id);
		readPerson.personExtendedInformation.armedForcesLiability = "0";
		
		persistence.person().update(readPerson);
		
		Person readPerson2 = persistence.person().read(id);
				
		Assert.assertEquals("0", readPerson2.personExtendedInformation.armedForcesService);
		persistence.commit();
	}

}	
