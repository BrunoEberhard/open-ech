package ch.openech.test.server;

import org.junit.Test;
import org.minimalj.util.IdUtils;

import  ch.openech.model.person.Person;
import  ch.openech.model.person.Relation;
import  ch.openech.model.person.types.TypeOfRelationship;
import junit.framework.Assert;

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
		Assert.assertNotNull(relation1.partner.person);
		Assert.assertTrue(IdUtils.equals(person2, relation1.partner.person));
		
		Assert.assertNotNull(person2);
		Assert.assertTrue(person2.maritalStatus.isPartnerschaft());
		Relation relation2 = person2.getPartner();
		Assert.assertEquals(TypeOfRelationship.Partner, relation2.typeOfRelationship);
		Assert.assertNotNull(relation2.partner.person);
		Assert.assertTrue(IdUtils.equals(person1, relation2.partner.person));
	}
	
	
}
