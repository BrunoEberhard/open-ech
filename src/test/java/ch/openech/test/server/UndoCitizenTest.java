package ch.openech.test.server;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.minimalj.util.StringUtils;

import  ch.openech.model.person.Person;
import  ch.openech.model.person.PlaceOfOrigin;

public class UndoCitizenTest extends AbstractServerTest {

	private static final String vn = "7561829871378";
	private Person p;
	
	@Before
	public void createPerson() throws Exception {
		p = insertPerson(vn);
	}

	@Test
	public void naturalizeSwiss() throws Exception {
		Person person = reload(p);
		
		int placeOfOriginCountBefore = person.placeOfOrigin.size();
		
		processFile("samples/eCH-0020/InfostarSamples/Buergerrecht - Nationalité/eventUndoCitizen/data_53722700000000543.xml");
		
		person = reload(p);

		Assert.assertEquals(placeOfOriginCountBefore, person.placeOfOrigin.size());

		for (PlaceOfOrigin placeOfOrigin : person.placeOfOrigin) {
			if (!StringUtils.equals("Köniz", placeOfOrigin.originName)) continue;
			Assert.assertEquals(LocalDate.of(2006, 10, 12), placeOfOrigin.expatriationDate);
		}
	}
	
}
