package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.common.Address;
import ch.openech.dm.common.Swiss;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.PlaceOfOrigin;

/*
 * Diese Sammlung von Tests betrifft die bei Schema - Version 2.2. neu
 * hinzugekommenen Korrekturmöglichkeiten. 
 */
public class CorrectTest extends AbstractServerTest {

	private static final String vn = "7561829871370";
	private static final String FOREGIN_VN = "7561829871371";

	private static String id, foreignId;
	
	@Before
	public void createPerson() throws Exception {
		id = insertPerson(vn);
		foreignId = insertPerson(FOREGIN_VN);
	}
	
	@Test
	public void correctIdentification() throws Exception {
		PersonIdentification personIdentification = load(id).personIdentification;
		Person person = load(id);
		person.personIdentification.officialName = "TestCorrect2";
		
		process(writer().correctIdentification(personIdentification, person));
		
		person = load(id);
		
		Assert.assertEquals("TestCorrect2", person.personIdentification.officialName);
	}
	
	@Test
	public void correctNationality0() throws Exception {
		Person person = load(id);
		person.nationality.nationalityStatus = "0";
		
		process(writer().correctNationality(person));
		
		person = load(id);
		Assert.assertEquals("0", person.nationality.nationalityStatus);
	}

	@Test
	public void correctName() throws Exception {
		Person person = load(foreignId);
		person.aliasName = "Alias2";
		person.foreign.nameOnPassport = "passport1";
		
		process(writer().correctName(person));
		
		person = load(foreignId);
		Assert.assertEquals("Alias2", person.aliasName);
		Assert.assertEquals("passport1", person.foreign.nameOnPassport);
	}
	
	@Test
	public void correctNationality2() throws Exception {
		Person person = load(id);
		person.nationality.nationalityStatus = "2";
		person.nationality.nationalityCountry.countryIdISO2 = "CX";
		person.nationality.nationalityCountry.countryNameShort = "CX Name";
		person.nationality.nationalityCountry.countryId = "4242";
		
		process(writer().correctNationality(person));
		
		person = load(id);
		Assert.assertEquals("2", person.nationality.nationalityStatus);
		Assert.assertEquals("CX", person.nationality.nationalityCountry.countryIdISO2);
		Assert.assertEquals("CX Name", person.nationality.nationalityCountry.countryNameShort);
		Assert.assertEquals("4242", person.nationality.nationalityCountry.countryId);
	}
	
	@Test
	public void correctContact_Address() throws Exception {
		Person person = load(id);
		person.contactPerson.person = null;
		person.contactPerson.address = new Address();
		person.contactPerson.address.lastName = "Kontaktnachname";
		person.contactPerson.address.street = "Kontaktstrasse";
		person.contactPerson.address.countryZipTown.town = "Kontaktort";
		
		process(writer().correctContact(person));
		
		person = load(id);
		Assert.assertNull(person.contactPerson.person);
		Assert.assertNotNull(person.contactPerson.address);
		Assert.assertEquals("Kontaktnachname", person.contactPerson.address.lastName);
		Assert.assertEquals("Kontaktstrasse", person.contactPerson.address.street);
		Assert.assertEquals("Kontaktort", person.contactPerson.address.countryZipTown.town);
	}

	@Test
	public void correctContact_Person() throws Exception {
		Person person = load(id);
		Person person2 = load(foreignId);
		person.contactPerson.person = person2.personIdentification;
		person.contactPerson.address = person2.dwellingAddress.mailAddress;
		person.contactPerson.address.lastName = person2.personIdentification.officialName;
		
		process(writer().correctContact(person));
		
		person = load(id);
		Assert.assertNotNull(person.contactPerson);
		Assert.assertTrue(person2.personIdentification.isEqual(person.contactPerson.person));
	}

	
	@Test
	public void correctReligion() throws Exception {
		Person person = load(id);
		
		process(writer().correctReligion(person, "222"));
		
		person = load(id);
		Assert.assertEquals("222", person.religion);
	}
	
