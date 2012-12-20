package ch.openech.client.ewk;

import static ch.openech.dm.person.Person.PERSON;
import static ch.openech.dm.person.PersonIdentification.PERSON_IDENTIFICATION;
import ch.openech.client.e10.AddressField;
import ch.openech.client.e11.PlaceOfOriginField;
import ch.openech.client.e11.PlaceReadOnlyField;
import ch.openech.client.e21.PartnerField;
import ch.openech.client.e21.RelationField;
import ch.openech.client.e44.TechnicalIdsField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonEditMode;
import ch.openech.dm.person.types.MaritalStatus;
import ch.openech.dm.person.types.PartnerShipAbolition;
import ch.openech.dm.types.Sex;
import ch.openech.mj.autofill.FirstNameGenerator;
import ch.openech.mj.autofill.NameWithFrequency;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.EnumEditField;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.xml.write.EchSchema;

public class PersonPanel extends EchFormPanel<Person>  {

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
			createIdentification();
		}

		if (mode == PersonEditMode.BIRTH) {
			createBirth();
		} else if (mode != PersonEditMode.CORRECT_IDENTIFICATION) {
			createData();
		}
	}

	public void createIdentification() {
		TechnicalIdsField technicalId = new TechnicalIdsField(PERSON.personIdentification.technicalIds, TechnicalIdsField.WITH_EU_IDS, editable);
		
		line(PERSON.personIdentification.officialName);
		line(PERSON.personIdentification.firstName);
		line(PERSON.personIdentification.sex, PERSON.personIdentification.dateOfBirth, PERSON.personIdentification.vn, technicalId);
	}

	protected void createData() {
		
		// ReportedPerson (ech0011)
		line(PERSON.originalName, PERSON.alliancePartnershipName, PERSON.aliasName, PERSON.otherName);
		
		if (editable) {
			line(PERSON.maritalStatus.maritalStatus,
					new DateOfMaritalStatusField(),
					new CancelationReasonField(), PERSON.callName);
		} else {
			line(PERSON.maritalStatus.maritalStatus,
					PERSON.maritalStatus.dateOfMaritalStatus,
					PERSON.cancelationReason, PERSON.callName);
		}

		if (echSchema.separationTillAvailable()) {
			if (!mode.isCorrectName()) {
				line(PERSON.separation.separation, PERSON.separation.dateOfSeparation,
						PERSON.separation.separationTill, PERSON.languageOfCorrespondance);
			} else {
				line(PERSON.separation.separation, PERSON.separation.dateOfSeparation,
						PERSON.separation.separationTill, PERSON.foreign.nameOnPassport);
			}
		} else {
			if (!mode.isCorrectName()) {
				line(PERSON.separation.separation, PERSON.separation.dateOfSeparation,
						PERSON.languageOfCorrespondance);
			} else {
				line(PERSON.separation.separation, PERSON.separation.dateOfSeparation,
						PERSON.foreign.nameOnPassport);
			}
		}

		line(PERSON.nationality, PERSON.religion);

		//
		
		if (mode.isMoveIn()) {
			line(PERSON.placeOfBirth, PERSON.arrivalDate);
			line(PERSON.typeOfResidence, PERSON.comesFrom);
			area(PERSON.residence, PERSON.comesFromAddress);
		} else {
			line(PERSON.placeOfBirth, PERSON.dateOfDeath);
		
			if (mode != PersonEditMode.CORRECT_PERSON && mode != PersonEditMode.CHANGE_RESIDENCE_TYPE) {
				area(PERSON.typeOfResidence, PERSON.residence);
			}

			if (mode != PersonEditMode.CORRECT_PERSON) {
				line(PERSON.arrivalDate, PERSON.departureDate);
				line(PERSON.comesFrom, PERSON.goesTo);
				area(PERSON.comesFromAddress, PERSON.goesToAddress);
			}
		}		
		
		if (mode == PersonEditMode.BASE_DELIVERY || mode == PersonEditMode.DISPLAY) {
			line(PERSON.dataLock, PERSON.paperLock);
		}
		
		createAreas();
	}
	
	private class DateOfMaritalStatusField extends DateField implements DependingOnFieldAbove<MaritalStatus> {
		
		public DateOfMaritalStatusField() {
			super(Constants.getProperty(PERSON.maritalStatus.dateOfMaritalStatus));
		}

		@Override
		public MaritalStatus getKeyOfDependedField() {
			return PERSON.maritalStatus.maritalStatus;
		}

		@Override
		public void valueChanged(MaritalStatus maritalStatus) {
			setEnabled(maritalStatus != MaritalStatus.ledig);
		}
	}
	
	private class CancelationReasonField extends EnumEditField<PartnerShipAbolition> implements DependingOnFieldAbove<MaritalStatus> {
		
		public CancelationReasonField() {
			super(Constants.getProperty(PERSON.cancelationReason));
		}

		@Override
		public MaritalStatus getKeyOfDependedField() {
			return PERSON.maritalStatus.maritalStatus;
		}

		@Override
		public void valueChanged(MaritalStatus maritalStatus) {	
			setEnabled(maritalStatus == MaritalStatus.aufgeloeste_partnerschaft || maritalStatus == MaritalStatus.geschieden);

		}
	}
	
	private void createAreas() {
		boolean placeOfOriginWithAdd = mode != PersonEditMode.BIRTH;
		
		switch (mode) {
		case CHANGE_RESIDENCE_TYPE:
			area(new PlaceOfOriginField(PERSON.placeOfOrigin, placeOfOriginWithAdd, editable), PERSON.foreign);
			area(PERSON.dwellingAddress);
			area(new RelationField(PERSON.relation, echSchema, true, editable));
			area(PERSON.occupation);
			break;
		case CORRECT_PERSON:
			area(new PlaceOfOriginField(PERSON.placeOfOrigin, placeOfOriginWithAdd, editable), PERSON.foreign);
			if (extensionAvailable()) {
				area(PERSON.contactPerson, PERSON.personExtendedInformation, PERSON.contact);
			} else {
				area(PERSON.contactPerson);
			}
			break;
		case BIRTH:
			PartnerField mother = new PartnerField(PERSON.getMother(), echSchema);
			PartnerField father = new PartnerField(PERSON.getFather(), echSchema);
			// Das ist zur Zeit nicht mehr möglich, das Mutter - Feld müsste oberhalb des Namensfeld sein
			// mother.setConnectedNameField((TextField) getField(PERSON_IDENTIFICATION.officialName));
			area(mother, father);
			area(new PlaceOfOriginField(PERSON.placeOfOrigin, placeOfOriginWithAdd, editable), PERSON.foreign);
			break;
		default:
			area(new PlaceOfOriginField(PERSON.placeOfOrigin, placeOfOriginWithAdd, editable), PERSON.foreign, PERSON.dwellingAddress, PERSON.contactPerson);
			RelationField relationField = new RelationField(PERSON.relation, echSchema, true, editable);
			if (extensionAvailable()) {
				area(PERSON.occupation, relationField, PERSON.personExtendedInformation, PERSON.contact);
			} else {
				area(PERSON.occupation, relationField);
			}
		}
	}

	protected void createBirth() {
		line(PERSON.callName, PERSON.languageOfCorrespondance);
		line(PERSON.nationality, PERSON.religion);
		line(PERSON.placeOfBirth);
		createAreas();
	}

	// getter / setter

	@Override
	public void setObject(Person person) {
		person.editMode = mode;
		super.setObject(person);
		disableDeathFieldIfAlive(person);
		disableMoveOutFields(person);
	}

	private void disableDeathFieldIfAlive(Person person) {
		if (mode == PersonEditMode.DISPLAY) {
			((DateField) getField(PERSON.dateOfDeath)).setEnabled(person.dateOfDeath != null);
		}
	}

	private void disableMoveOutFields(Person person) {
		if (mode == PersonEditMode.DISPLAY) {
			((DateField) getField(PERSON.departureDate)).setEnabled(person.departureDate != null);
			((PlaceReadOnlyField) getField(PERSON.goesTo)).setEnabled(person.goesTo != null && !person.goesTo.isUnknown());
			((AddressField) getField(PERSON.goesToAddress)).setEnabled(person.goesTo != null && person.goesTo.mailAddress != null && !person.goesTo.mailAddress.isEmpty());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void fillWithDemoData() {
		super.fillWithDemoData();

		boolean male = Math.random() < .5;
		NameWithFrequency generatedName = FirstNameGenerator.getName(male);
		((FormField<String>) getField(PERSON.personIdentification.firstName)).setObject(generatedName.name);
		EnumEditField<Sex> sexField = (EnumEditField<Sex>) getField(PERSON_IDENTIFICATION.sex);
		if (sexField != null) sexField.setObject(male ? Sex.maennlich : Sex.weiblich);
	}
	
//	public static void main(String... args) {
//		FrameManager.setSystemLookAndFeel();
//		Resources.addResourceBundle(ResourceBundle.getBundle("ch.openech.resources.OpenEch"));
//		ClientToolkit.setToolkit(new SwingClientToolkit());
//
//		PersonPanel personPanel = new PersonPanel(PersonEditMode.DISPLAY, null);
//		personPanel.setObject(new Person());
//		
//		JFrame frame = new JFrame("Test");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setLayout(new BorderLayout());
//		frame.add((Component) personPanel.getComponent(), BorderLayout.CENTER);
//		frame.pack();
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//	}

}
