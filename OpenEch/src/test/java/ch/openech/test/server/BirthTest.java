package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.code.NationalityStatus;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;

public class BirthTest extends AbstractServerTest {

	private static final String VN_FATHER = "7561829871378";
	private static final String VN_MOTHER = "7566223399589";

	private Person mother, father;
	
	@Before
	public void createPerson() throws Exception {
		mother = insertPerson(VN_MOTHER);
		father = insertPerson(VN_FATHER);
	}
	
	@Test
	public void birth() throws Exception {

		Person child = processFile("samples/eCH-0020/InfostarSamples/Geburt - Naissance/data_53765000000000033.xml");
		
		Assert.assertNotNull(child);
		Assert.assertEquals("Hauber", child.officialName);
		Assert.assertEquals("Daniela", child.firstName);
		Assert.assertTrue(child.maritalStatus.isLedig());
		Assert.assertEquals(new LocalDate(2005, 5, 25), child.dateOfBirth);

		Assert.assertEquals("GE", child.placeOfBirth.municipalityIdentification.cantonAbbreviation.canton);
		Assert.assertEquals("Genève", child.placeOfBirth.municipalityIdentification.municipalityName);
		Assert.assertEquals(new Integer(6621), child.placeOfBirth.municipalityIdentification.municipalityId);

		Assert.assertEquals(NationalityStatus.with, child.nationality.nationalityStatus);
		Assert.assertEquals(new Integer(8100), child.nationality.nationalityCountry.countryId);
		Assert.assertEquals("Schweiz", child.nationality.nationalityCountry.countryNameShort);

		Relation motherRelation = child.getMother();
		Assert.assertEquals(mother.id, motherRelation.partner.id);
		Relation fatherRelation = child.getFather();
		Assert.assertEquals(father.id, fatherRelation.partner.id);
	}
	
	
}
