package ch.openech.test.persistence;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minimalj.backend.db.DbPersistence;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonExtendedInformation;
import ch.openech.model.types.YesNo;

public class PersonExtendedInformationPersistenceTest {

	private static DbPersistence persistence;

	@BeforeClass
	public static void setupDb() throws SQLException {
		persistence = new DbPersistence(DbPersistence.embeddedDataSource(), Person.class);
	}
	
	@Test
	public void insertInformationTest() throws Exception {
		Person person = DataGenerator.person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = YesNo.Yes;
		
		long id = persistence.getTable(Person.class).insert(person);
		
		Person readPerson = persistence.getTable(Person.class).read(id);
		Assert.assertEquals(YesNo.Yes, readPerson.personExtendedInformation.armedForcesLiability);
	}

	@Test
	public void updateInformationTest() throws Exception {
		Person person = DataGenerator.person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = YesNo.Yes;
		
		long id = persistence.getTable(Person.class).insert(person);
		
		Person readPerson = persistence.getTable(Person.class).read(id);
		Assert.assertEquals(YesNo.Yes, readPerson.personExtendedInformation.armedForcesLiability);
		
		readPerson.personExtendedInformation.armedForcesService = YesNo.No;
		persistence.getTable(Person.class).update(readPerson);
		
		Person readPerson2 = persistence.getTable(Person.class).read(id);
				
		Assert.assertEquals(YesNo.No, readPerson2.personExtendedInformation.armedForcesService);
	}

}	
