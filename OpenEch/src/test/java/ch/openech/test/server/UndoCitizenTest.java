package ch.openech.test.server;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.mj.util.StringUtils;
import ch.openech.server.ServerCallResult;

public class UndoCitizenTest extends AbstractServerTest {

	private static final String vn = "7561829871378";
	private String id;
	
	@Before
	public void createPerson() throws Exception {
		id = insertPerson(vn);
	}

	@Test
	public void naturalizeSwiss() throws Exception {
		ServerCallResult result = processFile("testPerson/undoCitizen/person.xml");
		String person_id = result.createdPersonId;

		Person person = load(person_id);
		
		int placeOfOriginCountBefore = person.placeOfOrigin.size();
		
		result = processFile("samples/eCH-0020/InfostarSamples/Buergerrecht - Nationalité/eventUndoCitizen/data_53722700000000543.xml");
		
		person = load(person_id);

		Assert.assertEquals(placeOfOriginCountBefore, person.placeOfOrigin.size());

		for (PlaceOfOrigin placeOfOrigin : person.placeOfOrigin) {
			if (!StringUtils.equals("Köniz", placeOfOrigin.originName)) continue;
			Assert.assertEquals("2006-10-12", placeOfOrigin.expatriationDate);
		}
	}
	
}