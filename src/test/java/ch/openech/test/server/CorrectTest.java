package ch.openech.test.server;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.minimalj.backend.Backend;
import org.minimalj.transaction.criteria.By;
import org.minimalj.util.Codes;
import org.minimalj.util.IdUtils;

import ch.openech.model.code.NationalityStatus;
import ch.openech.model.code.ResidencePermit;
import ch.openech.model.common.Address;
import ch.openech.model.common.Canton;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.Place;
import ch.openech.model.person.Foreign;
import ch.openech.model.person.Person;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.model.person.types.Religion;
import ch.openech.model.types.Language;
import junit.framework.Assert;

/*
 * Diese Sammlung von Tests betrifft die bei Schema - Version 2.2. neu
 * hinzugekommenen Korrekturmöglichkeiten. 
 */
public class CorrectTest extends AbstractServerTest {

	private static final String vn = "7561829871370";
	private static final String FOREGIN_VN = "7561829871371";

	private static Person p, foreignP;
	
	@Before
	public void createPerson() throws Exception {
		clear();
		
		p = insertPerson(vn);
		foreignP = insertPerson(FOREGIN_VN);
	}
	
	@Test
	public void correctIdentification() throws Exception {
		Person person = reload(p);
		Person changedPerson = reload(p);
		changedPerson.officialName = "TestCorrect2";
		
		process(writer().correctIdentification(person.personIdentification(), changedPerson));
		
		changedPerson = reload(p);
		
		Assert.assertEquals("TestCorrect2", changedPerson.officialName);
	}
	
	@Test
	public void correctNationality0() throws Exception {
		Person person = reload(p);
		person.nationality.nationalityStatus = NationalityStatus.unknown;
		
		process(writer().correctNationality(person));
		
		person = reload(p);
		Assert.assertEquals(NationalityStatus.unknown, person.nationality.nationalityStatus);
	}

	@Test
	public void correctName() throws Exception {
		Person person = reload(foreignP);
		person.aliasName = "Alias2";
		person.foreign.nameOnPassport = "passport1";
		
		process(writer().correctName(person));
		
		person = reload(foreignP);
		Assert.assertEquals("Alias2", person.aliasName);
		Assert.assertEquals("passport1", person.foreign.nameOnPassport);
	}
	
	@Test
	public void correctNationality2() throws Exception {
		Person person = reload(p);
		person.nationality.nationalityStatus = NationalityStatus.with;
		
		CountryIdentification country = Codes.findCode(CountryIdentification.class, 8244);
		person.nationality.nationalityCountry = country;
		
		process(writer().correctNationality(person));
		
		person = reload(p);
		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals(country.countryIdISO2, person.nationality.nationalityCountry.countryIdISO2);
		Assert.assertEquals("Tschechische Republik", person.nationality.nationalityCountry.countryNameShort);
		Assert.assertEquals(Integer.valueOf(8244), person.nationality.nationalityCountry.id);
	}
	
	@Test
	public void correctContact_Address() throws Exception {
		Person person = reload(p);
		person.contactPerson.partner.clear();
		person.contactPerson.address = new Address();
		person.contactPerson.address.lastName = "Kontaktnachname";
		person.contactPerson.address.street = "Kontaktstrasse";
		person.contactPerson.address.town = "Kontaktort";
		
		process(writer().correctContact(person));
		
		person = reload(p);
		Assert.assertTrue(person.contactPerson.partner.isEmpty());
		Assert.assertNotNull(person.contactPerson.address);
		Assert.assertEquals("Kontaktnachname", person.contactPerson.address.lastName);
		Assert.assertEquals("Kontaktstrasse", person.contactPerson.address.street);
		Assert.assertEquals("Kontaktort", person.contactPerson.address.town);
	}

	@Test
	public void correctContact_Person() throws Exception {
		Person person = reload(p);
		Person person2 = reload(foreignP);
		person.contactPerson.partner.setValue(person2);
		person.contactPerson.address = person2.dwellingAddress.mailAddress;
		person.contactPerson.address.lastName = person2.officialName;
		
		process(writer().correctContact(person));
		
		person = reload(p);
		Assert.assertNotNull(person.contactPerson);
		Assert.assertTrue(IdUtils.equals(person2, person.contactPerson.partner.personIdentification));
	}

	
	@Test
	public void correctReligion() throws Exception {
		Person person = reload(p);
		
		process(writer().correctReligion(person, Religion.christ_katolisch));
		
		person = reload(p);
		Assert.assertEquals(Religion.christ_katolisch, person.religion);
	}
	
