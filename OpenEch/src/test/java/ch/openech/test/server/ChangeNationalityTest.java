package ch.openech.test.server;

import junit.framework.Assert;

import org.threeten.bp.LocalDate;
import org.junit.Before;
import org.junit.Test;

import  ch.openech.model.code.NationalityStatus;
import  ch.openech.model.code.ResidencePermit;
import  ch.openech.model.person.Person;

public class ChangeNationalityTest extends AbstractServerTest {

	private static final String vn = "7563806343847";
	private Person person;
	
	@Before
	public void createPerson() throws Exception {
		person = insertPerson(vn);
	}

	@Test
	public void changeNationality() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Buergerrecht - Nationalité/eventChangeNationality/data_53868900000000023.xml");
		person = reload(person);
		
		Assert.assertNotNull(person);
		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals(new Integer(8236), person.nationality.nationalityCountry.id);	
		Assert.assertEquals("Spanien", person.nationality.nationalityCountry.countryNameShort);	
	}

	@Test
	public void changeResidencePermit() throws Exception {
		processFile("testPerson/changeNationality/changeResidencePermitTest.xml");
		person = reload(person);
		
		Assert.assertEquals(LocalDate.of(2010, 1, 31), person.foreign.residencePermitTill);
		Assert.assertEquals(ResidencePermit.Saisonarbeiter, person.foreign.residencePermit);

		Assert.assertEquals(1, person.occupation.size());
	}
	
}