	@Test
	public void correctOrigin() throws Exception {
		Person person = load(id);
		PlaceOfOrigin origin1 = new PlaceOfOrigin();
		origin1.canton = "TG";
		origin1.originName = "Heimat 1";
		origin1.naturalizationDate = "1974-02-28";
		origin1.expatriationDate = "2007-10-11";
		
		PlaceOfOrigin origin2 = new PlaceOfOrigin();
		origin2.canton = "TI";
		origin2.originName = "Heimat 2";
		origin2.naturalizationDate = "2007-10-11";

		person.placeOfOrigin.clear();
		person.placeOfOrigin.add(origin1);
		person.placeOfOrigin.add(origin2);
		
		process(writer().correctOrigin(person));
		
		person = load(id);
		Assert.assertEquals(2, person.placeOfOrigin.size());
		
		Assert.assertEquals(origin1.display(), person.placeOfOrigin.get(0).display());
		Assert.assertEquals(origin2.display(), person.placeOfOrigin.get(1).display());
	}
	
	@Test
	public void correctResidencePermit() throws Exception {
		Person person = load(foreignId);
		Foreign foreign = new Foreign();
		foreign.residencePermit = "0402";
		foreign.residencePermitTill = "2020-10-11";

		process(writer().correctResidencePermit(person, foreign));
		
		person = load(foreignId);
		Assert.assertEquals("0402", person.foreign.residencePermit);
		Assert.assertEquals("2020-10-11", person.foreign.residencePermitTill);	
	}
	
	@Test
	public void correctMaritalData2() throws Exception {
		Person person = load(id);
		person.maritalStatus.maritalStatus = "2";
		
		process(writer().correctMaritalData(person));
		
		person = load(id);
		Assert.assertTrue(person.maritalStatus.isVerheiratet());
	}
	
	@Test
	public void correctMaritalData1() throws Exception {
		Person person = load(id);
		person.maritalStatus.maritalStatus = "1";
		
		process(writer().correctMaritalData(person));
		
		person = load(id);
		Assert.assertTrue(person.maritalStatus.isLedig());
	}
	
	@Test
	public void correctPlaceOfBirth_Swiss() throws Exception {
		Person person = load(id);
		person.placeOfBirth.municipalityIdentification.cantonAbbreviation = "SG";
		person.placeOfBirth.municipalityIdentification.historyMunicipalityId = "20";
		person.placeOfBirth.municipalityIdentification.municipalityId = "2";
		person.placeOfBirth.municipalityIdentification.municipalityName = "Testwil";
		
		process(writer().correctPlaceOfBirth(person));
		
		person = load(id);
		Assert.assertEquals(Swiss.createCountryIdentification().toStringReadable(), person.placeOfBirth.countryIdentification.toStringReadable());
		Assert.assertEquals("SG", person.placeOfBirth.municipalityIdentification.cantonAbbreviation);
		Assert.assertEquals("20", person.placeOfBirth.municipalityIdentification.historyMunicipalityId);
		Assert.assertEquals("2", person.placeOfBirth.municipalityIdentification.municipalityId);
		Assert.assertEquals("Testwil", person.placeOfBirth.municipalityIdentification.municipalityName);
	}
	
	@Test
	public void correctPlaceOfBirth_Foreign() throws Exception {
		Person person = load(id);
		person.placeOfBirth.countryIdentification.countryId = "4242";
		person.placeOfBirth.countryIdentification.countryIdISO2 = "CZ";
		person.placeOfBirth.countryIdentification.countryNameShort = "CZ Name";
		person.placeOfBirth.foreignTown = "Testtown";
		
		process(writer().correctPlaceOfBirth(person));
		
		person = load(id);
		Assert.assertEquals("4242", person.placeOfBirth.countryIdentification.countryId);
		Assert.assertEquals("CZ", person.placeOfBirth.countryIdentification.countryIdISO2);
		Assert.assertEquals("CZ Name", person.placeOfBirth.countryIdentification.countryNameShort);
		Assert.assertEquals("Testtown", person.placeOfBirth.foreignTown);
	}
	

	@Test
	public void correctDateOfDeath() throws Exception {
		Person person = load(id);
		person.dateOfDeath = "2012-12-31";
		
		process(writer().correctDateOfDeath(person));
		
		person = load(id);
		Assert.assertEquals("2012-12-31", person.dateOfDeath);
	}
	
	@Test
	public void correctDateOfDeathEmpty() throws Exception {
		Person person = load(id);
		person.dateOfDeath = null;
		
		process(writer().correctDateOfDeath(person));
		
		person = load(id);
		Assert.assertNull(person.dateOfDeath);
	}

	@Test
	public void correctLanguageOfCorrespondance() throws Exception {
		Person person = load(id);
		person.languageOfCorrespondance = "3";
		
		process(writer().correctLanguageOfCorrespondance(person));
		
		person = load(id);
		Assert.assertEquals("3", person.languageOfCorrespondance);
	}
	
}