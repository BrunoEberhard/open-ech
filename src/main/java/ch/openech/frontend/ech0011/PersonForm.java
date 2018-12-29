package ch.openech.frontend.ech0011;

import static ch.ech.ech0011.Person.$;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.form.element.EmptyFormElement;

import ch.ech.ech0011.NationalityData.CountryInfo;
import ch.ech.ech0011.NationalityStatus;
import ch.ech.ech0011.Person;
import ch.openech.frontend.ech0008.CountryInfoFormElement;
import ch.openech.frontend.form.EchForm;

public class PersonForm extends EchForm<Person> {

	public PersonForm(boolean editable) {
		super(editable, 4);
		
		addTitle("Name");
		line($.personAdditionalData.mrMrs, $.personAdditionalData.title);
		line($.nameData.officialName, $.nameData.firstName);
		line($.nameData.originalName, $.nameData.allianceName, $.nameData.allianceName, $.nameData.aliasName);
		line($.nameData.otherName, $.nameData.callName);
		line($.nameData.nameOnForeignPassport, $.nameData.declaredForeignName);

		addTitle("Geburtsdaten");
		line($.birthData.dateOfBirth, $.birthData.sex, $.birthData.placeOfBirth);
		line($.birthAddonData.nameOfFather, $.birthAddonData.nameOfMother); // TODO only for some events

		addTitle("Weitere Angaben");
		line($.religionData.religion, $.religionData.religionValidFrom);

		line(new MaritalDataFormElement($.maritalData, editable));
		line($.maritalData.separationData.separation, $.maritalData.separationData.separationValidFrom, $.maritalData.separationData.separationValidTill, new EmptyFormElement());

		line($.nationalityData.nationalityStatus, new CountryInfoFormElement($.nationalityData.countryInfo));
		addDependecy($.nationalityData.nationalityStatus, new NationalityUpdater(), $.nationalityData.countryInfo);

		line(new PlaceOfOriginAddonFormElement($.placeOfOrigin, true), new ContactDataFormElement($.contactData, true));

		line($.residencePermit.residencePermit, $.residencePermit.residencePermitValidFrom, $.residencePermit.residencePermitValidTill,
				$.residencePermit.entryDate);

	}
	
	private class NationalityUpdater implements PropertyUpdater<NationalityStatus, List<CountryInfo>, Person> {

		@Override
		public List<CountryInfo> update(NationalityStatus input, Person copyOfEditObject) {
			if (input == NationalityStatus._0 || input == NationalityStatus._1) {
				return new ArrayList<>();
			} else {
				return copyOfEditObject.nationalityData.countryInfo;
			}
		}
		
	}
	
}
