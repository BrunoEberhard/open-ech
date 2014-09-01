package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.model.code.NationalityStatus;
import ch.openech.model.common.Swiss;
import ch.openech.model.person.Person;
import ch.openech.model.person.PlaceOfOrigin;

public class NaturalizeForeignerTest extends AbstractServerTest {

	@Test
	public void naturalizeForeigner() throws Exception {
		Person p = processFile("testPerson/naturalizeForeigner/person.xml");

		processFile("samples/eCH-0020/InfostarSamples/Buergerrecht - Nationalité/eventNaturalizeForeigner/data_53745100000000023.xml");
		
		Person person = reload(p);
		
		Assert.assertNotNull(person);
		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals(Swiss.SWISS_COUNTRY_ID, person.nationality.isSwiss());	

		Assert.assertEquals(1, person.placeOfOrigin.size());
		PlaceOfOrigin placeOfOrigin = person.placeOfOrigin.get(0);
		
		Assert.assertEquals(1, person.placeOfOrigin.size());
		Assert.assertEquals("Frauenfeld", placeOfOrigin.originName);
		Assert.assertEquals("TG", placeOfOrigin.cantonAbbreviation.canton);
	}
	
}
