package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.server.EchServer;
import ch.openech.server.ServerCallResult;

public class MarriageTest extends AbstractServerTest {

	private static final String vn1 = "7569397277370";
	private static final String vn2 = "7560584727838";
	private String id1, id2;
	
	@Before
	public void createPerson() throws Exception {
		EchServer.getInstance().getPersistence().clear();
		
		ServerCallResult result = processFile("testPerson/mariage/person_" + vn1 + ".xml");
		id1 = result.createdPersonId;
		result = processFile("testPerson/mariage/person_" + vn2 + ".xml");
		id2 = result.createdPersonId;
	}
	
	@Test
	public void marriage() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Eheschliessung - Mariage/data_53740900000000023.xml");
		processFile("samples/eCH-0020/InfostarSamples/Eheschliessung - Mariage/data_53740900000000033.xml");
		processFile("samples/eCH-0020/InfostarSamples/Eheschliessung - Mariage/data_53740900000000043.xml");
		// 43 mach ein rename. Da die neuen Daten dann schon gespeichert sind findet 53 die entsprechenden
		// Personen nicht mehr. Aber kanns das wirklich sein?
		// process("samples/eCH-0020/InfostarSamples/Eheschliessung - Mariage/data_53740900000000053.xml");
		// process("samples/eCH-0020/InfostarSamples/Eheschliessung - Mariage/data_53740900000000063.xml");
		
		Person person1 = load(id1);
		Person person2 = load(id2);
		
		Assert.assertNotNull(person1);
		Assert.assertTrue(person1.maritalStatus.isVerheiratet());
		Relation relation1 = person1.getPartner();
		Assert.assertEquals("1", relation1.typeOfRelationship);
		Assert.assertTrue(person2.personIdentification.isEqual(relation1.partner));
		
		Assert.assertNotNull(person2);
		Assert.assertTrue(person2.maritalStatus.isVerheiratet());
		Assert.assertEquals("Ogi Villiger", person2.personIdentification.officialName);
		Relation relation2 = person2.getPartner();
		Assert.assertEquals("1", relation2.typeOfRelationship);
		Assert.assertTrue(person1.personIdentification.isEqual(relation2.partner));
	}
	
	@Test
	public void divorce() throws Exception {
		Person person = load(id1);
		process(writer().divorce(person.personIdentification, "2009-08-07"));
		
		person = load(id1);
		Assert.assertTrue(person.maritalStatus.isGeschieden());
		Assert.assertEquals("2009-08-07", person.maritalStatus.dateOfMaritalStatus);
	}
	
	@Test
	public void undoMarriage() throws Exception {
		Person person = load(id2);
		process(writer().undoMarriage(person.personIdentification, "2009-08-06"));
		
		person = load(id2);
		Assert.assertTrue(person.maritalStatus.isUngueltigeEhe());
		Assert.assertEquals("2009-08-06", person.maritalStatus.dateOfMaritalStatus);
	}
	
}
