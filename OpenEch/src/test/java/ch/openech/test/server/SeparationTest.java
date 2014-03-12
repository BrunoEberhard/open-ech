package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.types.Separation;

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
		process(writer().separation(person.personIdentification, Separation.freiwillig, ISODateTimeFormat.date().parseLocalDate("2010-09-03")));
		
		person = reload(p);
		Assert.assertNotNull(person);
		Assert.assertEquals(ISODateTimeFormat.date().parseLocalDate("2010-09-03"), person.separation.dateOfSeparation);
		Assert.assertEquals(Separation.freiwillig, person.separation.separation);
	}
	
	@Test
	public void undoSeparation() throws Exception {
		Person person = reload(p);
		process(writer().undoSeparation(person.personIdentification));

		person = reload(p);
		Assert.assertNull(person.separation.dateOfSeparation);
	}
	
}
