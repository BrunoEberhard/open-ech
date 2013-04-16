package ch.openech.test.dm;

import java.util.ResourceBundle;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.Person;
import ch.openech.mj.model.Codes;
import ch.openech.mj.model.test.ModelTest;

public class DomainClassTest {

	@Test
	public void testOpenEchDomainClasses() {
		boolean codesAvailable = Codes.getCode("commercialRegisterStatus") != null;
		if (!codesAvailable) {
			Codes.addCodes(ResourceBundle.getBundle("ch.openech.dm.organisation.types.ech_organisation"));
			Codes.addCodes(ResourceBundle.getBundle("ch.openech.dm.person.types.ech_person"));
		}
		
		ModelTest test = new ModelTest();
		test.test(Person.class);
		test.test(Organisation.class);
//		testDomainClass(ThirdPartyMove.class);
		if (!test.getProblems().isEmpty()) {
			for (String s : test.getProblems()) {
				System.err.println(s);
			}
			Assert.fail();
		}
	}
}