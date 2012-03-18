package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.server.ServerCallResult;

public class CorrectRelationTest extends AbstractServerTest {

	private String child_id, mother_id;
	
	@Before
	public void createPerson() throws Exception {
		ServerCallResult result = processFile("testPerson/correctRelation/personChild.xml");
		child_id = result.createdPersonId;
		result = processFile("testPerson/correctRelation/personMother.xml");
		mother_id = result.createdPersonId;
	}
	
	@Test
	public void marriage() throws Exception {
		load(child_id);
		
		processFile("samples/eCH-0020/InfostarSamples/Kindsverhältnis - Lien de filiation/data_53759900000000023.xml");
		processFile("samples/eCH-0020/InfostarSamples/Kindsverhältnis - Lien de filiation/data_53759900000000033.xml");
		
		Person child = load(child_id);
		Person mother = load(mother_id);

		Assert.assertTrue(mother.personIdentification.isEqual(child.getMother().partner));
		Assert.assertEquals("Bernacchi", child.personIdentification.officialName);
	}
	
}
