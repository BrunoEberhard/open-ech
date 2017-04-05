package ch.openech.test.dm;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.util.Codes;

import ch.openech.model.organisation.types.LegalForm;
import ch.openech.test.server.AbstractServerTest;

public class LegalFormCsvTest extends AbstractServerTest {

	@Test
	public void testAvailableLegalForms() {
		Assert.assertTrue("Mindestens 30 Gesellschaftsformen (LegalForm) sollten vom beigelegten csv file gelesen werden", Codes.get(LegalForm.class).size() > 30);
	}
}
