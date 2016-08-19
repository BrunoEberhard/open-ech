package ch.openech.test.server;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.util.Codes;

import ch.openech.model.code.NationalityStatus;
import ch.openech.model.common.Canton;
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
		Assert.assertTrue(person.nationality.isSwiss());	

		Assert.assertEquals(1, person.placeOfOrigin.size());
		PlaceOfOrigin placeOfOrigin = person.placeOfOrigin.get(0);
		
		Assert.assertEquals(1, person.placeOfOrigin.size());
		Assert.assertEquals("Frauenfeld", placeOfOrigin.originName);
		Assert.assertEquals(Codes.findCode(Canton.class, "TG"), placeOfOrigin.canton);
	}
	
}
