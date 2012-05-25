package ch.openech.datagenerator;

import java.lang.reflect.Field;
import java.util.List;

import ch.openech.client.e44.VnField;
import ch.openech.dm.code.MaritalStatus;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.common.Plz;
import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.mj.autofill.FirstNameGenerator;
import ch.openech.mj.autofill.NameGenerator;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.mj.edit.value.PropertyAccessor;
import ch.openech.server.EchServer;
import ch.openech.util.PlzImport;
import ch.openech.xml.read.StaxEch0071;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0148;

public class DataGenerator {

	private static List<MunicipalityIdentification> municipalityIdentifications = StaxEch0071.getInstance().getMunicipalityIdentifications();

	public static void generatePerson(WriterEch0020 writerEch0020) {
		try {
			String xml = writerEch0020.moveIn(person());
			EchServer.getInstance().process(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Person person() {
		Person person = new Person();
		person.personIdentification.officialName = NameGenerator.officialName();
		boolean male = Math.random() > .5;
		person.personIdentification.firstName = FirstNameGenerator.getFirstName(male);
		person.personIdentification.sex = male ? "1" : "2";
		person.setDateOfBirth(DateField.generateRandom());
		if (Math.random() > .9) {
			person.dateOfDeath = DateField.generateRandom();
		}
		person.personIdentification.vn = VnField.generateRandom();
		person.callName = "Lorem Ipsum";
		person.placeOfBirth = place();
		
		person.arrivalDate = DateField.generateRandom();
		person.residence.reportingMunicipality = createJona();
		
		person.placeOfOrigin.add(placeOfOrigin());
		while (Math.random() > .65) {
			person.placeOfOrigin.add(placeOfOrigin());
		}
		
		person.dwellingAddress = dwellingAddress();

		if (Math.random() < .2) {
			person.comesFrom = place();
		}

		return person;
	}

	public static Person completeFilledPerson() {
		Person person = new Person();
		for (Field field : person.getClass().getFields()) {
			String key = field.getName();
			if (key.endsWith("Name")) {
				PropertyAccessor.set(person, key, key.substring(0, key.length() -4) + "Test");
			}
		}
		person.personIdentification.sex = "2";
		person.setDateOfBirth("1999-01-02");
		person.dateOfDeath = "2010-03-04";
		person.personIdentification.vn = VnField.generateRandom();
		person.placeOfBirth = place();
		
		person.maritalStatus.maritalStatus = MaritalStatus.Ledig.value;
		person.maritalStatus.dateOfMaritalStatus = "2004-02-03";
		person.separation.separation = "2";
		person.separation.dateOfSeparation = "2005-05-12";
		
		person.typeOfResidence = "1";
		person.arrivalDate = "1999-01-02";
		person.departureDate = "2010-03-04";
		person.residence.reportingMunicipality = createJona();
		
		person.placeOfOrigin.add(placeOfOrigin());
		person.dwellingAddress = dwellingAddress();
		
		person.comesFrom = place();
		person.goesTo = place();
		
		person.dataLock = "1";
		person.paperLock = "1";
		person.languageOfCorrespondance = "fr";
		
		Occupation occupation = new Occupation();
		occupation.kindOfEmployment = "1";
		person.occupation.add(occupation);
		
		Relation relation = new Relation();
		relation.typeOfRelationship = "1";
		person.relation.add(relation);
		
		person.typeOfResidence = "2";
		person.nationality.nationalityStatus = "3";
		
		return person;
	}
	
	private static MunicipalityIdentification createJona() {
		MunicipalityIdentification reportingMunicipality = new MunicipalityIdentification();
		reportingMunicipality.historyMunicipalityId = "14925";
		reportingMunicipality.cantonAbbreviation = "SG";
		reportingMunicipality.municipalityId = "3340";
		reportingMunicipality.municipalityName = "Rapperswil-Jona";
		return reportingMunicipality;
	}
	
	public static Place place() {
		Place place = new Place();
//		if (Math.random() < 0.86) {
			place.setMunicipalityIdentification(municipalityIdentifications.get((int)(Math.random() * municipalityIdentifications.size())));
//		} else {
//			CountryIdentificationDAO.getInstance().getCountryIdentification(id)
//		}
		return place;
	}
	
	public static PlaceOfOrigin placeOfOrigin() {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		placeOfOrigin.naturalizationDate = DateField.generateRandom();
		
		MunicipalityIdentification municipalityIdentification = municipalityIdentifications.get((int)(Math.random() * municipalityIdentifications.size()));
		placeOfOrigin.originName = municipalityIdentification.municipalityName;
		placeOfOrigin.canton = municipalityIdentification.cantonAbbreviation;
		
		return placeOfOrigin;
	}
	
	public static MunicipalityIdentification municipalityIdentification() {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		MunicipalityIdentification m = municipalityIdentifications.get((int)(Math.random() * municipalityIdentifications.size()));
		m.copyTo(municipalityIdentification);
		return municipalityIdentification;
	}
	
	public static DwellingAddress dwellingAddress() {
		DwellingAddress dwellingAddress = new DwellingAddress();
		dwellingAddress.EGID = "" + (int)(Math.random() * 999999998 + 1);
		dwellingAddress.EWID = "" + (int)(Math.random() * 998 + 1);
		dwellingAddress.typeOfHousehold =  "" + (int)(Math.random() * 4);
		dwellingAddress.mailAddress = address(true, false, false);
		return dwellingAddress;
	}
	
	public static Address address(boolean swiss, boolean person, boolean organisation) {
		Address address = new Address();
		
		address.street = NameGenerator.street();
		if (Math.random() < 0.75) {
			address.houseNumber.houseNumber = "" + (int)(1 + Math.random() * 42);
		} else if (Math.random() < 0.8) {
			address.houseNumber.houseNumber = "" + (int)(1 + Math.random() * 420);
		}
		
		if (swiss || Math.random() < .9) {
			List<Plz> plzList = PlzImport.getInstance().getPlzList();
			int pos = (int) (Math.random() * plzList.size());
			Plz plz = plzList.get(pos);
			address.country = "CH";
			address.zip.setPlz(plz);
			address.town = plz.ortsbezeichnung;
		} else {
			if (Math.random() < .5) {
				address.postOfficeBoxText = "POSTFACH";
				address.postOfficeBoxNumber = "" + (int)(1000 + Math.random() * 9000);
			}
			address.zip.foreignZipCode = "" + ((int)(Math.random() * 90000 + 10000));
			address.town = NameGenerator.officialName() + "Town";
			address.country = "DE";
		}
		
		return address;
	}
	
	public static Organisation organisation() {
		Organisation organisation = new Organisation();
		// TODO reactivate organisation.organisationName = OrganisationNameGenerator.getName();
		if (organisation.organisationName.length() > 60) {
			organisation.organisationAdditionalName = organisation.organisationName.substring(60);
			organisation.organisationName = organisation.organisationName.substring(0, 60);
		}
		organisation.uid = "ADM323423421";
		organisation.foundationDate = DateField.generateRandom();
		organisation.arrivalDate = DateField.generateRandom();
		organisation.reportingMunicipality = createJona();
		organisation.businessAddress = dwellingAddress();
		
		if (Math.random() < .2) {
			organisation.comesFrom = place();
		}

		return organisation;
	}
	
	public static void generateOrganisation(WriterEch0148 writerEch0148) {
		try {
			String xml = writerEch0148.moveIn(organisation());
			EchServer.getInstance().processOrg(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