	@Test
	public void correctOrigin() throws Exception {
		Person person = reload(p);
		PlaceOfOrigin origin1 = new PlaceOfOrigin();
		origin1.canton = Codes.findCode(Canton.class, "TG");
		origin1.originName = "Heimat 1";
		origin1.naturalizationDate = LocalDate.of(1974, 2, 28);
		origin1.expatriationDate = LocalDate.of(2007, 10, 11);
		
		PlaceOfOrigin origin2 = new PlaceOfOrigin();
		origin2.canton = Codes.findCode(Canton.class, "TI");
		origin2.originName = "Heimat 2";
		origin2.naturalizationDate = LocalDate.of(2007, 10, 11);

		person.placeOfOrigin.clear();
		person.placeOfOrigin.add(origin1);
		person.placeOfOrigin.add(origin2);
		
		process(writer().correctOrigin(person));
		
		person = reload(p);
		Assert.assertEquals(2, person.placeOfOrigin.size());
		
		Assert.assertEquals(origin1.display(), person.placeOfOrigin.get(0).display());
		Assert.assertEquals(origin2.display(), person.placeOfOrigin.get(1).display());
	}
	
	@Test
	public void correctResidencePermit() throws Exception {
		Person person = reload(foreignP);
		Foreign foreign = new Foreign();
		foreign.residencePermit = ResidencePermit.Aufenthalter_nach_eu_efta_abkommen;
		foreign.residencePermitTill = LocalDate.of(2020, 10, 11);

		process(writer().correctResidencePermit(person, foreign));
		
		person = reload(foreignP);
		Assert.assertEquals(ResidencePermit.Aufenthalter_nach_eu_efta_abkommen, person.foreign.residencePermit);
		Assert.assertEquals(LocalDate.of(2020, 10, 11), person.foreign.residencePermitTill);	
	}
	
	@Test
	public void correctMaritalData2() throws Exception {
		Person person = reload(p);
		person.maritalStatus.maritalStatus =  ch.openech.model.person.types.MaritalStatus.verheiratet;
		
		process(writer().correctMaritalData(person));
		
		person = reload(p);
		Assert.assertTrue(person.maritalStatus.isVerheiratet());
	}
	
	@Test
	public void correctMaritalData1() throws Exception {
		Person person = reload(p);
		person.maritalStatus.maritalStatus =  ch.openech.model.person.types.MaritalStatus.ledig;
		
		process(writer().correctMaritalData(person));
		
		person = reload(p);
		Assert.assertTrue(person.maritalStatus.isLedig());
	}
	
	@Test
	public void correctPlaceOfBirth_Swiss() throws Exception {
		Person person = reload(p);
		person.placeOfBirth = new Place();
		MunicipalityIdentification jona = Codes.findCode(MunicipalityIdentification.class, 3340);
		person.placeOfBirth.municipalityIdentification = jona;
		
		process(writer().correctPlaceOfBirth(person));
		
		person = reload(p);
		Assert.assertEquals(CountryIdentification.createSwiss(), person.placeOfBirth.countryIdentification);
		Assert.assertEquals(jona.canton, person.placeOfBirth.municipalityIdentification.canton);
		Assert.assertEquals(jona.historyMunicipalityId, person.placeOfBirth.municipalityIdentification.historyMunicipalityId);
		Assert.assertEquals(jona.id, person.placeOfBirth.municipalityIdentification.id);
		Assert.assertEquals(jona.municipalityName, person.placeOfBirth.municipalityIdentification.municipalityName);
	}
	
	@Test
	public void correctPlaceOfBirth_Foreign() throws Exception {
		Person person = reload(p);
		
		List<CountryIdentification> countries = Backend.read(CountryIdentification.class, By.all(), 1000);
		CountryIdentification c10 = countries.get(10);
		person.placeOfBirth = new Place();
		person.placeOfBirth.countryIdentification = c10;
		person.placeOfBirth.foreignTown = "Testtown";
		
		process(writer().correctPlaceOfBirth(person));
		
		person = reload(p);
		Assert.assertEquals(c10.id, person.placeOfBirth.countryIdentification.id);
		Assert.assertEquals(c10.countryIdISO2, person.placeOfBirth.countryIdentification.countryIdISO2);
		Assert.assertEquals(c10.countryNameShort, person.placeOfBirth.countryIdentification.countryNameShort);
		Assert.assertEquals("Testtown", person.placeOfBirth.foreignTown);
	}
	

	@Test
	public void correctDateOfDeath() throws Exception {
		Person person = reload(p);
		person.dateOfDeath = LocalDate.of(2012, 12, 31);
		
		process(writer().correctDateOfDeath(person));
		
		person = reload(p);
		Assert.assertEquals(LocalDate.of(2012, 12, 31), person.dateOfDeath);
	}
	
	@Test
	public void correctDateOfDeathEmpty() throws Exception {
		Person person = reload(p);
		person.dateOfDeath = null;
		
		process(writer().correctDateOfDeath(person));
		
		person = reload(p);
		Assert.assertNull(person.dateOfDeath);
	}

	@Test
	public void correctLanguageOfCorrespondance() throws Exception {
		Person person = reload(p);
		person.languageOfCorrespondance = Language.it;
		
		process(writer().correctLanguageOfCorrespondance(person));
		
		person = reload(p);
		Assert.assertEquals(Language.it, person.languageOfCorrespondance);
	}
	
}
