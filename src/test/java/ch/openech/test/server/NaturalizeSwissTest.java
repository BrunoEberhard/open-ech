package ch.openech.test.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.minimalj.util.Codes;

import ch.openech.model.code.NationalityStatus;
import ch.openech.model.code.ResidencePermit;
import ch.openech.model.common.Canton;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.person.Person;
import ch.openech.model.person.PlaceOfOrigin;

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
		Assert.assertTrue(person.nationality.isSwiss());	

		Assert.assertEquals(placeOfOriginCountBefore + 1, person.placeOfOrigin.size());
		PlaceOfOrigin placeOfOrigin = person.placeOfOrigin.get(person.placeOfOrigin.size() - 1);
		Assert.assertEquals("Murten", placeOfOrigin.originName);
		Assert.assertEquals(Codes.findCode(Canton.class, "FR"), placeOfOrigin.canton);
	}

	@Test
	public void undoSwiss() throws Exception {
		Person person = reload(p);
		person.nationality.nationalityCountry = new CountryIdentification();
		person.nationality.nationalityCountry.id = 8345;
		person.nationality.nationalityCountry.countryIdISO2 = "SN";
		person.nationality.nationalityCountry.countryNameShort = "Senegal";
		person.foreign.residencePermit = ResidencePermit.Saisonarbeiter;
		
		process(writer().undoSwiss(person));

		person = reload(p);
		Assert.assertNotNull(person);
		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals(Integer.valueOf(8345), person.nationality.nationalityCountry.id);	
		Assert.assertEquals("Senegal", person.nationality.nationalityCountry.countryNameShort);	
		Assert.assertEquals("SN", person.nationality.nationalityCountry.countryIdISO2);	
		
		// Assert.assertEquals("Passname", person.nameOnPassport);	
		Assert.assertEquals(ResidencePermit.Saisonarbeiter, person.foreign.residencePermit);	
	}
	
}
