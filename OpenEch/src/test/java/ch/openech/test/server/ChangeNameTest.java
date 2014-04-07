package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;

public class ChangeNameTest extends AbstractServerTest {

	private static final String vn = "7561234567890";
	private Person person;
	
	@Before
	public void createPerson() throws Exception {
		person = insertPerson(vn);
	}
	
	@Test
	public void changeName() throws Exception {
		Assert.assertNotNull(person);

		processFile("samples/eCH-0020/changeName/data_ordipro-changeName-40.xml");
		
		person = reload(person);
		Assert.assertNotNull(person);
		
		Assert.assertEquals("MUSTER", person.officialName);
		Assert.assertEquals("Hanspeter", person.firstName);

//	   Dieses AdHoc umstellen klappt noch nicht ganz
//		ApplicationController.preferences().put("xmlVersion", "1.1");
//		
//		process("testPerson/changeName/changeNameWithParents.xml");
//		person = personLoader.load(id, null);
//		
//		Assert.assertEquals("VorVater", person.getFatherFirstName());
//		Assert.assertEquals("AmtVater", person.getFatherOfficialName());
//		Assert.assertEquals("VorMutter", person.getMotherFirstName());
//		Assert.assertEquals("AmtMutter", person.getMotherOfficialName());
//		
//		Assert.assertEquals("Rufname", person.callName);
//
//		ApplicationController.preferences().put("xmlVersion", "2.2");
	}
	
}
