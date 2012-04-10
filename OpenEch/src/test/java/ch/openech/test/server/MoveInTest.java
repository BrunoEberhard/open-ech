package ch.openech.test.server;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.server.EchServer;
import ch.openech.server.ServerCallResult;

public class MoveInTest extends AbstractServerTest {

	@Test
	public void moveIn() throws Exception {
		processFile("samples/eCH-0020/moveIn/data_ordipro-moveIn-21.xml");
		
		List<Person> persons = EchServer.getInstance().getPersistence().person().find("BERNALUSKOVSKI");
		Assert.assertEquals(1, persons.size());
		Person person = persons.get(0);
		
		Assert.assertNotNull(person);
		Assert.assertEquals("BERNALUSKOVSKI", person.personIdentification.officialName);
		Assert.assertEquals("Emilie", person.personIdentification.firstName);
		Assert.assertEquals("2", person.personIdentification.sex);
		Assert.assertEquals("1949-11-29", person.getDateOfBirth());
		
		Assert.assertEquals("CZ", person.placeOfBirth.countryIdentification.countryIdISO2);
		Assert.assertEquals("Tschechische Republik", person.placeOfBirth.countryIdentification.countryNameShort);

		Assert.assertEquals("2", person.nationality.nationalityStatus);
		Assert.assertEquals("CZ", person.nationality.nationalityCountry.countryIdISO2);

		Assert.assertTrue(person.maritalStatus.isVerheiratet());

		Assert.assertEquals("11", person.foreign.residencePermit);
		
		// Assert.assertEquals("Ordipro", person.residence.reportingMunicipality);
		Assert.assertEquals("2009-04-23", person.arrivalDate);
		Assert.assertEquals("unknown", person.comesFrom.countryIdentification.countryNameShort);
		Assert.assertEquals("Muristrasse 53", person.dwellingAddress.mailAddress.street);
		Assert.assertEquals("Bern", person.dwellingAddress.mailAddress.town);
		Assert.assertEquals("3006", person.dwellingAddress.mailAddress.zip.swissZipCode);
		Assert.assertEquals("CH", person.dwellingAddress.mailAddress.country);

		Assert.assertEquals("1", person.dwellingAddress.typeOfHousehold);
	}

	@Test
	public void moveIn_secondaryResidence() throws Exception {
		ServerCallResult result = processFile("testPerson/moveIn/personSecondaryResidence.xml");
		String id = result.createdPersonId;
		
		Person person = load(id);
		
		Assert.assertEquals("2", person.typeOfResidence);
	}

	@Test
	public void moveIn_otherResidence() throws Exception {
		ServerCallResult result = processFile("testPerson/moveIn/personOtherResidence.xml");
		String id = result.createdPersonId;
		
		Person person = load(id);
		
		Assert.assertEquals("3", person.typeOfResidence);
	}

	
}
