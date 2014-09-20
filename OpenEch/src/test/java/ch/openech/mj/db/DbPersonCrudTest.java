package ch.openech.mj.db;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minimalj.backend.db.DbPersistence;
import org.minimalj.backend.db.Table;

import ch.openech.model.common.Address;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.Swiss;
import ch.openech.model.person.Occupation;
import ch.openech.model.person.Person;
import ch.openech.model.types.MrMrs;
import ch.openech.model.types.Sex;

public class DbPersonCrudTest {

	private static DbPersistence persistence;

	@BeforeClass
	public static void setupDb() throws SQLException {
		persistence = new DbPersistence(DbPersistence.embeddedDataSource(), Person.class);
		persistence.insert(Swiss.createCountryIdentification());
	}

	@Test
	public void testCrud() throws SQLException {
		Table<Address> addressTable = persistence.getTable(Address.class);
		
		Address address = new Address();
		address.street = "Grütstrasse";
		address.mrMrs = MrMrs.Herr;
		address.houseNumber.houseNumber = "10";
		address.town = "Jona";
		address.country = "CH";
		
		Object id = addressTable.insert(address);
		
		Address readAddress = (Address)addressTable.read(id);
		
		Assert.assertEquals(address.mrMrs, readAddress.mrMrs);
		Assert.assertEquals(address.street, readAddress.street);
		Assert.assertEquals(address.houseNumber.houseNumber, readAddress.houseNumber.houseNumber);
		Assert.assertEquals(address.country, readAddress.country);
		
		readAddress.houseNumber.houseNumber = "11";
		addressTable.update(readAddress);

		readAddress = (Address)addressTable.read(id);
		Assert.assertEquals("11", readAddress.houseNumber.houseNumber);
	}
	
	@Test
	public void testCrudCountry() throws SQLException {
		Table<CountryIdentification> countryTable = persistence.getTable(CountryIdentification.class);
		
		CountryIdentification country = new CountryIdentification();
		country.id = 123;
		country.countryIdISO2 = "DE";
		country.countryNameShort = "Deutschland";
		
		countryTable.insert(country);
		
		CountryIdentification readCountry = (CountryIdentification)countryTable.read(123);
		
		Assert.assertNotNull(readCountry);
		Assert.assertEquals(country.id, readCountry.id);
		Assert.assertEquals(country.countryIdISO2, readCountry.countryIdISO2);
		Assert.assertEquals(country.countryNameShort, readCountry.countryNameShort);
	
		countryTable.update(country);
	}
	
	@Test
	public void testCrudPerson() throws SQLException {
		Table<Person> personTable = persistence.getTable(Person.class);
		
		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth.value = "1974-08-02";
		person.vn.value = "123";
		person.sex = Sex.maennlich;
		
		person.aliasName = "Biwi";
		
		Object id = personTable.insert(person);
		
		Person readPerson = (Person)personTable.read(id);
		
		Assert.assertEquals(person.officialName, readPerson.officialName);
		Assert.assertEquals(person.aliasName, readPerson.aliasName);
		
		readPerson.aliasName = "Biwidus";
		
		personTable.update(readPerson);
		
		Person readPerson2 = (Person)personTable.read(id);
		
		Assert.assertEquals(readPerson.aliasName, readPerson2.aliasName);
	}
	
	@Test
	public void testInsertPerson2() throws SQLException {
		Table<Person> personTable = persistence.getTable(Person.class);

		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth.value = "1974-08-02";
		person.vn.value = "123";
		person.sex = Sex.maennlich;

		Occupation occupation = new Occupation();
		occupation.jobTitle = "Gango";
		person.occupation.add(occupation);
		
		Occupation occupation2 = new Occupation();
		occupation2.jobTitle = "Holmir";
		
		Address address = new Address();
		address.addressLine1 = "Ne Linie";
		address.houseNumber.dwellingNumber = "5";
		address.town = "Jona";
		occupation2.placeOfEmployer = address;
		
		person.occupation.add(occupation2);
		
		Object id = personTable.insert(person);
		
		Person readPerson = personTable.read(id);
		
		Assert.assertEquals(2, readPerson.occupation.size());
		
		Occupation readOccupation1 = readPerson.occupation.get(0);
		Assert.assertEquals(occupation.jobTitle, readOccupation1.jobTitle);
		
		Occupation readOccupation2 = readPerson.occupation.get(1);
		Assert.assertEquals(occupation2.placeOfEmployer.houseNumber.dwellingNumber, readOccupation2.placeOfEmployer.houseNumber.dwellingNumber);
		Assert.assertEquals(occupation2.placeOfEmployer.addressLine1, readOccupation2.placeOfEmployer.addressLine1);

	}

