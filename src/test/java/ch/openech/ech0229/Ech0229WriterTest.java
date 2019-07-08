package ch.openech.ech0229;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ch.ech.ech0097.OrganisationIdentification;
import ch.ech.ech0229.ContactAndAdministration;
import ch.ech.ech0229.Header;
import ch.ech.ech0229.Source;
import ch.ech.ech0229.TaxDeclarationLegalEntity;
import ch.openech.model.EchSchemaValidation;
import ch.openech.xml.EchReader;
import ch.openech.xml.EchWriter;

// https://stackoverflow.com/questions/20807066/how-to-validate-xml-against-xsd-1-1-in-java
public class Ech0229WriterTest {

	@Test
	@Ignore // läuft nur mit xsd 1.1
	public void testReadAndWriteExample() throws Exception {
		System.setProperty("jdk.xml.maxOccurLimit", "20000");
		try (EchReader reader = new EchReader(this.getClass().getResourceAsStream("example229.xml"))) {
			Object o = reader.read();
			
			String string = EchWriter.serialize(o);
			
			Assert.assertEquals("ok", EchSchemaValidation.validate(string));
		}
	}

	@Test
	@Ignore // noch nicht fertig
	public void testWrite() throws Exception {
		System.setProperty("jdk.xml.maxOccurLimit", "20000");
		TaxDeclarationLegalEntity tax = new TaxDeclarationLegalEntity();
		tax.minorVersion = 1;

		tax.header = new Header();
		tax.header.taxPeriod = 2018;
		tax.header.source = Source._0;
		tax.header.sourceDescription = "OpenEch Test";

		tax.content.contactAndAdministration = new ContactAndAdministration();
		tax.content.contactAndAdministration.organisation = new OrganisationIdentification();
		tax.content.contactAndAdministration.organisation.uid.value = "CHE123456789";
		tax.content.contactAndAdministration.organisation.organisationName = "Test AG";

		tax.content.netProfit.taxableIncome.cantonalTax = 100000L;
		tax.content.netProfit.taxableIncome.federalTax = 120000L;

		String string = EchWriter.serialize(tax);

		Assert.assertEquals("ok", EchSchemaValidation.validate(string));
	}

}
