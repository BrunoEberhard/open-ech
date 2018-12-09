package ch.openech.xml;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ch.ech.ech0071.Canton;
import ch.ech.ech0071.CantonAbbreviation;
import ch.ech.ech0071.District;
import ch.ech.ech0071.Nomenclature;
import ch.openech.model.EchSchemaValidation;

public class EchWriterTest {

	@Test
	@Ignore
	public void testEch71WriteCantons() throws Exception {
		Nomenclature nomenclature = new Nomenclature();
		nomenclature.validFrom = LocalDate.now().minusMonths(1);
		nomenclature.cantons.canton = new ArrayList<>();

		for (int i = 1; i <= 26; i++) {
			Canton canton = new Canton();
			canton.cantonId = i;
			canton.setCantonAbbreviation(CantonAbbreviation.values()[i - 1]);
			canton.cantonLongName = "Kanton" + i;
			canton.cantonDateOfChange = LocalDate.now();
			nomenclature.cantons.canton.add(canton);
		}

		nomenclature.districts.district = new ArrayList<>();
		District district = new District();
		district.id = 12345;
		district.cantonId = 1;
		district.districtLongName = "See Gaster";
		nomenclature.districts.district.add(district);

		StringWriter stringWriter = new StringWriter();
		EchWriter writer = new EchWriter(stringWriter);
		writer.writeDocument(nomenclature);
		writer.close();

		String string = stringWriter.toString();
		System.out.println(string);

		System.out.println(EchSchemaValidation.validate(string));

	}

	@Test
	public void testReadWriteEch071() throws Exception {
		EchReader reader = new EchReader(getClass().getClassLoader().getResourceAsStream("eCH0071.xml"));
		Nomenclature nomenclature = (Nomenclature) reader.read();
		reader.close();

		StringWriter stringWriter = new StringWriter();
		EchWriter writer = new EchWriter(stringWriter);
		writer.writeDocument(nomenclature);
		writer.close();

		String string = stringWriter.toString();
		System.out.println(string.substring(0, 1000));

		Assert.assertEquals(EchSchemaValidation.OK, EchSchemaValidation.validate(string));
	}

}
