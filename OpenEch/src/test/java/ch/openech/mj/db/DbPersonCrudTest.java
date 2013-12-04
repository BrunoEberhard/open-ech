package ch.openech.mj.db;

import java.sql.SQLException;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.openech.dm.common.Address;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.dm.types.MrMrs;
import ch.openech.dm.types.Sex;
import ch.openech.server.EchPersistence;

public class DbPersonCrudTest {

	private static EchPersistence persistence;

	@BeforeClass
	public static void setupDb() throws SQLException {
		persistence = new EchPersistence(DbPersistence.embeddedDataSource());
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
		
		int id = addressTable.getOrCreateId(address);
		
		Address readAddress = (Address)addressTable.read(id);
		
		Assert.assertEquals(address.mrMrs, readAddress.mrMrs);
		Assert.assertEquals(address.street, readAddress.street);
		Assert.assertEquals(address.houseNumber.houseNumber, readAddress.houseNumber.houseNumber);
		Assert.assertEquals(address.country, readAddress.country);
	
		int id2 = addressTable.getOrCreateId(address);
		
		Assert.assertEquals(id, id2);
		
		readAddress.houseNumber.houseNumber = "11";
		int id3 = addressTable.getOrCreateId(readAddress);

		Assert.assertNotSame(id, id3);
	}
	
	@Test
	public void testCrudCountry() throws SQLException {
		ImmutableTable<CountryIdentification> countryTable = persistence.getImmutableTable(CountryIdentification.class);
		
		CountryIdentification country = new CountryIdentification();
		country.countryId = null;
		country.countryIdISO2 = "DE";
		country.countryNameShort = "Deutschland";
		
		int id = countryTable.getOrCreateId(country);
		
		CountryIdentification readCountry = (CountryIdentification)countryTable.read(id);
		
		Assert.assertEquals(country.countryId, readCountry.countryId);
		Assert.assertEquals(country.countryIdISO2, readCountry.countryIdISO2);
		Assert.assertEquals(country.countryNameShort, readCountry.countryNameShort);
	
		int id2 = countryTable.getOrCreateId(country);
		
		Assert.assertEquals(id, id2);
		
		readCountry.countryIdISO2 = "GE";
		int id3 = countryTable.getOrCreateId(readCountry);

		Assert.assertNotSame(id, id3);
	}
	
	@Test
	public void testCrudPerson() throws SQLException {
		Table<Person> personTable = persistence.person();
		
		Person person = new Person();
		person.personIdentification.officialName = "Eberhard";
		person.personIdentification.firstName = "Bruno";
		person.personIdentification.dateOfBirth = new LocalDate(1974, 8, 2);
		person.personIdentification.vn.value = "123";
		person.personIdentification.sex = Sex.maennlich;
		
		person.aliasName = "Biwi";
		
		int id = personTable.insert(person);
		
		Person readPerson = (Person)personTable.read(id);
		
		Assert.assertEquals(person.personIdentification.officialName, readPerson.personIdentification.officialName);
		Assert.assertEquals(person.aliasName, readPerson.aliasName);
		
		readPerson.aliasName = "Biwidus";
		
		personTable.update(readPerson);
		
		Person readPerson2 = (Person)personTable.read(id);
		
		Assert.assertEquals(readPerson.aliasName, readPerson2.aliasName);
	}
	
	@Test
	public void testInsertPerson2() throws SQLException {
		Person person = new Person();
		person.personIdentification.officialName = "Eberhard";
		person.personIdentification.firstName = "Bruno";
		person.personIdentification.dateOfBirth = new LocalDate(1974, 8, 2);
		person.personIdentification.vn.value = "123";
		person.personIdentification.sex = Sex.maennlich;

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
		
		int id = persistence.person().insert(person);
		
		Person readPerson = persistence.person().read(id);
		
		Assert.assertEquals(2, readPerson.occupation.size());
		
		Occupation readOccupation1 = readPerson.occupation.get(0);
		Assert.assertEquals(occupation.jobTitle, readOccupation1.jobTitle);
		
		Occupation readOccupation2 = readPerson.occupation.get(1);
		Assert.assertEquals(occupation2.placeOfEmployer.houseNumber.dwellingNumber, readOccupation2.placeOfEmployer.houseNumber.dwellingNumber);
		Assert.assertEquals(occupation2.placeOfEmployer.addressLine1, readOccupation2.placeOfEmployer.addressLine1);

	}

	@Test
	public void testUpdatePerson() throws SQLException {
		Person person = new Person();
		person.personIdentification.officialName = "Eberhard";
		person.personIdentification.firstName = "Bruno";
		person.personIdentification.dateOfBirth = new LocalDate(1974, 8, 2);
		person.personIdentification.vn.value = "123";
		person.personIdentification.sex = Sex.maennlich;

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
		
		int id = persistence.person().insert(person);
		
		Person readPerson = persistence.person().read(id);
		readPerson.aliasName = "biwi";
		readPerson.occupation.remove(1);
		persistence.person().update(readPerson);

		Person readPerson2 = persistence.person().read(id);
		Assert.assertEquals("biwi", readPerson2.aliasName);
		Assert.assertEquals(1, readPerson2.occupation.size());
	}

	
	@Test
	public void testUpdateSubTable() throws SQLException {
		Person person = new Person();
		person.personIdentification.officialName = "Eberhard";
		person.personIdentification.firstName = "Bruno";
		person.personIdentification.dateOfBirth = new LocalDate(1974, 8, 2);
		person.personIdentification.vn.value = "123";
		person.personIdentification.sex = Sex.maennlich;
		
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
		
		int id = persistence.person().insert(person);
		
		Person readPerson = persistence.person().read(id);
		Occupation readOccupation2 = readPerson.occupation.get(1);
		readOccupation2.placeOfEmployer.addressLine1 = "Ne andere Line";
		
		persistence.person().update(readPerson);

		Person readPerson2 = persistence.person().read(id);
		Assert.assertEquals(readOccupation2.placeOfEmployer.addressLine1, readPerson2.occupation.get(1).placeOfEmployer.addressLine1);
	
//		List<Integer> versions = persistence.person().readVersions(id);
//		for (Integer i : versions) {
//			System.out.println(i);
//		}
	}
	
	@Test
	public void testChangePersonIdentification() throws SQLException {
		Person person = new Person();
		person.personIdentification.officialName = "Eberhard";
		person.personIdentification.firstName = "Bruno";
		person.personIdentification.dateOfBirth = new LocalDate(1974, 8, 2);
		person.personIdentification.vn.value = "123";
		person.personIdentification.sex = Sex.maennlich;

		int id = persistence.person().insert(person);
		
		Person readPerson = persistence.person().read(id);
		readPerson.personIdentification.officialName = "Äberhard";
		persistence.person().update(readPerson);
		
		Person readPerson2 = persistence.person().read(id);
		Assert.assertEquals(readPerson.personIdentification.officialName, readPerson2.personIdentification.officialName);
		readPerson2.personIdentification.officialName = "Eberhardt";
		persistence.person().update(readPerson2);
		
		Person readPerson3 = persistence.person().read(id);
		Assert.assertEquals(readPerson2.personIdentification.officialName, readPerson3.personIdentification.officialName);
	}
	
}
