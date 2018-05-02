package ch.openech.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import ch.ech.ech0071.v1.Canton;
import ch.ech.ech0071.v1.CantonAbbreviation;
import ch.ech.ech0071.v1.Nomenclature;

public class EchReaderTest {

	@Test
	public void testEch71WriteCantons() throws Exception {
		Nomenclature nomenclature = new Nomenclature();
		nomenclature.validFrom = LocalDate.now().minusMonths(1);

		nomenclature.cantons = new ArrayList<>();
		for (int i = 1; i <= 26; i++) {
			Canton canton = new Canton();
			canton.cantonId = i;
			canton.cantonAbbreviation = CantonAbbreviation.values()[i - 1];
			canton.cantonLongName = "Kanton" + i;
			canton.cantonDateOfChange = LocalDate.now();
			nomenclature.cantons.add(canton);
		}

		XsdModel reader = new XsdModel();

		StringWriter stringWriter = new StringWriter();
		EchWriter writer = new EchWriter(stringWriter);
		writer.writeDocument(nomenclature);
		writer.close();

		String xml = stringWriter.toString();
		
		StringReader sr = new StringReader(xml);
		try (EchReader er = new EchReader(sr)) {
			nomenclature = (Nomenclature) er.read();
			Assert.assertEquals(26, nomenclature.cantons.size());
		}
	}

	@Test
	public void testReadRandomXml() throws Exception {
		String fileName = "/ch/openech/test/server/testPerson/BirthTest_7566223399589.xml";
		InputStream inputStream = getClass().getResourceAsStream(fileName);
		try (EchReader er = new EchReader(inputStream)) {
			Object o = er.read();
		}
	}
	
}
