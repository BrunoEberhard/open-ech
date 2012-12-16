package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.code.NationalityStatus;
import ch.openech.dm.code.ResidencePermit;
import ch.openech.dm.common.Swiss;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.server.EchServer;
import ch.openech.server.ServerCallResult;

public class NaturalizeSwissTest extends AbstractServerTest {

	private String id;
	
	@Before
	public void createPerson() throws Exception {
		EchServer.getInstance().getPersistence().clear();
		
		ServerCallResult result = processFile("testPerson/naturalizeSwiss/person.xml");
		id = result.createdPersonId;
	}
	
	@Test
	public void naturalizeSwiss() throws Exception {
		Person person = load(id);
		int placeOfOriginCountBefore = person.placeOfOrigin.size();
		
		processFile("samples/eCH-0020/InfostarSamples/Buergerrecht - Nationalité/eventNaturalizeSwiss/data_53741400000000113.xml");
		
		person = load(id);
		
		Assert.assertNotNull(person);
		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals(Swiss.SWISS_COUNTRY_ID, person.nationality.nationalityCountry.countryId);	
		Assert.assertEquals(Swiss.SWISS_COUNTRY_NAME_SHORT, person.nationality.nationalityCountry.countryNameShort);	

		Assert.assertEquals(placeOfOriginCountBefore + 1, person.placeOfOrigin.size());
		PlaceOfOrigin placeOfOrigin = person.placeOfOrigin.get(person.placeOfOrigin.size() - 1);
		Assert.assertEquals("Murten", placeOfOrigin.originName);
		Assert.assertEquals("FR", placeOfOrigin.cantonAbbreviation.canton);
	}

	@Test
	public void undoSwiss() throws Exception {
		Person person = load(id);
		person.nationality.nationalityCountry.countryId = 8345;
		person.nationality.nationalityCountry.countryIdISO2 = "SN";
		person.nationality.nationalityCountry.countryNameShort = "Senegal";
		person.foreign.residencePermit = ResidencePermit.Saisonarbeiter;
		
		process(writer().undoSwiss(person));

		person = load(id);
		Assert.assertNotNull(person);
		Assert.assertEquals(NationalityStatus.with, person.nationality.nationalityStatus);
		Assert.assertEquals(Integer.valueOf(8345), person.nationality.nationalityCountry.countryId);	
		Assert.assertEquals("Senegal", person.nationality.nationalityCountry.countryNameShort);	
		Assert.assertEquals("SN", person.nationality.nationalityCountry.countryIdISO2);	
		
		// Assert.assertEquals("Passname", person.nameOnPassport);	
		Assert.assertEquals(ResidencePermit.Saisonarbeiter, person.foreign.residencePermit);	
	}
	
}