	@Test
	public void testUpdatePerson() throws SQLException {
		Table<Person> personTable = persistence.getTable(Person.class);

		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth.value = "1974-08-02";
		person.vn.value = "123";
		person.sex = Sex.maennlich;

		Occupation occupation = new Occupation();
		occupation.jobTitle = "Gango";
		person.occupation.add(occupation);
		
		Occupation occupation2 = new Occupation();
		occupation2.jobTitle = "Holmir";
		
		Address address = new Address();
		address.addressLine1 = "Ne Linie";
		address.houseNumber.dwellingNumber = "5";
		address.town = "Jona";
		occupation2.placeOfEmployer = address;
		
		person.occupation.add(occupation2);
		
		Object id = personTable.insert(person);
		
		Person readPerson = personTable.read(id);
		readPerson.aliasName = "biwi";
		readPerson.occupation.remove(1);
		personTable.update(readPerson);

		Person readPerson2 = personTable.read(id);
		Assert.assertEquals("biwi", readPerson2.aliasName);
		Assert.assertEquals(1, readPerson2.occupation.size());
	}

	
	@Test
	public void testUpdateSubTable() throws SQLException {
		Table<Person> personTable = persistence.getTable(Person.class);

		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth.value = "1974-08-02";
		person.vn.value = "123";
		person.sex = Sex.maennlich;
		
		Occupation occupation = new Occupation();
		occupation.jobTitle = "Gango";
		person.occupation.add(occupation);
		
		Occupation occupation2 = new Occupation();
		occupation2.jobTitle = "Holmir";
		
		Address address = new Address();
		address.addressLine1 = "Ne Linie";
		address.houseNumber.dwellingNumber = "5";
		address.town = "Jona";
		occupation2.placeOfEmployer = address;
		
		person.occupation.add(occupation2);
		
		Object id = personTable.insert(person);
		
		Person readPerson = personTable.read(id);
		Occupation readOccupation2 = readPerson.occupation.get(1);
		readOccupation2.placeOfEmployer.addressLine1 = "Ne andere Line";
		
		personTable.update(readPerson);

		Person readPerson2 = personTable.read(id);
		Assert.assertEquals(readOccupation2.placeOfEmployer.addressLine1, readPerson2.occupation.get(1).placeOfEmployer.addressLine1);
	}
	
	@Test
	public void testChangePersonIdentification() throws SQLException {
		Table<Person> personTable = persistence.getTable(Person.class);

		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth.value = "1974-08-02";
		person.vn.value = "123";
		person.sex = Sex.maennlich;

		Object id = personTable.insert(person);
		
		Person readPerson = personTable.read(id);
		readPerson.officialName = "Äberhard";
		personTable.update(readPerson);
		
		Person readPerson2 = personTable.read(id);
		Assert.assertEquals(readPerson.officialName, readPerson2.officialName);
		readPerson2.officialName = "Eberhardt";
		personTable.update(readPerson2);
		
		Person readPerson3 = personTable.read(id);
		Assert.assertEquals(readPerson2.officialName, readPerson3.officialName);
	}
	
//	@Test @Ignore("Not possible as long as id an UUID")
//	public void testMaxPerson() throws SQLException {
//		int startMaxId = persistence.execute(Integer.class, "select max(id) from Person");
//		Table<Person> personTable = persistence.getTable(Person.class);
//		
//		Person person = new Person();
//		person.officialName = "Eberhard";
//		person.firstName = "Bruno";
//		person.dateOfBirth.value = "1974-08-02";
//		person.vn.value = "123";
//		person.sex = Sex.maennlich;
//		
//		person.aliasName = "Biwi";
//		
//		personTable.insert(person);
//		
//		int afterInsertMaxId = persistence.execute(Integer.class, "select max(id) from Person");
//		Assert.assertEquals(startMaxId + 1, afterInsertMaxId);
//	}
}
