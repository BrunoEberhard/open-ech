package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.server.ServerCallResult;

public class MissingTest extends AbstractServerTest {

	private static String id;
	
	@BeforeClass
	public static void createPerson() throws Exception {
		ServerCallResult result = processFile("testPerson/missing/person.xml");
		id = result.createdPersonId;
	}
	
	@Test
	public void missing() throws Exception {
		Person person = load(id);
		process(writer().missing(person.personIdentification, "2011-01-02"));
		person = load(id);

		Assert.assertEquals("2011-01-02", person.dateOfDeath);
	}
	
	@Test
	public void undoMissing()  throws Exception {
		Person person = load(id);
		process(writer().undoMissing(person.personIdentification));
		person = load(id);

		Assert.assertNull(person.dateOfDeath);
	}

}
