package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.openech.dm.code.BasedOnLaw;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.server.ServerCallResult;

public class GardianTest extends AbstractServerTest {

	private static String id, gardianId;
	
	@BeforeClass
	public static void createPerson() throws Exception {
		ServerCallResult result = processFile("testPerson/gardian/person.xml");
		id = result.createdPersonId;
		
		result = processFile("testPerson/gardian/personGardian.xml");
		gardianId = result.createdPersonId;
	}
	
	@Test
	public void gardianMeasure() throws Exception {
		Person person = load(id);
		Person personGardian = load(gardianId);

		Relation relation = new Relation();
		relation.typeOfRelationship = TypeOfRelationship.Vormund;
		relation.basedOnLaw = BasedOnLaw._368;
		relation.partner = personGardian.personIdentification;
		person.relation.add(relation);
		process(writer().gardianMeasure(person.personIdentification, relation));
		
		person = load(id);
		Assert.assertNotNull(person);
		Relation gardian = person.getRelation(TypeOfRelationship.Vormund);
		Assert.assertTrue(personGardian.personIdentification.isEqual(gardian.partner));
		Assert.assertEquals(BasedOnLaw._368, gardian.basedOnLaw);
	}

	@Test
	public void changeGardian() throws Exception {
		Person person = load(id);
		Person personGardian = load(gardianId);

		Relation relation = new Relation();
		relation.typeOfRelationship = TypeOfRelationship.Beistand;
		relation.basedOnLaw = BasedOnLaw._369;
		relation.partner = personGardian.personIdentification;
		person.relation.add(relation);
		process(writer().changeGardian(person.personIdentification, relation));

		person = load(id);
		Assert.assertNotNull(person);
		Relation gardian = person.getRelation(TypeOfRelationship.Beistand);
		Assert.assertTrue(load(gardianId).personIdentification.isEqual(gardian.partner));
		Assert.assertEquals(BasedOnLaw._369, gardian.basedOnLaw);

		Assert.assertTrue(person.getRelation(TypeOfRelationship.Vormund).isEmpty());
	}
	
	@Test
	public void undoGardian() throws Exception {
		Person person = load(id);
		process(writer().undoGardian(person.personIdentification));
		
		person = load(id);
		Assert.assertNotNull(person);
		Assert.assertTrue(person.getRelation(TypeOfRelationship.Beistand).isEmpty());
		Assert.assertTrue(person.getRelation(TypeOfRelationship.Vormund).isEmpty());
	}
	
}
