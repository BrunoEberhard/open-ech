package ch.openech.xml;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.model.test.ModelTest;

import ch.ech.ech0071.Canton;
import ch.ech.ech0071.CantonAbbreviation;
import ch.ech.ech0071.Nomenclature;
import ch.ech.ech0071.Nomenclature.Cantons;
import ch.ech.ech0098.Organisation;

public class EchReaderTest {

	@Test
	public void testEch71WriteCantons() throws Exception {
		Nomenclature nomenclature = new Nomenclature();
		nomenclature.validFrom = LocalDate.now().minusMonths(1);

		nomenclature.cantons = new Cantons();
		nomenclature.cantons.canton = new ArrayList<>();
		for (int i = 1; i <= 26; i++) {
			Canton canton = new Canton();
			canton.cantonId = i;
			canton.setCantonAbbreviation(CantonAbbreviation.values()[i - 1]);
			canton.cantonLongName = "Kanton" + i;
			canton.cantonDateOfChange = LocalDate.now();
			nomenclature.cantons.canton.add(canton);
		}

		String xml = EchWriter.serialize(nomenclature);
		
		StringReader sr = new StringReader(xml);
		try (EchReader er = new EchReader(sr)) {
			nomenclature = (Nomenclature) er.read();
			Assert.assertEquals(26, nomenclature.cantons.canton.size());
		}
	}

//	@Test
//	public void testReadRandomXml() throws Exception {
//		String fileName = "/ch/openech/test/server/testPerson/BirthTest_7566223399589.xml";
//		InputStream inputStream = getClass().getResourceAsStream(fileName);
//		try (EchReader er = new EchReader(inputStream)) {
//			Object o = er.read();
//		}
//	}

//	@Test
//	public void testReadXmlPerson() throws Exception {
//		String fileName = "/ch/openech/test/server/testPerson/mariage/person_7560584727838.xml";
//		InputStream inputStream = getClass().getResourceAsStream(fileName);
//		try (EchReader er = new EchReader(inputStream)) {
//			Delivery delivery = (Delivery) er.read();
//			Person o = delivery.baseDelivery.messages.get(0).baseDeliveryPerson.person;
//			SqlRepository repository = new SqlRepository(DataSourceFactory.embeddedDataSource(), o.getClass());
//			repository.insert(o);
//		}
//	}

	@Test
	public void testOrganisation() throws Exception {
		ModelTest test = new ModelTest(Organisation.class);
		test.assertValid();
	}
	
}
