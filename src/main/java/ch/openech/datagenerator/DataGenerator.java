package ch.openech.datagenerator;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.properties.FlatProperties;
import org.minimalj.util.Codes;
import org.minimalj.util.mock.MockDate;
import org.minimalj.util.mock.MockPrename;
import org.minimalj.util.mock.MockName;
import org.minimalj.util.mock.MockOrganisation;

import ch.openech.model.code.NationalityStatus;
import ch.openech.model.common.Address;
import ch.openech.model.common.Canton;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.DwellingAddress;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.Place;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.person.Occupation;
import ch.openech.model.person.Person;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.model.person.Relation;
import ch.openech.model.person.types.DataLock;
import ch.openech.model.person.types.KindOfEmployment;
import ch.openech.model.person.types.MaritalStatus;
import ch.openech.model.person.types.PaperLock;
import ch.openech.model.person.types.Separation;
import ch.openech.model.person.types.TypeOfHousehold;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.model.types.Language;
import ch.openech.model.types.Sex;
import ch.openech.model.types.TypeOfResidence;
import ch.openech.util.Plz;
import ch.openech.util.PlzImport;

public class DataGenerator {

	public static Person person() {
		Person person = new Person();
		person.officialName = MockName.officialName();
		boolean male = Math.random() > .5;
		person.firstName = MockPrename.getFirstName(male);
		person.sex = male ? Sex.maennlich : Sex.weiblich;
		person.dateOfBirth.value = MockDate.generateRandomDatePartiallyKnown();
		if (Math.random() > .9) {
			person.dateOfDeath = MockDate.generateRandomDate();
		}
		person.vn.mock();
		person.callName = "Lorem Ipsum";
		person.placeOfBirth = place();
		
		person.arrivalDate = MockDate.generateRandomDate();
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
		person.sex = Sex.weiblich;
		person.dateOfBirth.value = "1999-01-02";
		person.dateOfDeath = LocalDate.of(2010, 3, 4);
		person.vn.mock();
		person.placeOfBirth = place();
		
		person.maritalStatus.maritalStatus = MaritalStatus.ledig;
		person.maritalStatus.dateOfMaritalStatus = LocalDate.of(2004, 2, 3);
		person.separation.separation = Separation.gerichtlich;
		person.separation.dateOfSeparation = LocalDate.of(2005, 5, 12);
		
		person.typeOfResidence = TypeOfResidence.hasMainResidence;
		person.arrivalDate = LocalDate.of(1999, 1, 2);
		person.departureDate = LocalDate.of(2010, 3, 4);
		person.residence.reportingMunicipality = createJona();
		
		person.placeOfOrigin.add(placeOfOrigin());
		person.dwellingAddress = dwellingAddress();
		
		person.comesFrom = place();
		person.goesTo = place();
		
		person.dataLock = DataLock.adresssperre;
		person.paperLock = PaperLock.sperre;
		person.languageOfCorrespondance = Language.fr;
		
		Occupation occupation = new Occupation();
		occupation.kindOfEmployment = KindOfEmployment.selbstaendiger;
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
		reportingMunicipality.canton = Codes.findCode(Canton.class, "SG");
		reportingMunicipality.id = 3340;
		reportingMunicipality.municipalityName = "Rapperswil-Jona";
		return reportingMunicipality;
	}
	
	public static Canton canton() {
		List<Canton> cantons = Codes.get(Canton.class);
		return cantons.get((int)(Math.random() * cantons.size()));
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
		placeOfOrigin.naturalizationDate = MockDate.generateRandomDate();
		
		List<MunicipalityIdentification> municipalities = Codes.get(MunicipalityIdentification.class);
		MunicipalityIdentification municipalityIdentification = municipalities.get((int)(Math.random() * municipalities.size()));
		placeOfOrigin.originName = municipalityIdentification.municipalityName;
		placeOfOrigin.canton = municipalityIdentification.canton;
		
		return placeOfOrigin;
	}
	
	public static MunicipalityIdentification municipalityIdentification() {
		List<MunicipalityIdentification> municipalities = Codes.get(MunicipalityIdentification.class);
		return municipalities.get((int)(Math.random() * municipalities.size()));
	}

	public static CountryIdentification countryIdentification() {
		List<CountryIdentification> countries = Codes.get(CountryIdentification.class);
		return countries.get((int)(Math.random() * countries.size()));
	}
	
	public static DwellingAddress dwellingAddress() {
		DwellingAddress dwellingAddress = new DwellingAddress();
		dwellingAddress.EGID = "" + (int)(Math.random() * 999999998 + 1);
		dwellingAddress.EWID = "" + (int)(Math.random() * 998 + 1);
		dwellingAddress.typeOfHousehold =  TypeOfHousehold.values()[(int)(Math.random() * TypeOfHousehold.values().length)];
		dwellingAddress.mailAddress = address(true, false, false);
		return dwellingAddress;
	}
	
	public static Address address(boolean swiss, boolean person, boolean organisation) {
		Address address = new Address();
		
		address.street = MockName.street();
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
			address.town = MockName.officialName() + "Town";
			address.country = "DE";
		}
		
		return address;
	}
	
	public static Organisation organisation() {
		Organisation organisation = new Organisation();
		organisation.organisationName = MockOrganisation.getName();
		if (organisation.organisationName.length() > 60) {
			organisation.organisationAdditionalName = organisation.organisationName.substring(60);
			organisation.organisationName = organisation.organisationName.substring(0, 60);
		}
		organisation.uid.value = "ADM323423421";
		organisation.foundationDate.value = MockDate.generateRandomDatePartiallyKnown();
		organisation.arrivalDate = MockDate.generateRandomDate();
		organisation.reportingMunicipality = createJona();
		organisation.businessAddress = dwellingAddress();
		
		if (Math.random() < .2) {
			organisation.comesFrom = place();
		}

		return organisation;
	}
	
}