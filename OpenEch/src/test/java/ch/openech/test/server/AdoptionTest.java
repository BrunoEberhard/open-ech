package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;

public class AdoptionTest extends AbstractServerTest {

	private static final String VN_CHILD = "7560343533168";
	private static final String VN_FATHER = "7561566500548";
	private static final String VN_MOTHER = "7563232840743";

	private String mother_id, father_id, child_id;
	
	@Before
	public void createPerson() throws Exception {
		mother_id = insertPerson(VN_MOTHER);
		father_id = insertPerson(VN_FATHER);
		child_id = insertPerson(VN_CHILD);
	}
	
	@Test
	public void adoption() throws Exception {
		Person mother = load(mother_id);
		Person father = load(father_id);
		Person child = load(child_id);
		
		processFile("samples/eCH-0020/InfostarSamples/Adoption/data_53747100000000023.xml");
		processFile("samples/eCH-0020/InfostarSamples/Adoption/data_53747100000000033.xml");
		
		child = load(child_id);
		
		Assert.assertNotNull(child);
		Assert.assertEquals(father.personIdentification.officialName, child.personIdentification.officialName);

		Assert.assertEquals("CH", child.placeOfBirth.countryIdentification.countryIdISO2);
		Assert.assertEquals("2196", child.placeOfBirth.municipalityIdentification.municipalityId);
		Assert.assertEquals("Fribourg", child.placeOfBirth.municipalityIdentification.municipalityName);
		Assert.assertEquals("FR", child.placeOfBirth.municipalityIdentification.cantonAbbreviation);
		
		Relation motherRelation = child.getRelation("3");
		Assert.assertTrue(mother.personIdentification.isEqual(motherRelation.partner));
		
		Relation fatherRelation = child.getRelation("4");
		Assert.assertTrue(father.personIdentification.isEqual(fatherRelation.partner));
	}
	
	
}
