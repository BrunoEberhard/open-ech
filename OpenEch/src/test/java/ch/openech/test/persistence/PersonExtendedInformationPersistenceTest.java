package ch.openech.test.persistence;

import junit.framework.Assert;

import org.junit.Test;
import org.minimalj.backend.db.DbBackend;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonExtendedInformation;
import ch.openech.model.types.YesNo;
import ch.openech.test.server.AbstractServerTest;

public class PersonExtendedInformationPersistenceTest extends AbstractServerTest {

	@Test
	public void insertInformationTest() throws Exception {
		Person person = DataGenerator.person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = YesNo.Yes;
		
		Object id = DbBackend.getInstance().insert(person);
		
		Person readPerson = DbBackend.getInstance().read(Person.class, id);
		Assert.assertEquals(YesNo.Yes, readPerson.personExtendedInformation.armedForcesLiability);
	}

	@Test
	public void updateInformationTest() throws Exception {
		Person person = DataGenerator.person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = YesNo.Yes;
		
		Object id = DbBackend.getInstance().insert(person);
		
		Person readPerson = DbBackend.getInstance().read(Person.class, id);
		Assert.assertEquals(YesNo.Yes, readPerson.personExtendedInformation.armedForcesLiability);
		
		readPerson.personExtendedInformation.armedForcesService = YesNo.No;
		DbBackend.getInstance().update(readPerson);
		
		Person readPerson2 = DbBackend.getInstance().read(Person.class, id);
				
		Assert.assertEquals(YesNo.No, readPerson2.personExtendedInformation.armedForcesService);
	}

}	
