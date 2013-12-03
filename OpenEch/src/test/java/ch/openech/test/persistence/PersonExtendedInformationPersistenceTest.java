package ch.openech.test.persistence;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.dm.types.YesNo;
import ch.openech.mj.db.DbPersistence;
import ch.openech.server.EchPersistence;

public class PersonExtendedInformationPersistenceTest {

	private static EchPersistence persistence;

	@BeforeClass
	public static void setupDb() throws SQLException {
		persistence = new EchPersistence(DbPersistence.embeddedDataSource());
		persistence.createTables();
	}
	
	@Test
	public void insertInformationTest() throws Exception {
		Person person = DataGenerator.person();
		person.personExtendedInformation = new PersonExtendedInformation();
		person.personExtendedInformation.armedForcesLiability = YesNo.Yes;
		
		int id = persistence.person().insert(person);
		
		Person readPerson = persistence.person().read(id);
		Assert.assertEquals(YesNo.Yes, readPerson.personExtendedInformation.armedForcesLiability);
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
	}

}	
