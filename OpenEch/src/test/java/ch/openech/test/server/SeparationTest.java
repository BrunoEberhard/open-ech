package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.server.ServerCallResult;

public class SeparationTest extends AbstractServerTest {

	private String id;
	
	@Before
	public void createPerson() throws Exception {
		ServerCallResult result = processFile("testPerson/separation/person.xml");
		id = result.createdPersonId;
	}
	
	@Test
	public void separation() throws Exception {
		String FREIWILLIG_GETRENNT = "1";
		
		Person person = load(id);
		process(writer().separation(person.personIdentification, FREIWILLIG_GETRENNT, "2010-09-03"));
		
		person = load(id);
		Assert.assertNotNull(person);
		Assert.assertEquals("2010-09-03", person.separation.dateOfSeparation);
		Assert.assertEquals(FREIWILLIG_GETRENNT, person.separation.separation);
	}
	
	@Test
	public void undoSeparation() throws Exception {
		Person person = load(id);
		process(writer().undoSeparation(person.personIdentification));

		person = load(id);
		Assert.assertNull(person.separation.dateOfSeparation);
	}
	
}
