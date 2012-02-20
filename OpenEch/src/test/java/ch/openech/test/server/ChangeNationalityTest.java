package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;

public class ChangeNationalityTest extends AbstractServerTest {

	private static final String vn = "7563806343847";
	private String id;
	
	@Before
	public void createPerson() throws Exception {
		id = insertPerson(vn);
	}

	@Test
	public void changeNationality() throws Exception {
		processFile("samples/eCH-0020/InfostarSamples/Buergerrecht - Nationalité/eventChangeNationality/data_53868900000000023.xml");
		
		Person person = load(id);
		
		Assert.assertNotNull(person);
		Assert.assertEquals("2", person.nationality.nationalityStatus);
		Assert.assertEquals("8236", person.nationality.nationalityCountry.countryId);	
		Assert.assertEquals("Spanien", person.nationality.nationalityCountry.countryNameShort);	
	}

	@Test
	public void changeResidencePermit() throws Exception {
		processFile("testPerson/changeNationality/changeResidencePermitTest.xml");
		Person person = load(id);
		
		Assert.assertEquals("2010-01-31", person.foreign.residencePermitTill);
		Assert.assertEquals("01", person.foreign.residencePermit);

		Assert.assertEquals(1, person.occupation.size());
	}
	
}
