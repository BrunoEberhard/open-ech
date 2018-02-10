package ch.openech.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Test;

import ch.ech.ech0071.v1.Canton;
import ch.ech.ech0071.v1.CantonAbbreviation;
import ch.ech.ech0071.v1.Nomenclature;
import ch.openech.xml.model.XsdReader;
import ch.openech.xml.model.XsdSchema;

public class EchReaderTest {

	@Test
	public void testEch71WriteCantons() throws Exception {
		Nomenclature nomenclature = new Nomenclature();
		nomenclature.validFrom = LocalDate.now().minusMonths(1);
		nomenclature.cantons = new Nomenclature.Cantons();

		nomenclature.cantons.canton = new ArrayList<>();
		for (int i = 1; i <= 26; i++) {
			Canton canton = new Canton();
			canton.cantonId = i;
			canton.cantonAbbreviation = CantonAbbreviation.values()[i - 1];
			canton.cantonLongName = "Kanton" + i;
			canton.cantonDateOfChange = LocalDate.now();
			nomenclature.cantons.canton.add(canton);
		}

		XsdReader reader = new XsdReader();
		XsdSchema schema = reader.read("http://www.ech.ch/xmlns/eCH-0071/1/eCH-0071-1-0.xsd");

		StringWriter stringWriter = new StringWriter();
		EchWriter writer = new EchWriter(stringWriter, schema);
		writer.write(schema.elements.get(0), nomenclature);
		writer.close();

		String xml = stringWriter.toString();
		
		StringReader sr = new StringReader(xml);
		EchReader er = new EchReader(sr);
		er.read();
	}

}
