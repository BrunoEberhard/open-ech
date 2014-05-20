package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import  ch.openech.model.code.NationalityStatus;
import  ch.openech.model.code.ResidencePermit;
import  ch.openech.model.common.Swiss;
import  ch.openech.model.person.Person;
import  ch.openech.model.person.PlaceOfOrigin;

public class NaturalizeSwissTest extends AbstractServerTest {

	private Person p;
	
	@Before
	public void createPerson() throws Exception {
		clear();
		
		p = processFile("testPerson/naturalizeSwiss/person.xml");
	}
	
	@Test
	public void naturalizeSwiss() throws Exception {
		Person person = reload(p);
		int placeOfOriginCountBefore = person.placeOfOrigin.size();
		
		processFile("samples/eCH-0020/InfostarSamples/Buergerrecht - Nationalité/eventNaturalizeSwiss/data_53741400000000113.xml");
		
		person = reload(p);
		
		Assert.assertNotNull(person);
		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals(Swiss.SWISS_COUNTRY_ID, person.nationality.nationalityCountry.countryId);	
		Assert.assertEquals(Swiss.SWISS_COUNTRY_NAME_SHORT, person.nationality.nationalityCountry.countryNameShort);	

		Assert.assertEquals(placeOfOriginCountBefore + 1, person.placeOfOrigin.size());
		PlaceOfOrigin placeOfOrigin = person.placeOfOrigin.get(person.placeOfOrigin.size() - 1);
		Assert.assertEquals("Murten", placeOfOrigin.originName);
		Assert.assertEquals("FR", placeOfOrigin.cantonAbbreviation.canton);
	}

	@Test
	public void undoSwiss() throws Exception {
		Person person = reload(p);
		person.nationality.nationalityCountry.countryId = 8345;
		person.nationality.nationalityCountry.countryIdISO2 = "SN";
		person.nationality.nationalityCountry.countryNameShort = "Senegal";
		person.foreign.residencePermit = ResidencePermit.Saisonarbeiter;
		
		process(writer().undoSwiss(person));

		person = reload(p);
		Assert.assertNotNull(person);
		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals(Integer.valueOf(8345), person.nationality.nationalityCountry.countryId);	
		Assert.assertEquals("Senegal", person.nationality.nationalityCountry.countryNameShort);	
		Assert.assertEquals("SN", person.nationality.nationalityCountry.countryIdISO2);	
		
		// Assert.assertEquals("Passname", person.nameOnPassport);	
		Assert.assertEquals(ResidencePermit.Saisonarbeiter, person.foreign.residencePermit);	
	}
	
}
