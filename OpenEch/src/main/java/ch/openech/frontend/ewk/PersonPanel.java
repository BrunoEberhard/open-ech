package ch.openech.frontend.ewk;

import static ch.openech.model.person.Person.*;
import static ch.openech.model.person.Relation.*;

import org.minimalj.autofill.FirstNameGenerator;
import org.minimalj.autofill.NameGenerator;
import org.minimalj.autofill.NameWithFrequency;
import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.Keys;
import org.minimalj.util.StringUtils;

import ch.openech.frontend.e10.AddressField;
import ch.openech.frontend.e11.PlaceOfOriginField;
import ch.openech.frontend.e11.ResidenceField;
import ch.openech.frontend.e21.NameOfParentsField;
import ch.openech.frontend.e21.RelationField;
import ch.openech.frontend.e44.TechnicalIdsField;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.model.person.Relation;
import ch.openech.model.person.Residence;
import ch.openech.model.types.Sex;
import ch.openech.model.types.TypeOfResidence;
import ch.openech.xml.write.EchSchema;

public class PersonPanel extends EchForm<Person>  {

	private final PersonEditMode mode;
	
	public PersonPanel(PersonEditMode mode, EchSchema echSchema) {
		super(echSchema, PersonEditMode.DISPLAY != mode, 4);
		this.mode = mode;
		createContent();
	}

	private boolean extensionAvailable() {
		// TODO remove == null test
		return echSchema == null || echSchema.extensionAvailable();
	}
	
	private void createContent() {
		if (mode.isIdentificationVisible()) {
			// not visible for: CHANGE_RESIDENCE_TYPE, CORRECT_NAME
			createIdentification();
		}

		if (mode == PersonEditMode.BIRTH) {
			createBirth();
		} else if (mode != PersonEditMode.CORRECT_IDENTIFICATION) {
			createData();
		}
	}

	public void createIdentification() {
		TechnicalIdsField technicalId = new TechnicalIdsField(PERSON.technicalIds, TechnicalIdsField.WITH_EU_IDS, editable);
		
		line(PERSON.officialName);
		line(PERSON.firstName);
		line(PERSON.sex, PERSON.dateOfBirth, PERSON.vn, technicalId);
	}

	protected void createData() {
		// not visible for: BIRTH, CORRECT_IDENTIFICATION
		
		line(PERSON.originalName, PERSON.alliancePartnershipName, PERSON.aliasName, PERSON.otherName);
		line(PERSON.maritalStatus.maritalStatus, PERSON.maritalStatus.dateOfMaritalStatus, PERSON.cancelationReason, PERSON.callName);

		if (echSchema.separationTillAvailable()) {
			line(PERSON.separation.separation, PERSON.separation.dateOfSeparation, PERSON.separation.separationTill, PERSON.languageOfCorrespondance);
		} else {
			line(PERSON.separation.separation, PERSON.separation.dateOfSeparation, PERSON.languageOfCorrespondance);
		}

		line(PERSON.nationality, PERSON.religion);
		if (!mode.isMoveIn()) {
			line(PERSON.placeOfBirth, PERSON.dateOfDeath);
		}
		
		//

		RelationField relationField = new RelationField(PERSON.relation, echSchema, editable);
		NameOfParentsField nameOfParentsField = new NameOfParentsField(Person.PERSON.nameOfParents, editable);

		switch (mode) {
		case DISPLAY:
		case BASE_DELIVERY:
			line(PERSON.typeOfResidence, PERSON.residence);
			addDependecy(PERSON.typeOfResidence, new ResidenceUpdater(), PERSON.residence);
			
			line(PERSON.arrivalDate, PERSON.departureDate);
			line(PERSON.comesFrom, PERSON.goesTo);
			line(PERSON.comesFromAddress, PERSON.dwellingAddress, PERSON.goesToAddress);
			
			if (extensionAvailable()) {
				line(PERSON.placeOfOrigin, PERSON.foreign, PERSON.occupation, PERSON.personExtendedInformation);
				line(relationField, nameOfParentsField, PERSON.contactPerson, PERSON.contacts);
			} else {
				line(PERSON.placeOfOrigin, PERSON.foreign, PERSON.occupation);
				line(relationField, nameOfParentsField, PERSON.contactPerson);
			}
			
			line(PERSON.dataLock, PERSON.paperLock);
			break;
		case MOVE_IN:
			line(PERSON.placeOfBirth, PERSON.arrivalDate);
			line(PERSON.typeOfResidence, PERSON.comesFrom);
			line(PERSON.residence, PERSON.dwellingAddress, PERSON.comesFromAddress);
			
			if (extensionAvailable()) {
				line(PERSON.placeOfOrigin, PERSON.foreign, PERSON.occupation, PERSON.personExtendedInformation);
				line(relationField, nameOfParentsField, PERSON.contactPerson, PERSON.contacts);
			} else {
				line(PERSON.placeOfOrigin, PERSON.foreign, PERSON.occupation);
				line(relationField, nameOfParentsField, PERSON.contactPerson);
			}
			break;
		case CHANGE_RESIDENCE_TYPE:
			line(PERSON.arrivalDate, PERSON.departureDate);
			line(PERSON.comesFrom, PERSON.goesTo);
			line(PERSON.comesFromAddress, PERSON.goesToAddress);
			line(PERSON.placeOfOrigin, PERSON.foreign);
			line(PERSON.dwellingAddress, PERSON.occupation);
			line(new RelationField(PERSON.relation, echSchema, editable), new NameOfParentsField(Person.PERSON.nameOfParents, true));
			break;
		case CORRECT_PERSON:
			line(PERSON.placeOfOrigin, PERSON.foreign);
			if (extensionAvailable()) {
				line(PERSON.contactPerson, PERSON.personExtendedInformation, PERSON.contacts);
			} else {
				line(PERSON.contactPerson);
			}
			break;
		case BIRTH:
		case CORRECT_IDENTIFICATION:
			// both not implemented here
		}
	}

