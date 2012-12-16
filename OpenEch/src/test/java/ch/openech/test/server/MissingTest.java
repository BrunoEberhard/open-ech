package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.LocalDate;
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
		process(writer().missing(person.personIdentification, new LocalDate(2011, 1, 2)));
		person = load(id);

		Assert.assertEquals(new LocalDate(2011, 1, 2), person.dateOfDeath);
	}
	
	@Test
	public void undoMissing()  throws Exception {
		Person person = load(id);
		process(writer().undoMissing(person.personIdentification));
		person = load(id);

		Assert.assertNull(person.dateOfDeath);
	}

}
