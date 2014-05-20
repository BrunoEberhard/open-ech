package ch.openech.mj.db;

import java.sql.SQLException;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.minimalj.backend.db.DbPersistence;
import org.minimalj.backend.db.ImmutableTable;
import org.minimalj.backend.db.Table;

import  ch.openech.model.common.Address;
import  ch.openech.model.common.CountryIdentification;
import  ch.openech.model.person.Occupation;
import  ch.openech.model.person.Person;
import  ch.openech.model.types.MrMrs;
import  ch.openech.model.types.Sex;

public class DbPersonCrudTest {

	private static DbPersistence persistence;

	@BeforeClass
	public static void setupDb() throws SQLException {
		persistence = new DbPersistence(DbPersistence.embeddedDataSource(), Person.class);
	}

	@Test
	public void testCrud() throws SQLException {
		ImmutableTable<Address> addressTable = persistence.getImmutableTable(Address.class);
		
		Address address = new Address();
		address.street = "Grütstrasse";
		address.mrMrs = MrMrs.Herr;
		address.houseNumber.houseNumber = "10";
		address.town = "Jona";
		address.country = "CH";
		
		long id = addressTable.getOrCreateId(address);
		
		Address readAddress = (Address)addressTable.read(id);
		
		Assert.assertEquals(address.mrMrs, readAddress.mrMrs);
		Assert.assertEquals(address.street, readAddress.street);
		Assert.assertEquals(address.houseNumber.houseNumber, readAddress.houseNumber.houseNumber);
		Assert.assertEquals(address.country, readAddress.country);
	
		long id2 = addressTable.getOrCreateId(address);
		
		Assert.assertEquals(id, id2);
		
		readAddress.houseNumber.houseNumber = "11";
		long id3 = addressTable.getOrCreateId(readAddress);

		Assert.assertNotSame(id, id3);
	}
	
	@Test
	public void testCrudCountry() throws SQLException {
		ImmutableTable<CountryIdentification> countryTable = persistence.getImmutableTable(CountryIdentification.class);
		
		CountryIdentification country = new CountryIdentification();
		country.countryId = null;
		country.countryIdISO2 = "DE";
		country.countryNameShort = "Deutschland";
		
		long id = countryTable.getOrCreateId(country);
		
		CountryIdentification readCountry = (CountryIdentification)countryTable.read(id);
		
		Assert.assertEquals(country.countryId, readCountry.countryId);
		Assert.assertEquals(country.countryIdISO2, readCountry.countryIdISO2);
		Assert.assertEquals(country.countryNameShort, readCountry.countryNameShort);
	
		long id2 = countryTable.getOrCreateId(country);
		
		Assert.assertEquals(id, id2);
		
		readCountry.countryIdISO2 = "GE";
		long id3 = countryTable.getOrCreateId(readCountry);

		Assert.assertNotSame(id, id3);
	}
	
	@Test
	public void testCrudPerson() throws SQLException {
		Table<Person> personTable = (Table<Person>) persistence.getTable(Person.class);
		
		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth = new LocalDate(1974, 8, 2);
		person.vn.value = "123";
		person.sex = Sex.maennlich;
		
		person.aliasName = "Biwi";
		
		long id = personTable.insert(person);
		
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
		Table<Person> personTable = (Table<Person>) persistence.getTable(Person.class);

		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth = new LocalDate(1974, 8, 2);
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
		
		long id = personTable.insert(person);
		
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
		Table<Person> personTable = (Table<Person>) persistence.getTable(Person.class);

		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth = new LocalDate(1974, 8, 2);
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
		
		long id = personTable.insert(person);
		
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
		Table<Person> personTable = (Table<Person>) persistence.getTable(Person.class);

		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth = new LocalDate(1974, 8, 2);
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
		
		long id = personTable.insert(person);
		
		Person readPerson = personTable.read(id);
		Occupation readOccupation2 = readPerson.occupation.get(1);
		readOccupation2.placeOfEmployer.addressLine1 = "Ne andere Line";
		
		personTable.update(readPerson);

		Person readPerson2 = personTable.read(id);
		Assert.assertEquals(readOccupation2.placeOfEmployer.addressLine1, readPerson2.occupation.get(1).placeOfEmployer.addressLine1);
	
//		List<Integer> versions = persistence.person().readVersions(id);
//		for (Integer i : versions) {
//			System.out.println(i);
//		}
	}
	
	@Test
	public void testChangePersonIdentification() throws SQLException {
		Table<Person> personTable = (Table<Person>) persistence.getTable(Person.class);

		Person person = new Person();
		person.officialName = "Eberhard";
		person.firstName = "Bruno";
		person.dateOfBirth = new LocalDate(1974, 8, 2);
		person.vn.value = "123";
		person.sex = Sex.maennlich;

		long id = personTable.insert(person);
		
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
	
}
