package ch.openech.test.server;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.backend.Backend;
import org.minimalj.repository.criteria.By;

import ch.openech.model.code.FederalRegister;
import ch.openech.model.code.NationalityStatus;
import ch.openech.model.person.Person;
import ch.openech.model.person.types.TypeOfHousehold;
import ch.openech.model.types.Sex;
import ch.openech.model.types.TypeOfResidence;

public class MoveInTest extends AbstractServerTest {

	@Test
	public void moveIn() throws Exception {
		processFile("samples/eCH-0020/moveIn/data_ordipro-moveIn-21.xml");
		
		List<Person> persons = Backend.read(Person.class, By.search("BERNALUSKOVSKI"), 2);
		Assert.assertEquals(1, persons.size());
		Person person = Backend.read(Person.class, persons.get(0).id);
		
		Assert.assertNotNull(person);
		Assert.assertEquals("BERNALUSKOVSKI", person.officialName);
		Assert.assertEquals("Emilie", person.firstName);
		Assert.assertEquals(Sex.weiblich, person.sex);
		Assert.assertEquals(LocalDate.of(1949, 11, 29), person.dateOfBirth.toLocalDate());
		
		Assert.assertEquals("CZ", person.placeOfBirth.countryIdentification.countryIdISO2);
		Assert.assertEquals("Tschechische Republik", person.placeOfBirth.countryIdentification.countryNameShort);

		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals("CZ", person.nationality.nationalityCountry.countryIdISO2);

		Assert.assertTrue(person.maritalStatus.isVerheiratet());

		// TODO Alte residence permit (0006-1 schema) übernehmen
		// Assert.assertEquals("11", person.foreign.residencePermit);
		
		Assert.assertEquals(FederalRegister.Ordipro, person.residence.reportingMunicipality.getFederalRegister());
		Assert.assertEquals(LocalDate.of(2009, 4, 23), person.arrivalDate);
		Assert.assertTrue(person.comesFrom.isUnknown());
		Assert.assertEquals("Muristrasse 53", person.dwellingAddress.mailAddress.street);
		Assert.assertEquals("Bern", person.dwellingAddress.mailAddress.town);
		Assert.assertEquals("3006", person.dwellingAddress.mailAddress.zip);
		Assert.assertEquals("CH", person.dwellingAddress.mailAddress.country);

		Assert.assertEquals(TypeOfHousehold.privathaushalt, person.dwellingAddress.typeOfHousehold);
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
