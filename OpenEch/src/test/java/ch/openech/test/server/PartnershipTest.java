package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.server.ServerCallResult;

public class PartnershipTest extends AbstractServerTest {

	@Test
	public void partnership() throws Exception {
		ServerCallResult result = processFile("testPerson/partnership/person1.xml");
		String id1 = result.createdPersonId;

		result = processFile("testPerson/partnership/person2.xml");
		String id2 = result.createdPersonId;

		processFile("samples/eCH-0020/InfostarSamples/Eintragung Partnerschaft - Enregistrement du partenariat/data_53740300000000023.xml");
		processFile("samples/eCH-0020/InfostarSamples/Eintragung Partnerschaft - Enregistrement du partenariat/data_53740300000000033.xml");
		
		Person person1 = load(id1);
		Person person2 = load(id2);
		
		Assert.assertNotNull(person1);
		Assert.assertTrue(person1.maritalStatus.isPartnerschaft());
		Relation relation1 = person1.getPartner();
		Assert.assertEquals("2", relation1.typeOfRelationship);
		Assert.assertTrue(person2.personIdentification.isEqual(relation1.partner));
		
		Assert.assertNotNull(person2);
		Assert.assertTrue(person2.maritalStatus.isPartnerschaft());
		Relation relation2 = person2.getPartner();
		Assert.assertEquals("2", relation2.typeOfRelationship);
		Assert.assertTrue(person1.personIdentification.isEqual(relation2.partner));
	}
	
	
}
