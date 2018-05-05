package ch.openech.xml;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Test;

import ch.ech.ech0071.v1.Canton;
import ch.ech.ech0071.v1.CantonAbbreviation;
import ch.ech.ech0071.v1.Nomenclature;

public class EchWriterTest {

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

		StringWriter stringWriter = new StringWriter();
		EchWriter writer = new EchWriter(stringWriter);
		writer.writeDocument(nomenclature);
		writer.close();

		System.out.println(stringWriter.toString());

	}

}
