package ch.openech.test.server;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.minimalj.util.IdUtils;

import  ch.openech.model.code.BasedOnLaw;
import  ch.openech.model.person.Person;
import  ch.openech.model.person.Relation;
import  ch.openech.model.person.types.TypeOfRelationship;

public class GardianTest extends AbstractServerTest {

	private static Person p, gardianP;
	
	@BeforeClass
	public static void createPerson() throws Exception {
		p = processFile("testPerson/gardian/person.xml");
		
		gardianP = processFile("testPerson/gardian/personGardian.xml");
	}
	
	@Test
	public void gardianMeasure() throws Exception {
		Person person = reload(p);
		Person personGardian = reload(gardianP);

		Relation relation = new Relation();
		relation.typeOfRelationship = TypeOfRelationship.Vormund;
		relation.basedOnLaw = BasedOnLaw._368;
		relation.partner.setValue(personGardian);
		person.relation.add(relation);
		process(writer().gardianMeasure(person.personIdentification(), relation));
		
		person = reload(p);
		Assert.assertNotNull(person);
		Relation gardian = person.getRelation(TypeOfRelationship.Vormund);
		Assert.assertNotNull(gardian.partner.personIdentification);
		Assert.assertTrue(IdUtils.equals(personGardian, gardian.partner.personIdentification));
		Assert.assertEquals(BasedOnLaw._368, gardian.basedOnLaw);
	}

	@Test
	public void changeGardian() throws Exception {
		Person person = reload(p);
		Person personGardian = reload(gardianP);

		Relation relation = new Relation();
		relation.typeOfRelationship = TypeOfRelationship.Beistand;
		relation.basedOnLaw = BasedOnLaw._369;
		relation.partner.setValue(personGardian);
		person.relation.add(relation);
		process(writer().changeGardian(person.personIdentification(), relation));

		person = reload(p);
		Assert.assertNotNull(person);
		Relation gardian = person.getRelation(TypeOfRelationship.Beistand);
		Assert.assertNotNull(gardian.partner.personIdentification);
		Assert.assertTrue(IdUtils.equals(reload(gardianP), gardian.partner.personIdentification));
		Assert.assertEquals(BasedOnLaw._369, gardian.basedOnLaw);

		Assert.assertNull(person.getRelation(TypeOfRelationship.Vormund));
	}
	
	@Test
	public void undoGardian() throws Exception {
		Person person = reload(p);
		process(writer().undoGardian(person.personIdentification()));
		
		person = reload(p);
		Assert.assertNotNull(person);
		Assert.assertNull(person.getRelation(TypeOfRelationship.Beistand));
		Assert.assertNull(person.getRelation(TypeOfRelationship.Vormund));
	}
	
}
