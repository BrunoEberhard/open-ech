package ch.openech.test.server;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import  ch.openech.model.person.Person;
import junit.framework.Assert;

public class MissingTest extends AbstractServerTest {

	private static Person p;
	
	@Before
	public void createPerson() throws Exception {
		p = processFile("testPerson/missing/person.xml");
	}
	
	@Test
	public void missing() throws Exception {
		Person person = reload(p);
		process(writer().missing(person.personIdentification(), LocalDate.of(2011, 1, 2)));
		person = reload(p);

		Assert.assertEquals(LocalDate.of(2011, 1, 2), person.dateOfDeath);
	}
	
	@Test
	public void undoMissing()  throws Exception {
		Person person = reload(p);
		process(writer().undoMissing(person.personIdentification()));
		person = reload(p);

		Assert.assertNull(person.dateOfDeath);
	}

}
