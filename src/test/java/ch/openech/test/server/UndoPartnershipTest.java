package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import  ch.openech.model.person.Person;
import  ch.openech.model.person.Relation;
import  ch.openech.model.person.types.MaritalStatus;
import  ch.openech.model.person.types.TypeOfRelationship;

public class UndoPartnershipTest extends AbstractServerTest {

	private static final String vn1 = "7561169906723";
	private static final String vn2 = "7569990431117";
	private Person p1, p2;
	
	@Before
	public void createPerson() throws Exception {
		p1 = insertPerson(vn1);
		p2 = insertPerson(vn2);
	}
	
	@Test
	public void partnership() throws Exception {
		processFile("testPerson/undoPartnership/partnershipTest1.xml");
		processFile("testPerson/undoPartnership/partnershipTest2.xml");
		
		Person person1 = reload(p1);
		Person person2 = reload(p2);
		
		Assert.assertNotNull(person1);
		Assert.assertEquals(MaritalStatus.partnerschaft, person1.maritalStatus.maritalStatus);
		Relation relation1 = person1.getPartner();
		Assert.assertEquals(TypeOfRelationship.Partner, relation1.typeOfRelationship);
		Assert.assertEquals(person2.id, relation1.partner.id);
		
		Assert.assertNotNull(person2);
		Assert.assertEquals(MaritalStatus.partnerschaft, person2.maritalStatus.maritalStatus);
		Relation relation2 = person2.getPartner();
		Assert.assertEquals(TypeOfRelationship.Partner, relation2.typeOfRelationship);
		Assert.assertEquals(person1.id, relation2.partner.id);
	}
	
	@Test
	public void undoPartnership() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Auflösung Partnerschaft - Dissolution du partenariat/data_53756300000000023.xml");
		processFile("samples/eCH-0020/InfostarSamples/Auflösung Partnerschaft - Dissolution du partenariat/data_53756300000000033.xml");
		
		Person person1 = reload(p1);
		Person person2 = reload(p2);
		
		Assert.assertNotNull(person1);
		Assert.assertEquals(MaritalStatus.aufgeloeste_partnerschaft, person1.maritalStatus.maritalStatus);
		Relation relation1 = person1.getPartner();
		Assert.assertNull(relation1);
		
		Assert.assertNotNull(person2);
		Assert.assertEquals(MaritalStatus.aufgeloeste_partnerschaft, person2.maritalStatus.maritalStatus);
		Relation relation2 = person2.getPartner();
		Assert.assertNull(relation2);
	}
	
	
}