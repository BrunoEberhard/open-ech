package ch.openech.test.server;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.code.NationalityStatus;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.server.ServerCallResult;

public class BirthTest extends AbstractServerTest {

	private static final String VN_FATHER = "7561829871378";
	private static final String VN_MOTHER = "7566223399589";

	private String mother_id, father_id;
	
	@Before
	public void createPerson() throws Exception {
		mother_id = insertPerson(VN_MOTHER);
		father_id = insertPerson(VN_FATHER);
	}
	
	@Test
	public void birth() throws Exception {
		Person mother = load(mother_id);
		Person father = load(father_id);

		ServerCallResult result = processFile("samples/eCH-0020/InfostarSamples/Geburt - Naissance/data_53765000000000033.xml");
		String child_id = result.createdPersonId;
		Person child = load(child_id);
		
		Assert.assertNotNull(child);
		Assert.assertEquals("Hauber", child.personIdentification.officialName);
		Assert.assertEquals("Daniela", child.personIdentification.firstName);
		Assert.assertTrue(child.maritalStatus.isLedig());
		Assert.assertEquals(new LocalDate(2005, 5, 25), child.personIdentification.dateOfBirth);

		Assert.assertEquals("GE", child.placeOfBirth.municipalityIdentification.cantonAbbreviation.canton);
		Assert.assertEquals("Genève", child.placeOfBirth.municipalityIdentification.municipalityName);
		Assert.assertEquals(new Integer(6621), child.placeOfBirth.municipalityIdentification.municipalityId);

		Assert.assertEquals(NationalityStatus.with, child.nationality.nationalityStatus);
		Assert.assertEquals(new Integer(8100), child.nationality.nationalityCountry.countryId);
		Assert.assertEquals("Schweiz", child.nationality.nationalityCountry.countryNameShort);

		Relation motherRelation = child.getRelation(TypeOfRelationship.Mutter);
		Assert.assertTrue(mother.personIdentification.isEqual(motherRelation.partner));
		Relation fatherRelation = child.getRelation(TypeOfRelationship.Vater);
		Assert.assertTrue(father.personIdentification.isEqual(fatherRelation.partner));
	}
	
	
}
