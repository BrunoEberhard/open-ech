package ch.openech.test.dm;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.model.test.ModelTest;

import ch.openech.model.organisation.Organisation;
import ch.openech.model.person.Person;

public class DomainClassTest {

	@Test
	public void testOpenEchDomainClasses() {
		ModelTest test = new ModelTest(Person.class, Organisation.class); // ThirdPartyMove.class
		if (!test.getProblems().isEmpty()) {
			test.logProblems();
			Assert.fail();
		}
	}
	
}