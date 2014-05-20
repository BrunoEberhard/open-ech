package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import  ch.openech.model.person.Person;

public class DeathTest extends AbstractServerTest {

	private Person p1, p2, p3;
	
	@Before
	public void createPerson() throws Exception {
		p1 = insertPerson("7561829871378");
		p2 = insertPerson("7566223399589");
		p3 = insertPerson("7566223399590");
	}
	
	@Test
	public void death_1() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Tod - Décès/data_53765200000000023.xml");
		
		Person person1 = reload(p1);
		
		Assert.assertNotNull(person1);
		Assert.assertEquals(new LocalDate(2008, 11, 15), person1.dateOfDeath);
	}
	
	@Test
	public void death_2() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Tod - Décès/data_53765200000000033.xml");
		
		Person person2 = reload(p2);
		
		Assert.assertNotNull(person2);
		Assert.assertEquals(new LocalDate(2008, 11, 15), person2.maritalStatus.dateOfMaritalStatus);
		Assert.assertTrue(person2.maritalStatus.isVerwitwet());
	}

	@Test
	public void death_3() throws Exception {
		Person person = reload(p3);
		person.dateOfDeath = new LocalDate(2010, 9, 8);
		
		process(writer().correctDateOfDeath(person));
		person = reload(person);
		
		Assert.assertNotNull(person);
		Assert.assertEquals(new LocalDate(2010, 9, 8), person.dateOfDeath);
	}
	
}
