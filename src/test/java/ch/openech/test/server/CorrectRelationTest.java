package ch.openech.test.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.minimalj.util.IdUtils;

import  ch.openech.model.person.Person;

public class CorrectRelationTest extends AbstractServerTest {

	private Person child, mother;
	
	@Before
	public void createPerson() throws Exception {
		child = processFile("testPerson/correctRelation/personChild.xml");
		mother = processFile("testPerson/correctRelation/personMother.xml");
	}
	
	@Test
	public void marriage() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Kindsverhältnis - Lien de filiation/data_53759900000000023.xml");
		processFile("samples/eCH-0020/InfostarSamples/Kindsverhältnis - Lien de filiation/data_53759900000000033.xml");
		
		child = reload(child);
		mother = reload(mother);

		Assert.assertNotNull(child.getMother().partner.personIdentification);
		Assert.assertTrue(IdUtils.equals(mother, child.getMother().partner.personIdentification));
		Assert.assertEquals("Bernacchi", child.officialName);
	}
	
}
