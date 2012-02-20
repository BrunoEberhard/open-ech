package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.common.Swiss;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.server.ServerCallResult;

public class NationalityTest extends AbstractServerTest {

	@Test
	public void naturalizeForeigner() throws Exception {
		ServerCallResult result = processFile("testPerson/naturalizeForeigner/person.xml");
		String person_id = result.createdPersonId;

		result = processFile("samples/eCH-0020/InfostarSamples/Buergerrecht - Nationalité/eventNaturalizeForeigner/data_53745100000000023.xml");
		
		Person person = load(person_id);
		
		Assert.assertNotNull(person);
		Assert.assertEquals("2", person.nationality.nationalityStatus);
		Assert.assertEquals(Swiss.SWISS_COUNTRY_ID, person.nationality.nationalityCountry.countryId);	
		Assert.assertEquals(Swiss.SWISS_COUNTRY_NAME_SHORT, person.nationality.nationalityCountry.countryNameShort);	

		Assert.assertEquals(1, person.placeOfOrigin.size());
		PlaceOfOrigin placeOfOrigin = person.placeOfOrigin.get(0);
		
		Assert.assertEquals(1, person.placeOfOrigin.size());
		Assert.assertEquals("Frauenfeld", placeOfOrigin.originName);
		Assert.assertEquals("TG", placeOfOrigin.canton);
	}
	
}
