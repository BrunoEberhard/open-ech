package ch.openech.frontend.ech0011;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.application.Application;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.impl.json.JsonFrontend;
import org.minimalj.util.EqualsHelper;

import ch.ech.ech0006.ResidencePermit;
import ch.ech.ech0011.Person;
import ch.ech.ech0011.ResidencePermitData;
import ch.openech.OpenEch;

public class ResidencePermitFormElementTest {

	static {
		Application.setInstance(new OpenEch());
		Frontend.setInstance(new JsonFrontend());
	}

	private ResidencePermitFormElement element = new ResidencePermitFormElement(Person.$.residencePermit, false);

	@Test
	public void testEmpty() {
		ResidencePermitData data = new ResidencePermitData();
		test(data);
	}

	@Test
	public void testPermit() {
		ResidencePermitData data = new ResidencePermitData();
		data.residencePermit = ResidencePermit._01;
		test(data);
	}

	@Test
	public void testEntryDate() {
		ResidencePermitData data = new ResidencePermitData();
		data.residencePermit = ResidencePermit._01;
		data.entryDate = LocalDate.of(2001, 02, 03);
		test(data);
	}

	@Test
	public void testEntryCompolete() {
		ResidencePermitData data = new ResidencePermitData();
		data.residencePermit = ResidencePermit._01;
		data.entryDate = LocalDate.of(2001, 02, 03);
		data.residencePermitValidFrom = LocalDate.of(2010, 11, 12);
		data.residencePermitValidTill = LocalDate.of(2011, 01, 31);
		test(data);
	}

	private void test(ResidencePermitData data) {
		String text = element.render(data);
		ResidencePermitData data2 = element.parse(text);
		Assert.assertTrue(EqualsHelper.equals(data, data2));
	}

}
