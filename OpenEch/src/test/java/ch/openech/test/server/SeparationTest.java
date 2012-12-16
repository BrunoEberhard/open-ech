package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.types.Separation;
import ch.openech.server.EchServer;
import ch.openech.server.ServerCallResult;

public class SeparationTest extends AbstractServerTest {

	private String id;
	
	@Before
	public void createPerson() throws Exception {
		EchServer.getInstance().getPersistence().clear();
		
		ServerCallResult result = processFile("testPerson/separation/person.xml");
		id = result.createdPersonId;
	}
	
	@Test
	public void separation() throws Exception {
		Person person = load(id);
		process(writer().separation(person.personIdentification, Separation.freiwillig, ISODateTimeFormat.date().parseLocalDate("2010-09-03")));
		
		person = load(id);
		Assert.assertNotNull(person);
		Assert.assertEquals(ISODateTimeFormat.date().parseLocalDate("2010-09-03"), person.separation.dateOfSeparation);
		Assert.assertEquals(Separation.freiwillig, person.separation.separation);
	}
	
	@Test
	public void undoSeparation() throws Exception {
		Person person = load(id);
		process(writer().undoSeparation(person.personIdentification));

		person = load(id);
		Assert.assertNull(person.separation.dateOfSeparation);
	}
	
}
