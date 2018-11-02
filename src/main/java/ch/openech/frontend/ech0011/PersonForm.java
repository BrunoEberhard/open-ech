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

		addTitle("Religion");
		line($.religionData.religion, $.religionData.religionValidFrom);

		addTitle("Zivilstand");
		line($.maritalData.maritalStatus, $.maritalData.dateOfMaritalStatus, $.maritalData.cancelationReason, $.maritalData.officialProofOfMaritalStatusYesNo);
		line($.maritalData.separationData.separation, $.maritalData.separationData.separationValidFrom, $.maritalData.separationData.separationValidTill, new EmptyFormElement());

		addTitle("Nationalität");
		line($.nationalityData.nationalityStatus, new CountryInfoFormElement($.nationalityData.countryInfo));
		addDependecy($.nationalityData.nationalityStatus, new NationalityUpdater(), $.nationalityData.countryInfo);

		addTitle("Kontakt");
		// line(new PersonIdentificationFormElement($.contactData.personIdentification,
		// $.contactData.personIdentificationPartner,
		// $.contactData.partnerIdOrganisation));
		// line($.contactData.contactAddress);
		// line($.contactData.contactValidFrom, $.contactData.contactValidTill);

		addTitle("Aufenthaltsbewilligung");
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
