package ch.openech.test.persistence;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.dm.types.YesNo;
import ch.openech.server.EchPersistence;
import ch.openech.server.EchServer;

public class PersonExtendedInformationPersistenceTest {

	private static EchPersistence persistence = EchServer.getInstance().getPersistence();
	
	@Test
	public void insertInformationTest() throws Exception {
		Person person = DataGenerator.person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = YesNo.Yes;
		
		int id = persistence.person().insert(person);
		
		Person readPerson = persistence.person().read(id);
		Assert.assertEquals(YesNo.Yes, readPerson.personExtendedInformation.armedForcesLiability);
		persistence.commit();
	}

	@Test
	public void updateInformationTest() throws Exception {
		Person person = DataGenerator.person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = YesNo.Yes;
		
		int id = persistence.person().insert(person);
		
		Person readPerson = persistence.person().read(id);
		Assert.assertEquals(YesNo.Yes, readPerson.personExtendedInformation.armedForcesLiability);
		
		readPerson.personExtendedInformation.armedForcesService = YesNo.No;
		persistence.person().update(readPerson);
		
		Person readPerson2 = persistence.person().read(id);
				
		Assert.assertEquals(YesNo.No, readPerson2.personExtendedInformation.armedForcesService);
		persistence.commit();
	}

}	
