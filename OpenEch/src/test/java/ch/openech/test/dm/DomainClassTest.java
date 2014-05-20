package ch.openech.test.dm;

import java.util.ResourceBundle;

import junit.framework.Assert;

import org.junit.Test;
import org.minimalj.model.Codes;
import org.minimalj.model.test.ModelTest;

import ch.openech.model.organisation.Organisation;
import ch.openech.model.person.Person;

public class DomainClassTest {

	@Test
	public void testOpenEchDomainClasses() {
		boolean orgCodesAvailable = Codes.getCode("commercialRegisterStatus") != null;
		if (!orgCodesAvailable) {
			Codes.addCodes(ResourceBundle.getBundle("ch.openech.model.organisation.types.ech_organisation"));
		}
		boolean personCodesAvailable = Codes.getCode("basedOnLaw") != null;
		if (!personCodesAvailable) {
			Codes.addCodes(ResourceBundle.getBundle("ch.openech.model.person.types.ech_person"));
		}
		
		ModelTest test = new ModelTest(Person.class, Organisation.class); // ThirdPartyMove.class
		if (!test.getProblems().isEmpty()) {
			for (String s : test.getProblems()) {
				System.err.println(s);
			}
			Assert.fail();
		}
	}
}