package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.TypeOfRelationship;

public class MarriageTest extends AbstractServerTest {

	private static final String vn1 = "7569397277370";
	private static final String vn2 = "7560584727838";
	private Person p1, p2;
	
	@Before
	public void createPerson() throws Exception {
		clear();
		
		p1 = processFile("testPerson/mariage/person_" + vn1 + ".xml");
		p2 = processFile("testPerson/mariage/person_" + vn2 + ".xml");
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
		
		Person person1 = reload(p1);
		Person person2 = reload(p2);
		
		Assert.assertNotNull(person1);
		Assert.assertTrue(person1.maritalStatus.isVerheiratet());
		Relation relation1 = person1.getPartner();
		Assert.assertEquals(TypeOfRelationship.Ehepartner, relation1.typeOfRelationship);
		Assert.assertEquals(person2.id, relation1.partner.id);
		
		Assert.assertNotNull(person2);
		Assert.assertTrue(person2.maritalStatus.isVerheiratet());
		Assert.assertEquals("Ogi Villiger", person2.officialName);
		Relation relation2 = person2.getPartner();
		Assert.assertEquals(TypeOfRelationship.Ehepartner, relation2.typeOfRelationship);
		Assert.assertEquals(person1.id, relation2.partner.id);
	}
	
	@Test
	public void divorce() throws Exception {
		Person person = reload(p1);
		process(writer().divorce(person.personIdentification(), new LocalDate(2009, 8, 7)));
		
		person = reload(p1);
		Assert.assertTrue(person.maritalStatus.isGeschieden());
		Assert.assertEquals(new LocalDate(2009, 8, 7), person.maritalStatus.dateOfMaritalStatus);
	}
	
	@Test
	public void undoMarriage() throws Exception {
		Person person = reload(p2);
		process(writer().undoMarriage(person.personIdentification(), new LocalDate(2009, 8, 6)));
		
		person = reload(p2);
		Assert.assertTrue(person.maritalStatus.isUngueltigeEhe());
		Assert.assertEquals(new LocalDate(2009, 8, 6), person.maritalStatus.dateOfMaritalStatus);
	}
	
}
