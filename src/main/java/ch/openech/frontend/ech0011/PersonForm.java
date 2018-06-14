package ch.openech.frontend.ech0011;

import static ch.ech.ech0011.v8.Person.$;

import ch.ech.ech0011.v8.Person;
import ch.openech.frontend.form.EchForm;

public class PersonForm extends EchForm<Person> {

	public PersonForm(boolean editable) {
		super(editable, 4);
		
		addTitle("Name");
		line($.nameData.officialName, $.nameData.firstName);
		line($.nameData.originalName, $.nameData.allianceName, $.nameData.allianceName, $.nameData.aliasName);
		line($.nameData.otherName, $.nameData.callName);
		line($.nameData.nameOnForeignPassport, $.nameData.declaredForeignName);

		addTitle("Geburtsdaten");
		line($.birthData.dateOfBirth, $.birthData.sex, $.birthData.placeOfBirth);
		line($.birthAddonData.nameOfFather, $.birthAddonData.nameOfMother); // TODO only for some events

		addTitle("Religion");
		line($.religionData.religion, $.religionData.religionValidFrom);

	}
}
