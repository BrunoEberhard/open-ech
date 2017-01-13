package ch.openech.test.repository;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.backend.Backend;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonExtendedInformation;
import ch.openech.model.types.YesNo;
import ch.openech.test.server.AbstractServerTest;

public class PersonExtendedInformationRepositoryTest extends AbstractServerTest {

	@Test
	public void insertInformationTest() throws Exception {
		Person person = DataGenerator.person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = YesNo.Yes;
		
		Person readPerson = Backend.save(person);
		Assert.assertEquals(YesNo.Yes, readPerson.personExtendedInformation.armedForcesLiability);
	}

	@Test
	public void updateInformationTest() throws Exception {
		Person person = DataGenerator.person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = YesNo.Yes;
		
		Object newId = Backend.insert(person);
		Person readPerson = Backend.read(Person.class, newId);
		Assert.assertEquals(YesNo.Yes, readPerson.personExtendedInformation.armedForcesLiability);
		
		readPerson.personExtendedInformation.armedForcesService = YesNo.No;
		Person readPerson2 = Backend.save(readPerson);
		
		Assert.assertEquals(YesNo.No, readPerson2.personExtendedInformation.armedForcesService);
	}

}	
