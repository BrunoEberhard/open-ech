package ch.openech.datagenerator;

import java.lang.reflect.Field;
import java.util.List;

import org.joda.time.LocalDate;

import ch.openech.business.EchService;
import ch.openech.dm.code.NationalityStatus;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.MaritalStatus;
import ch.openech.dm.person.types.Separation;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.dm.types.Language;
import ch.openech.dm.types.Sex;
import ch.openech.dm.types.TypeOfResidence;
import ch.openech.mj.autofill.DateGenerator;
import ch.openech.mj.autofill.FirstNameGenerator;
import ch.openech.mj.autofill.NameGenerator;
import ch.openech.mj.autofill.OrganisationNameGenerator;
import ch.openech.mj.model.EnumUtils;
import ch.openech.mj.model.properties.FlatProperties;
import ch.openech.mj.server.Services;
import ch.openech.util.Plz;
import ch.openech.util.PlzImport;
import ch.openech.xml.read.StaxEch0071;
import ch.openech.xml.read.StaxEch0072;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0148;

public class DataGenerator {

	private static List<MunicipalityIdentification> municipalityIdentifications = StaxEch0071.getInstance().getMunicipalityIdentifications();
	private static List<CountryIdentification> countryIdentifications = StaxEch0072.getInstance().getCountryIdentifications();
	
	public static void generatePerson(WriterEch0020 writerEch0020) {
		try {
			String xml = writerEch0020.moveIn(person());
			Services.get(EchService.class).process(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Person person() {
		Person person = new Person();
		person.personIdentification.officialName = NameGenerator.officialName();
		boolean male = Math.random() > .5;
		person.personIdentification.firstName = FirstNameGenerator.getFirstName(male);
		person.personIdentification.sex = male ? Sex.maennlich : Sex.weiblich;
		person.personIdentification.dateOfBirth = DateGenerator.generateRandomDate();
		if (Math.random() > .9) {
			person.dateOfDeath = DateGenerator.generateRandomDate();
		}
		person.personIdentification.vn.fillWithDemoData();
		person.callName = "Lorem Ipsum";
		person.placeOfBirth = place();
		
		person.arrivalDate = DateGenerator.generateRandomDate();
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
				FlatProperties.set(person, key, key.substring(0, key.length() -4) + "Test");
			}
		}
		person.personIdentification.sex = Sex.weiblich;
		person.personIdentification.dateOfBirth = new LocalDate(1999, 1, 2);
		person.dateOfDeath = new LocalDate(2010, 3, 4);
		person.personIdentification.vn.fillWithDemoData();
		person.placeOfBirth = place();
		
		person.maritalStatus.maritalStatus = MaritalStatus.ledig;
		person.maritalStatus.dateOfMaritalStatus = new LocalDate(2004, 2, 3);
		person.separation.separation = Separation.gerichtlich;
		person.separation.dateOfSeparation = new LocalDate(2005, 5, 12);
		
		person.typeOfResidence = TypeOfResidence.hasMainResidence;
		person.arrivalDate = new LocalDate(1999, 1, 2);
		person.departureDate = new LocalDate(2010, 3, 4);
		person.residence.reportingMunicipality = createJona();
		
		person.placeOfOrigin.add(placeOfOrigin());
		person.dwellingAddress = dwellingAddress();
		
		person.comesFrom = place();
		person.goesTo = place();
		
		person.dataLock = "1";
		person.paperLock = "1";
		person.languageOfCorrespondance = Language.fr;
		
		Occupation occupation = new Occupation();
		occupation.kindOfEmployment = "1";
		person.occupation.add(occupation);
		
		Relation relation = new Relation();
		relation.typeOfRelationship = TypeOfRelationship.Ehepartner;
		person.relation.add(relation);
		
		person.typeOfResidence = TypeOfResidence.hasSecondaryResidence;
		person.nationality.nationalityStatus = EnumUtils.createEnum(NationalityStatus.class, "3");
		
		return person;
	}
	
	private static MunicipalityIdentification createJona() {
		MunicipalityIdentification reportingMunicipality = new MunicipalityIdentification();
		reportingMunicipality.historyMunicipalityId = 14925;
		reportingMunicipality.cantonAbbreviation.canton = "SG";
		reportingMunicipality.municipalityId = 3340;
		reportingMunicipality.municipalityName = "Rapperswil-Jona";
		return reportingMunicipality;
	}
	
	public static Place place() {
		Place place = new Place();
		if (Math.random() < 0.86) {
			place.municipalityIdentification = municipalityIdentification();
		} else {
			place.municipalityIdentification = municipalityIdentification();
			place.countryIdentification = countryIdentification();
		}
		return place;
	}
	
	public static PlaceOfOrigin placeOfOrigin() {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		placeOfOrigin.naturalizationDate = DateGenerator.generateRandomDate();
		
		MunicipalityIdentification municipalityIdentification = municipalityIdentifications.get((int)(Math.random() * municipalityIdentifications.size()));
		placeOfOrigin.originName = municipalityIdentification.municipalityName;
		placeOfOrigin.cantonAbbreviation.canton = municipalityIdentification.cantonAbbreviation.canton;
		
		return placeOfOrigin;
	}
	
	public static MunicipalityIdentification municipalityIdentification() {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		MunicipalityIdentification m = municipalityIdentifications.get((int)(Math.random() * municipalityIdentifications.size()));
		m.copyTo(municipalityIdentification);
		return municipalityIdentification;
	}
	
	public static CountryIdentification countryIdentification() {
		CountryIdentification countryIdentification = new CountryIdentification();
		CountryIdentification m = countryIdentifications.get((int)(Math.random() * countryIdentifications.size()));
		m.copyTo(countryIdentification);
		return countryIdentification;
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
			address.zip = "" + plz.postleitzahl;
			address.town = plz.ortsbezeichnung;
		} else {
			if (Math.random() < .5) {
				address.postOfficeBoxText = "POSTFACH";
				address.postOfficeBoxNumber = (int)(1000 + Math.random() * 9000);
			}
			address.zip = "" + ((int)(Math.random() * 90000 + 10000));
			address.town = NameGenerator.officialName() + "Town";
			address.country = "DE";
		}
		
		return address;
	}
	
	public static Organisation organisation() {
		Organisation organisation = new Organisation();
		organisation.identification.organisationName = OrganisationNameGenerator.getName();
		if (organisation.identification.organisationName.length() > 60) {
			organisation.identification.organisationAdditionalName = organisation.identification.organisationName.substring(60);
			organisation.identification.organisationName = organisation.identification.organisationName.substring(0, 60);
		}
		organisation.identification.uid.value = "ADM323423421";
		organisation.foundationDate = DateGenerator.generateRandomDate();
		organisation.arrivalDate = DateGenerator.generateRandomDate();
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
			Services.get(EchService.class).processOrg(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
