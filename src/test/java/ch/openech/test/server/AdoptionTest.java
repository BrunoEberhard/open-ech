package ch.openech.test.server;

import org.junit.Before;
import org.junit.Test;
import org.minimalj.util.Codes;
import org.minimalj.util.IdUtils;

import ch.openech.model.common.Canton;
import ch.openech.model.person.Person;
import ch.openech.model.person.Relation;
import junit.framework.Assert;

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
		Assert.assertEquals(father.officialName, child.officialName);

		Assert.assertEquals("CH", child.placeOfBirth.countryIdentification.countryIdISO2);
		Assert.assertEquals(new Integer(2196), child.placeOfBirth.municipalityIdentification.id);
		Assert.assertEquals("Fribourg", child.placeOfBirth.municipalityIdentification.municipalityName);
		Assert.assertEquals(Codes.findCode(Canton.class, "FR"), child.placeOfBirth.municipalityIdentification.canton);
		
		Relation motherRelation = child.getMother();
		Assert.assertNotNull(motherRelation.partner.personIdentification);
		Assert.assertTrue(IdUtils.equals(mother, motherRelation.partner.personIdentification));
		
		Relation fatherRelation = child.getFather();
		Assert.assertNotNull(fatherRelation.partner.personIdentification);
		Assert.assertTrue(IdUtils.equals(father, fatherRelation.partner.personIdentification));
	}
	
}
