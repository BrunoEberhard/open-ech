package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.openech.dm.person.Person;

public class MissingTest extends AbstractServerTest {

	private static Person p;
	
	@BeforeClass
	public static void createPerson() throws Exception {
		p = processFile("testPerson/missing/person.xml");
	}
	
	@Test
	public void missing() throws Exception {
		Person person = reload(p);
		process(writer().missing(person.personIdentification(), new LocalDate(2011, 1, 2)));
		person = reload(p);

		Assert.assertEquals(new LocalDate(2011, 1, 2), person.dateOfDeath);
	}
	
	@Test
	public void undoMissing()  throws Exception {
		Person person = reload(p);
		process(writer().undoMissing(person.personIdentification()));
		person = reload(p);

		Assert.assertNull(person.dateOfDeath);
	}

}
