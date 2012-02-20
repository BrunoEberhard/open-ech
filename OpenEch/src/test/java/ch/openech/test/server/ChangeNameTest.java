package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;

public class ChangeNameTest extends AbstractServerTest {

	private static final String vn = "7561829871380";
	private String id;
	
	@Before
	public void createPerson() throws Exception {
		id = insertPerson(vn);
	}
	
	@Test
	public void changeName() throws Exception {
		processFile("samples/eCH-0020/changeName/data_ordipro-changeName-40.xml");
		
		Person person = load(id);
		
		Assert.assertNotNull(person);
		Assert.assertEquals("MUSTER", person.personIdentification.officialName);
		Assert.assertEquals("Hanspeter", person.personIdentification.firstName);

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
