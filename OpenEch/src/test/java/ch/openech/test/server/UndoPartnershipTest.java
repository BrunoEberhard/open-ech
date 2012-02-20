package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;

public class UndoPartnershipTest extends AbstractServerTest {

	private static final String vn1 = "7561169906723";
	private static final String vn2 = "7569990431117";
	private String id1, id2;
	
	@Before
	public void createPerson() throws Exception {
		id1 = insertPerson(vn1);
		id2 = insertPerson(vn2);
	}
	
	@Test
	public void partnership() throws Exception {
		processFile("testPerson/undoPartnership/partnershipTest1.xml");
		processFile("testPerson/undoPartnership/partnershipTest2.xml");
		
		Person person1 = load(id1);
		Person person2 = load(id2);
		
		Assert.assertNotNull(person1);
		Assert.assertEquals("6", person1.maritalStatus.maritalStatus);
		Relation relation1 = person1.getPartner();
		Assert.assertEquals("2", relation1.typeOfRelationship);
		Assert.assertTrue(person2.personIdentification.isEqual(relation1.partner));
		
		Assert.assertNotNull(person2);
		Assert.assertEquals("6", person2.maritalStatus.maritalStatus);
		Relation relation2 = person2.getPartner();
		Assert.assertEquals("2", relation2.typeOfRelationship);
		Assert.assertTrue(person1.personIdentification.isEqual(relation2.partner));
	}
	
	@Test
	public void undoPartnership() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Auflösung Partnerschaft - Dissolution du partenariat/data_53756300000000023.xml");
		processFile("samples/eCH-0020/InfostarSamples/Auflösung Partnerschaft - Dissolution du partenariat/data_53756300000000033.xml");
		
		Person person1 = load(id1);
		Person person2 = load(id2);
		
		Assert.assertNotNull(person1);
		Assert.assertEquals("7", person1.maritalStatus.maritalStatus);
		Relation relation1 = person1.getPartner();
		Assert.assertNull(relation1);
		
		Assert.assertNotNull(person2);
		Assert.assertEquals("7", person2.maritalStatus.maritalStatus);
		Relation relation2 = person2.getPartner();
		Assert.assertNull(relation2);
	}
	
	
}
