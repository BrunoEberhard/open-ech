package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;

public class DeathTest extends AbstractServerTest {

	private String id1, id2, id3;
	
	@Before
	public void createPerson() throws Exception {
		id1 = insertPerson("7561829871378");
		id2 = insertPerson("7566223399589");
		id3 = insertPerson("7566223399590");
	}
	
	@Test
	public void death_1() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Tod - Décès/data_53765200000000023.xml");
		
		Person person1 = load(id1);
		
		Assert.assertNotNull(person1);
		Assert.assertEquals("2008-11-15", person1.dateOfDeath);
	}
	
	@Test
	public void death_2() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Tod - Décès/data_53765200000000033.xml");
		
		Person person2 = load(id2);
		
		Assert.assertNotNull(person2);
		Assert.assertEquals("2008-11-15", person2.maritalStatus.dateOfMaritalStatus);
		Assert.assertTrue(person2.maritalStatus.isVerwitwet());
	}

	@Test
	public void death_3() throws Exception {
		Person person = load(id3);
		person.dateOfDeath = "2010-09-08";
		
		process(writer().correctDateOfDeath(person));
		person = load(id3);
		
		Assert.assertNotNull(person);
		Assert.assertEquals("2010-09-08", person.dateOfDeath);
	}
	
}
