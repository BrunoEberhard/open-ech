package ch.openech.ech0020.v3;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import ch.ech.ech0006.ResidencePermit;
import ch.ech.ech0007.SwissMunicipality;
import ch.ech.ech0008.Country;
import ch.ech.ech0011.Destination;
import ch.ech.ech0011.DwellingAddress;
import ch.ech.ech0011.MaritalStatus;
import ch.ech.ech0011.NationalityStatus;
import ch.ech.ech0011.ResidencePermitData;
import ch.ech.ech0011.TypeOfHousehold;
import ch.ech.ech0020.v3.BaseDeliveryPerson;
import ch.ech.ech0020.v3.Delivery;
import ch.ech.ech0020.v3.Delivery.BaseDelivery;
import ch.ech.ech0020.v3.EventBaseDelivery;
import ch.ech.ech0020.v3.Header;
import ch.ech.ech0020.v3.ReportingMunicipality;
import ch.ech.ech0021.DataLock;
import ch.ech.ech0044.PersonIdentification;
import ch.ech.ech0044.Sex;
import ch.ech.ech0058.Action;
import ch.ech.ech0071.CantonAbbreviation;
import ch.openech.model.EchSchemaValidation;
import ch.openech.xml.EchWriter;
import ch.openech.xml.YesNo;

public class EventBaseDeliveryTest {

	@Test
	public void testXml() throws Exception {
		EventBaseDelivery event = new EventBaseDelivery();
		event.baseDeliveryPerson = new BaseDeliveryPerson();
		event.baseDeliveryPerson.personIdentification = new PersonIdentification();
		event.baseDeliveryPerson.personIdentification.officialName = "Eberhard";
		event.baseDeliveryPerson.personIdentification.firstName = "Bruno Joseph";
		event.baseDeliveryPerson.personIdentification.sex = Sex._1;
		event.baseDeliveryPerson.personIdentification.localPersonId.namedIdCategory = "OpenEch";
		event.baseDeliveryPerson.personIdentification.localPersonId.namedId = "test";
		event.baseDeliveryPerson.personIdentification.dateOfBirth.value = "1974-08-02";

		event.baseDeliveryPerson.nameInfo.nameData.officialName = "Eberhard";
		event.baseDeliveryPerson.nameInfo.nameData.firstName = "Bruno";
		event.baseDeliveryPerson.birthInfo.birthData.dateOfBirth.value = "1974-08-02";
		event.baseDeliveryPerson.birthInfo.birthData.placeOfBirth.swissTown = new SwissMunicipality();
		event.baseDeliveryPerson.birthInfo.birthData.placeOfBirth.swissTown.municipalityShortName = "Jona";
		event.baseDeliveryPerson.birthInfo.birthData.placeOfBirth.swissTown.cantonAbbreviation = CantonAbbreviation.SG;
		event.baseDeliveryPerson.birthInfo.birthData.sex = Sex._1;

		event.baseDeliveryPerson.religionData.religion = "666";

		event.baseDeliveryPerson.maritalInfo.maritalData.maritalStatus = MaritalStatus._1;

		event.baseDeliveryPerson.nationalityData.nationalityStatus = NationalityStatus._1;

		event.baseDeliveryPerson.residencePermitData = new ResidencePermitData();
		event.baseDeliveryPerson.residencePermitData.setResidencePermit(ResidencePermit._01);

		event.baseDeliveryPerson.lockData.dataLock = DataLock._0;
		event.baseDeliveryPerson.lockData.paperLock = YesNo._1;

		event.hasMainResidence = new ReportingMunicipality();
		event.hasMainResidence.reportingMunicipality = new SwissMunicipality();
		event.hasMainResidence.reportingMunicipality.municipalityShortName = "Jona";
		event.hasMainResidence.reportingMunicipality.cantonAbbreviation = CantonAbbreviation.SG;
		event.hasMainResidence.comesFrom = new Destination();
		event.hasMainResidence.comesFrom.generalPlace.swissTown = new SwissMunicipality();
		event.hasMainResidence.comesFrom.generalPlace.swissTown.municipalityShortName = "Rapperswil";
		event.hasMainResidence.reportingMunicipality.cantonAbbreviation = CantonAbbreviation.SG;
		event.hasMainResidence.arrivalDate = LocalDate.now();
		event.hasMainResidence.dwellingAddress = new DwellingAddress();
		event.hasMainResidence.dwellingAddress.address.street = "Grütstrasse";
		event.hasMainResidence.dwellingAddress.address.swissZipCode = 8645;
		event.hasMainResidence.dwellingAddress.address.town = "Jona";
		event.hasMainResidence.dwellingAddress.address.country = new Country();
		event.hasMainResidence.dwellingAddress.address.country.iso2Id = "CH";
		event.hasMainResidence.dwellingAddress.typeOfHousehold = TypeOfHousehold._1;

		Delivery delivery = new Delivery();
		delivery.baseDelivery = new BaseDelivery();
		delivery.baseDelivery.messages = new ArrayList<>();
		delivery.baseDelivery.messages.add(event);

		delivery.deliveryHeader = new Header();
		delivery.deliveryHeader.senderId = UUID.randomUUID().toString();
		delivery.deliveryHeader.sendingApplication.manufacturer = "OpenEch";
		delivery.deliveryHeader.sendingApplication.product = "Test";
		delivery.deliveryHeader.sendingApplication.productVersion = "1.0";
		delivery.deliveryHeader.messageId = UUID.randomUUID().toString();
		delivery.deliveryHeader.messageType = "Sali";
		delivery.deliveryHeader.messageDate = LocalDateTime.now();
		delivery.deliveryHeader.action = Action._1;
		delivery.deliveryHeader.testDeliveryFlag = true;

		String string = EchWriter.serialize(delivery);
		System.out.println(string);

		String validationResult = EchSchemaValidation.validate(string);

		Assert.assertEquals(EchSchemaValidation.OK, validationResult);
	}
}
