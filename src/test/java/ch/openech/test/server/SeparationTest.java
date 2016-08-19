package ch.openech.test.server;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.openech.model.person.Person;
import ch.openech.model.person.types.Separation;

public class SeparationTest extends AbstractServerTest {

	private Person p;
	
	@Before
	public void createPerson() throws Exception {
		clear();
		
		p = processFile("testPerson/separation/person.xml");
	}
	
	@Test
	public void separation() throws Exception {
		Person person = reload(p);
		process(writer().separation(person.personIdentification(), Separation.freiwillig, LocalDate.of(2010, 9, 3)));
		
		person = reload(p);
		Assert.assertNotNull(person);
		Assert.assertEquals(LocalDate.of(2010, 9, 3), person.separation.dateOfSeparation);
		Assert.assertEquals(Separation.freiwillig, person.separation.separation);
	}
	
	@Test
	public void undoSeparation() throws Exception {
		Person person = reload(p);
		process(writer().undoSeparation(person.personIdentification()));

		person = reload(p);
		Assert.assertNull(person.separation.dateOfSeparation);
	}
	
}
