package ch.openech.test.server;

import java.util.List;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Test;

import ch.openech.dm.code.FederalRegister;
import ch.openech.dm.code.NationalityStatus;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.types.Sex;
import ch.openech.dm.types.TypeOfResidence;
import ch.openech.mj.server.DbService;
import ch.openech.mj.server.Services;

public class MoveInTest extends AbstractServerTest {

	@Test
	public void moveIn() throws Exception {
		processFile("samples/eCH-0020/moveIn/data_ordipro-moveIn-21.xml");
		
		List<PersonIdentification> identifications = Services.get(DbService.class).search(PersonIdentification.class, "BERNALUSKOVSKI", 2);
		Assert.assertEquals(1, identifications.size());
		Person person = Services.get(DbService.class).read(Person.class, identifications.get(0).id);
		
		Assert.assertNotNull(person);
		Assert.assertEquals("BERNALUSKOVSKI", person.officialName);
		Assert.assertEquals("Emilie", person.firstName);
		Assert.assertEquals(Sex.weiblich, person.sex);
		Assert.assertEquals(new LocalDate(1949, 11, 29), person.dateOfBirth);
		
		Assert.assertEquals("CZ", person.placeOfBirth.countryIdentification.countryIdISO2);
		Assert.assertEquals("Tschechische Republik", person.placeOfBirth.countryIdentification.countryNameShort);

		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals("CZ", person.nationality.nationalityCountry.countryIdISO2);

		Assert.assertTrue(person.maritalStatus.isVerheiratet());

		// TODO Alte residence permit (0006-1 schema) übernehmen
		// Assert.assertEquals("11", person.foreign.residencePermit);
		
		Assert.assertEquals(FederalRegister.Ordipro, person.residence.reportingMunicipality.getFederalRegister());
		Assert.assertEquals(new LocalDate(2009, 4, 23), person.arrivalDate);
		Assert.assertEquals("unknown", person.comesFrom.countryIdentification.countryNameShort);
		Assert.assertEquals("Muristrasse 53", person.dwellingAddress.mailAddress.street);
		Assert.assertEquals("Bern", person.dwellingAddress.mailAddress.town);
		Assert.assertEquals("3006", person.dwellingAddress.mailAddress.zip);
		Assert.assertEquals("CH", person.dwellingAddress.mailAddress.country);

		Assert.assertEquals("1", person.dwellingAddress.typeOfHousehold);
	}

	@Test
	public void moveIn_secondaryResidence() throws Exception {
		Person person = processFile("testPerson/moveIn/personSecondaryResidence.xml");
		Assert.assertEquals(TypeOfResidence.hasSecondaryResidence, person.typeOfResidence);
	}

	@Test
	public void moveIn_otherResidence() throws Exception {
		Person person = processFile("testPerson/moveIn/personOtherResidence.xml");
		Assert.assertEquals(TypeOfResidence.hasOtherResidence, person.typeOfResidence);
	}

	
}