	public static class ResidenceUpdater implements Form.PropertyUpdater<TypeOfResidence, Residence, Person> {
		@Override
		public Residence update(TypeOfResidence typeOfResidence, Person person) {
			if (typeOfResidence == TypeOfResidence.hasOtherResidence) {
				person.residence.reportingMunicipality = null;
			}
			return person.residence;
		}
	}
	
	protected void createBirth() {
		line(PERSON.callName, PERSON.languageOfCorrespondance);
		line(PERSON.nationality, PERSON.religion);
		line(PERSON.placeOfBirth);
		ParentField mother = new ParentField(PERSON.getMother());
		ParentField father = new ParentField(PERSON.getFather());
		line(mother, father);
		line(new PlaceOfOriginField(PERSON.placeOfOrigin, false, editable), PERSON.foreign);
		
		addDependecy(PERSON.getMother(), new OfficialNameFromMotherUpdater(), PERSON.officialName);
	}

	private class OfficialNameFromMotherUpdater implements Form.PropertyUpdater<Relation, String, Person> {
		@Override
		public String update(Relation input, Person person) {
			String actualValue = person.officialName;
			if (input != null && StringUtils.isEmpty(actualValue)) {
				if (input.partner != null) {
					return (String) input.partner.officialName;
				} else if (input.address != null) {
					return input.address.lastName;
				}
			}
			return actualValue;
		}
	}
	
	// getter / setter

	@Override
	public void setObject(Person person) {
		person.editMode = mode;
		super.setObject(person);
	}
	
	@Override
	protected void fillWithDemoData(Person person) {
		super.fillWithDemoData(person);
		
		boolean male = Math.random() < .5;
		NameWithFrequency generatedName = FirstNameGenerator.getName(male);
		person.firstName = generatedName.name;
		person.sex = male ? Sex.maennlich : Sex.weiblich;
		person.callName = "Lorem Ipsum";
		person.officialName = NameGenerator.officialName();
		
		ResidenceField.fillWithMockupData(person.residence, person.typeOfResidence);
	}

	private class ParentField extends ObjectFlowField<Relation> {
		
		public ParentField(Relation key) {
			super(Keys.getProperty(key));
		}

		public class PartnerEditor extends ObjectFieldEditor {
			// Benötigt, damit dir richige Resource verwendet wird
		}
		
		@Override
		protected void show(Relation relation) {
			addText(relation.identificationToHtml());
		}

		@Override
		protected void showActions() {
			addAction(new PartnerEditor());
		}
		
		@Override
		public Form<Relation> createFormPanel() {
			EchForm<Relation> form = new EchForm<>();
			form.line(RELATION.partner);
			form.line(new AddressField(RELATION.address, true));
			return form;
		}
	}

}
