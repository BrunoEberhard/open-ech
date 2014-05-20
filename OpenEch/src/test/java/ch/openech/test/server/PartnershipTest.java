package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Test;

import  ch.openech.model.person.Person;
import  ch.openech.model.person.Relation;
import  ch.openech.model.person.types.TypeOfRelationship;

public class PartnershipTest extends AbstractServerTest {

	@Test
	public void partnership() throws Exception {
		Person p1 = processFile("testPerson/partnership/person1.xml");
		Person p2 = processFile("testPerson/partnership/person2.xml");

		processFile("samples/eCH-0020/InfostarSamples/Eintragung Partnerschaft - Enregistrement du partenariat/data_53740300000000023.xml");
		processFile("samples/eCH-0020/InfostarSamples/Eintragung Partnerschaft - Enregistrement du partenariat/data_53740300000000033.xml");
		
		Person person1 = reload(p1);
		Person person2 = reload(p2);
		
		Assert.assertNotNull(person1);
		Assert.assertTrue(person1.maritalStatus.isPartnerschaft());
		Relation relation1 = person1.getPartner();
		Assert.assertEquals(TypeOfRelationship.Partner, relation1.typeOfRelationship);
		Assert.assertEquals(person2.id, relation1.partner.id);
		
		Assert.assertNotNull(person2);
		Assert.assertTrue(person2.maritalStatus.isPartnerschaft());
		Relation relation2 = person2.getPartner();
		Assert.assertEquals(TypeOfRelationship.Partner, relation2.typeOfRelationship);
		Assert.assertEquals(person1.id, relation2.partner.id);
	}
	
	
}
