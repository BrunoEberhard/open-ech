package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;

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

		Assert.assertTrue(mother.personIdentification.isEqual(child.getMother().partner));
		Assert.assertEquals("Bernacchi", child.personIdentification.officialName);
	}
	
}
