package ch.openech.frontend.ewk;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.model.Keys;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.MockName;
import org.minimalj.util.mock.MockPrename;
import org.minimalj.util.mock.MockPrename.NameWithFrequency;

import ch.openech.frontend.e10.AddressFormElement;
import ch.openech.frontend.e11.PlaceOfOriginFormElement;
import ch.openech.frontend.e11.ResidenceFormElement;
import ch.openech.frontend.e21.NameOfParentsFormElement;
import ch.openech.frontend.e21.RelationFormElement;
import ch.openech.frontend.e44.TechnicalIdsFormElement;
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
		TechnicalIdsFormElement technicalId = new TechnicalIdsFormElement(Person.$.technicalIds, TechnicalIdsFormElement.WITH_EU_IDS, editable);
		
		line(Person.$.officialName);
		line(Person.$.firstName);
		line(Person.$.sex, Person.$.dateOfBirth, Person.$.vn, technicalId);
	}

	protected void createData() {
		// not visible for: BIRTH, CORRECT_IDENTIFICATION
		
		line(Person.$.originalName, Person.$.alliancePartnershipName, Person.$.aliasName, Person.$.otherName);
		line(Person.$.maritalStatus.maritalStatus, Person.$.maritalStatus.dateOfMaritalStatus, Person.$.cancelationReason, Person.$.callName);

		if (echSchema.separationTillAvailable()) {
			line(Person.$.separation.separation, Person.$.separation.dateOfSeparation, Person.$.separation.separationTill, Person.$.languageOfCorrespondance);
		} else {
			line(Person.$.separation.separation, Person.$.separation.dateOfSeparation, Person.$.languageOfCorrespondance);
		}

		line(Person.$.nationality, Person.$.religion);
		if (!mode.isMoveIn()) {
			line(Person.$.placeOfBirth, Person.$.dateOfDeath);
		}
		
		//

		RelationFormElement relationField = new RelationFormElement(Person.$.relation, echSchema, editable);
		NameOfParentsFormElement nameOfParentsField = new NameOfParentsFormElement(Person.$.nameOfParents, editable);

		switch (mode) {
		case DISPLAY:
		case BASE_DELIVERY:
			line(Person.$.typeOfResidence, Person.$.residence);
			addDependecy(Person.$.typeOfResidence, new ResidenceUpdater(), Person.$.residence);
			
			line(Person.$.arrivalDate, Person.$.departureDate);
			line(Person.$.comesFrom, Person.$.goesTo);
			line(Person.$.comesFromAddress, Person.$.dwellingAddress, Person.$.goesToAddress);
			
			if (extensionAvailable()) {
				line(Person.$.placeOfOrigin, Person.$.foreign, Person.$.occupation, Person.$.personExtendedInformation);
				line(relationField, nameOfParentsField, Person.$.contactPerson, Person.$.contacts);
			} else {
				line(Person.$.placeOfOrigin, Person.$.foreign, Person.$.occupation);
				line(relationField, nameOfParentsField, Person.$.contactPerson);
			}
			
			line(Person.$.dataLock, Person.$.paperLock);
			break;
		case MOVE_IN:
			line(Person.$.placeOfBirth, Person.$.arrivalDate);
			line(Person.$.typeOfResidence, Person.$.comesFrom);
			line(Person.$.residence, Person.$.dwellingAddress, Person.$.comesFromAddress);
			
			if (extensionAvailable()) {
				line(Person.$.placeOfOrigin, Person.$.foreign, Person.$.occupation, Person.$.personExtendedInformation);
				line(relationField, nameOfParentsField, Person.$.contactPerson, Person.$.contacts);
			} else {
				line(Person.$.placeOfOrigin, Person.$.foreign, Person.$.occupation);
				line(relationField, nameOfParentsField, Person.$.contactPerson);
			}
			break;
		case CHANGE_RESIDENCE_TYPE:
			line(Person.$.arrivalDate, Person.$.departureDate);
			line(Person.$.comesFrom, Person.$.goesTo);
			line(Person.$.comesFromAddress, Person.$.goesToAddress);
			line(Person.$.placeOfOrigin, Person.$.foreign);
			line(Person.$.dwellingAddress, Person.$.occupation);
			line(new RelationFormElement(Person.$.relation, echSchema, editable), new NameOfParentsFormElement(Person.$.nameOfParents, true));
			break;
		case CORRECT_PERSON:
			line(Person.$.placeOfOrigin, Person.$.foreign);
			if (extensionAvailable()) {
				line(Person.$.contactPerson, Person.$.personExtendedInformation, Person.$.contacts);
			} else {
				line(Person.$.contactPerson);
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
		line(Person.$.callName, Person.$.languageOfCorrespondance);
		line(Person.$.nationality, Person.$.religion);
		line(Person.$.placeOfBirth);
		ParentFormElement mother = new ParentFormElement(Person.$.getMother());
		ParentFormElement father = new ParentFormElement(Person.$.getFather());
		line(mother, father);
		line(new PlaceOfOriginFormElement(Person.$.placeOfOrigin, false, editable), Person.$.foreign);
		
		addDependecy(Person.$.getMother(), new OfficialNameFromMotherUpdater(), Person.$.officialName);
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
		NameWithFrequency generatedName = MockPrename.getName(male);
		person.firstName = generatedName.name;
		person.sex = male ? Sex.maennlich : Sex.weiblich;
		person.callName = "Lorem Ipsum";
		person.officialName = MockName.officialName();
		
		ResidenceFormElement.fillWithMockupData(person.residence, person.typeOfResidence);
	}

	private class ParentFormElement extends ObjectPanelFormElement<Relation> {
		
		public ParentFormElement(Relation key) {
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
			form.line(Relation.$.partner);
			form.line(new AddressFormElement(Relation.$.address, true));
			return form;
		}
	}

}
