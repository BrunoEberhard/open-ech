package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.types.Religion;

public class DivTest extends AbstractServerTest {

	private static final String vn = "7561829871378";
	private String id;
	
	@Before
	public void createPerson() throws Exception {
		id = insertPerson(vn);
	}

	@Test
	public void move() throws Exception {
		processFile("testPerson/div/moveTest.xml");
		
		Person person = load(id);
		
		Assert.assertNotNull(person);
		Assert.assertEquals("Zeile 1", person.dwellingAddress.mailAddress.addressLine1);
		Assert.assertEquals("Zeile 2", person.dwellingAddress.mailAddress.addressLine2);
		Assert.assertEquals("Bahnhofstrasse", person.dwellingAddress.mailAddress.street);
		Assert.assertEquals("1", person.dwellingAddress.mailAddress.houseNumber.houseNumber);
		Assert.assertEquals("2", person.dwellingAddress.mailAddress.houseNumber.dwellingNumber);
		Assert.assertEquals("Gebiet", person.dwellingAddress.mailAddress.locality);
		Assert.assertEquals("Herdern", person.dwellingAddress.mailAddress.town);
		Assert.assertEquals(new Integer(8535), person.dwellingAddress.mailAddress.zip.swissZipCode);
		Assert.assertEquals(new Integer(4837), person.dwellingAddress.mailAddress.zip.swissZipCodeId);
		Assert.assertEquals("CH", person.dwellingAddress.mailAddress.country);

		Assert.assertEquals("1", person.dwellingAddress.typeOfHousehold);
		Assert.assertEquals(new LocalDate(2010, 8, 30), person.dwellingAddress.movingDate);
	}
	
	@Test
	public void addressLock1() throws Exception {
		Person person = load(id);
		person.dataLock = "1";
		process(writer().addressLock(person));
		person = load(id);
		Assert.assertEquals("1", person.dataLock);
	}
	
	@Test
	public void addressLock2() throws Exception {
		Person person = load(id);
		person.dataLock = "2";
		process(writer().addressLock(person));
		person = load(id);
		Assert.assertEquals(Integer.valueOf(2), person.dataLock);
	}
	
	@Test
	public void addressLock0() throws Exception {
		Person person = load(id);
		person.dataLock = "0";
		process(writer().addressLock(person));
		person = load(id);
		Assert.assertEquals("0", person.dataLock);
	}
	
	@Test
	public void paperLock1() throws Exception {
		Person person = load(id);
		person.paperLock = "1";
		process(writer().paperLock(person));
		person = load(id);
		Assert.assertEquals("1", person.paperLock);
	}

	@Test
	public void paperLock0() throws Exception {
		Person person = load(id);
		person.paperLock = "0";
		process(writer().paperLock(person));
		person = load(id);
		Assert.assertEquals("0", person.paperLock);
	}

	@Test
	public void changeReligion() throws Exception {
		Person person = load(id);
		Assert.assertEquals(Religion.unbekannt, person.religion);
		person.paperLock = "0";
		process(writer().changeReligion(person.personIdentification, Religion.evangelisch));
		person = load(id);
		Assert.assertEquals(Religion.evangelisch, person.religion);
	}
	
	@Test
	public void contact() throws Exception {
		processFile("testPerson/div/contactTest.xml");
		Person person = load(id);
		Assert.assertEquals("Zeile 1", person.contactPerson.address.addressLine1);
		Assert.assertEquals("Zeile 2", person.contactPerson.address.addressLine2);
		Assert.assertEquals("Bahnhofstrasse", person.contactPerson.address.street);
		Assert.assertEquals("1000", person.contactPerson.address.houseNumber.houseNumber);
		Assert.assertEquals("5A", person.contactPerson.address.houseNumber.dwellingNumber);
		Assert.assertEquals(new Integer(1234), person.contactPerson.address.postOfficeBoxNumber);
		Assert.assertEquals("POSTFACH", person.contactPerson.address.postOfficeBoxText);
		Assert.assertEquals("Gebiet", person.contactPerson.address.locality);
		Assert.assertEquals("Vessy", person.contactPerson.address.town);
		Assert.assertEquals(new Integer(1234), person.contactPerson.address.zip.swissZipCode);
		Assert.assertEquals(new Integer(452), person.contactPerson.address.zip.swissZipCodeId);
		Assert.assertEquals("CH", person.contactPerson.address.country);
	}
	
}
