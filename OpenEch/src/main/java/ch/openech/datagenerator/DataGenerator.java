package ch.openech.datagenerator;

import java.lang.reflect.Field;
import java.util.List;

import javax.swing.SwingUtilities;

import ch.openech.client.e44.VnField;
import ch.openech.client.ewk.event.ThreadSafeProgressMonitor;
import ch.openech.dm.code.MaritalStatus;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.common.Plz;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.mj.autofill.FirstNameGenerator;
import ch.openech.mj.autofill.NameGenerator;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.mj.edit.value.PropertyAccessor;
import ch.openech.mj.page.RefreshablePage;
import ch.openech.server.EchServer;
import ch.openech.util.PlzImport;
import ch.openech.xml.read.StaxEch0071;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class DataGenerator {

	private static List<MunicipalityIdentification> municipalityIdentifications = StaxEch0071.getInstance().getMunicipalityIdentifications();

	public static void generateData(final EchNamespaceContext echNamespaceContext, final int parseInt, final RefreshablePage refreshable) {
		final ThreadSafeProgressMonitor progressMonitor = new ThreadSafeProgressMonitor(null, "Generiere Testdaten", "Initialisierung", 0, parseInt);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				WriterEch0020 writerEch0020 = new WriterEch0020(echNamespaceContext);
				for (int i = 0; i<parseInt; i++) {
					if (progressMonitor.isCanceled()) break;
					progressMonitor.invokeSetNote("Generiere Person " + i);
					progressMonitor.invokeSetProgress(i);
					generatePerson(writerEch0020, progressMonitor);
				}
				progressMonitor.close();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						refreshable.refresh();
					}
				});
			}
		}).start();
	}
	
	public static void generatePerson(WriterEch0020 writerEch0020, ThreadSafeProgressMonitor progressMonitor) {
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
			List<Plz> plzList = PlzImport.getInstance().getZipCodes();
			int pos = (int) (Math.random() * plzList.size());
			Plz plz = plzList.get(pos);
			address.countryZipTown.setPlz(plz);
		} else {
			if (Math.random() < .5) {
				address.postOfficeBoxText = "POSTFACH";
				address.postOfficeBoxNumber = "" + (int)(1000 + Math.random() * 9000);
			}
			address.countryZipTown.foreignZipCode = "" + ((int)(Math.random() * 90000 + 10000));
			address.countryZipTown.town = NameGenerator.officialName() + "Town";
			address.countryZipTown.country = "DE";
		}
		
		return address;
	}
	
}
