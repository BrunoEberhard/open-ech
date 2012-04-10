package ch.openech.test.xml;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.common.Address;
import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.server.EchServer;
import ch.openech.xml.read.StaxEch0101;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0101;

public class PersonExtendedInformationXmlTest {

	@Test
	public void RoundTripTest() throws Exception {
		WriterEch0101 writer = new WriterEch0101(EchNamespaceContext.getNamespaceContext(101, "1.0"));
	
		PersonExtendedInformation information = new PersonExtendedInformation();
		information.armedForcesLiability = "1";
		
		PersonIdentification personIdentification = new PersonIdentification();
		personIdentification.firstName = "TestVorname";
		personIdentification.officialName = "TestNachname";
		personIdentification.sex = "1";
		personIdentification.dateOfBirth = "2001-12-31";
		information.personIdentification = personIdentification;
		
		Address address1 = new Address();
		address1.organisationName = "CSS ;)";
		address1.addressLine1 = "Addresszeile 1";
		address1.addressLine2 = "Addresszeile 2";
		address1.street = "Teststrasse";
		address1.houseNumber.houseNumber = "42";
		address1.town = "Jona";
		address1.zip.swissZipCode = "8645";
		information.insuranceAddress = address1;
		
		String xml = writer.informationRoot(information);
		
		String validationMessage = EchServer.validate(xml);
		boolean valid = EchServer.OK.equals(validationMessage);
		if (!valid) {
			System.out.println(xml);
			System.out.println(validationMessage);
		}
		Assert.assertTrue(valid);
		
		PersonExtendedInformation informationOut = new StaxEch0101().read(xml);
		
		Assert.assertEquals(information.armedForcesLiability, informationOut.armedForcesLiability);
		Assert.assertEquals(information.insuranceAddress.addressLine1, informationOut.insuranceAddress.addressLine1);
	}
}
