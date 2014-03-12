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

	private Person mother, father, child;
	
	@Before
	public void createPerson() throws Exception {
		clear();
		
		mother = insertPerson(VN_MOTHER);
		father = insertPerson(VN_FATHER);
		child = insertPerson(VN_CHILD);
	}
	
	@Test
	public void adoption() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Adoption/data_53747100000000023.xml");
		processFile("samples/eCH-0020/InfostarSamples/Adoption/data_53747100000000033.xml");
		
		child = reload(child);
		
		Assert.assertNotNull(child);
		Assert.assertEquals(father.personIdentification.officialName, child.personIdentification.officialName);

		Assert.assertEquals("CH", child.placeOfBirth.countryIdentification.countryIdISO2);
		Assert.assertEquals(new Integer(2196), child.placeOfBirth.municipalityIdentification.municipalityId);
		Assert.assertEquals("Fribourg", child.placeOfBirth.municipalityIdentification.municipalityName);
		Assert.assertEquals("FR", child.placeOfBirth.municipalityIdentification.cantonAbbreviation.canton);
		
		Relation motherRelation = child.getMother();
		Assert.assertTrue(mother.personIdentification.isEqual(motherRelation.partner));
		
		Relation fatherRelation = child.getFather();
		Assert.assertTrue(father.personIdentification.isEqual(fatherRelation.partner));
	}
	
	
}
