package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.code.NationalityStatus;
import ch.openech.dm.code.ResidencePermit;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Swiss;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.types.Religion;
import ch.openech.dm.types.Language;

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
		person.nationality.nationalityCountry.countryIdISO2 = "CX";
		person.nationality.nationalityCountry.countryNameShort = "CX Name";
		person.nationality.nationalityCountry.countryId = 4242;
		
		process(writer().correctNationality(person));
		
		person = reload(p);
		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals("CX", person.nationality.nationalityCountry.countryIdISO2);
		Assert.assertEquals("CX Name", person.nationality.nationalityCountry.countryNameShort);
		Assert.assertEquals(Integer.valueOf(4242), person.nationality.nationalityCountry.countryId);
	}
	
	@Test
	public void correctContact_Address() throws Exception {
		Person person = reload(p);
		person.contactPerson.person = null;
		person.contactPerson.address = new Address();
		person.contactPerson.address.lastName = "Kontaktnachname";
		person.contactPerson.address.street = "Kontaktstrasse";
		person.contactPerson.address.town = "Kontaktort";
		
		process(writer().correctContact(person));
		
		person = reload(p);
		Assert.assertNull(person.contactPerson.person);
		Assert.assertNotNull(person.contactPerson.address);
		Assert.assertEquals("Kontaktnachname", person.contactPerson.address.lastName);
		Assert.assertEquals("Kontaktstrasse", person.contactPerson.address.street);
		Assert.assertEquals("Kontaktort", person.contactPerson.address.town);
	}

	@Test
	public void correctContact_Person() throws Exception {
		Person person = reload(p);
		Person person2 = reload(foreignP);
		person.contactPerson.person = person2.personIdentification();
		person.contactPerson.address = person2.dwellingAddress.mailAddress;
		person.contactPerson.address.lastName = person2.officialName;
		
		process(writer().correctContact(person));
		
		person = reload(p);
		Assert.assertNotNull(person.contactPerson);
		Assert.assertEquals(person2.id, person.contactPerson.person.id);
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
		origin1.cantonAbbreviation.canton = "TG";
		origin1.originName = "Heimat 1";
		origin1.naturalizationDate = new LocalDate(1974, 2, 28);
		origin1.expatriationDate = new LocalDate(2007, 10, 11);
		
		PlaceOfOrigin origin2 = new PlaceOfOrigin();
		origin2.cantonAbbreviation.canton = "TI";
		origin2.originName = "Heimat 2";
		origin2.naturalizationDate = new LocalDate(2007, 10, 11);

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
		foreign.residencePermitTill = new LocalDate(2020, 10, 11);

		process(writer().correctResidencePermit(person, foreign));
		
		person = reload(foreignP);
		Assert.assertEquals(ResidencePermit.Aufenthalter_nach_eu_efta_abkommen, person.foreign.residencePermit);
		Assert.assertEquals(new LocalDate(2020, 10, 11), person.foreign.residencePermitTill);	
	}
	
	@Test
	public void correctMaritalData2() throws Exception {
		Person person = reload(p);
		person.maritalStatus.maritalStatus = ch.openech.dm.person.types.MaritalStatus.verheiratet;
		
		process(writer().correctMaritalData(person));
		
		person = reload(p);
		Assert.assertTrue(person.maritalStatus.isVerheiratet());
	}
	
	@Test
	public void correctMaritalData1() throws Exception {
		Person person = reload(p);
		person.maritalStatus.maritalStatus = ch.openech.dm.person.types.MaritalStatus.ledig;
		
		process(writer().correctMaritalData(person));
		
		person = reload(p);
		Assert.assertTrue(person.maritalStatus.isLedig());
	}
	
	@Test
	public void correctPlaceOfBirth_Swiss() throws Exception {
		Person person = reload(p);
		person.placeOfBirth.municipalityIdentification = new MunicipalityIdentification();
		person.placeOfBirth.municipalityIdentification.cantonAbbreviation.canton = "SG";
		person.placeOfBirth.municipalityIdentification.historyMunicipalityId = 20;
		person.placeOfBirth.municipalityIdentification.municipalityId = 2;
		person.placeOfBirth.municipalityIdentification.municipalityName = "Testwil";
		
		process(writer().correctPlaceOfBirth(person));
		
		person = reload(p);
		Assert.assertEquals(Swiss.createCountryIdentification().toStringReadable(), person.placeOfBirth.countryIdentification.toStringReadable());
		Assert.assertEquals("SG", person.placeOfBirth.municipalityIdentification.cantonAbbreviation.canton);
		Assert.assertEquals(Integer.valueOf(20), person.placeOfBirth.municipalityIdentification.historyMunicipalityId);
		Assert.assertEquals(Integer.valueOf(2), person.placeOfBirth.municipalityIdentification.municipalityId);
		Assert.assertEquals("Testwil", person.placeOfBirth.municipalityIdentification.municipalityName);
	}
	
	@Test
	public void correctPlaceOfBirth_Foreign() throws Exception {
		Person person = reload(p);
		person.placeOfBirth.countryIdentification.countryId = 4242;
		person.placeOfBirth.countryIdentification.countryIdISO2 = "CZ";
		person.placeOfBirth.countryIdentification.countryNameShort = "CZ Name";
		person.placeOfBirth.foreignTown = "Testtown";
		
		process(writer().correctPlaceOfBirth(person));
		
		person = reload(p);
		Assert.assertEquals(Integer.valueOf(4242), person.placeOfBirth.countryIdentification.countryId);
		Assert.assertEquals("CZ", person.placeOfBirth.countryIdentification.countryIdISO2);
		Assert.assertEquals("CZ Name", person.placeOfBirth.countryIdentification.countryNameShort);
		Assert.assertEquals("Testtown", person.placeOfBirth.foreignTown);
	}
	

	@Test
	public void correctDateOfDeath() throws Exception {
		Person person = reload(p);
		person.dateOfDeath = new LocalDate(2012, 12, 31);
		
		process(writer().correctDateOfDeath(person));
		
		person = reload(p);
		Assert.assertEquals(new LocalDate(2012, 12, 31), person.dateOfDeath);
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
