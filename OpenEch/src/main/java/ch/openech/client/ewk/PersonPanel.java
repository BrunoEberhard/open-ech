package ch.openech.client.ewk;

import static ch.openech.dm.person.Person.PERSON;
import static ch.openech.dm.person.PersonIdentification.PERSON_IDENTIFICATION;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import ch.openech.client.e10.AddressTextField;
import ch.openech.client.e11.PlaceOfOriginField;
import ch.openech.client.e11.PlaceReadOnlyField;
import ch.openech.client.e11.SeparationField;
import ch.openech.client.e21.PartnerField;
import ch.openech.client.e21.RelationField;
import ch.openech.client.e44.TechnicalIdsField;
import ch.openech.client.e44.VnField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.EchFormats;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.code.MaritalStatus;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.mj.autofill.FirstNameGenerator;
import ch.openech.mj.autofill.NameWithFrequency;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.CodeEditField;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.edit.fields.TextEditField;
import ch.openech.mj.edit.fields.TextFormField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.swing.FrameManager;
import ch.openech.mj.swing.toolkit.SwingClientToolkit;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;

public class PersonPanel extends EchFormPanel<Person>  {

	public enum PersonPanelType {
		DISPLAY, BASE_DELIVERY, MOVE_IN, BIRTH, CHANGE_RESIDENCE_TYPE, CORRECT_PERSON, CORRECT_IDENTIFICATION, CORRECT_NAME
	};

	private final PersonPanelType type;
	
	public PersonPanel(PersonPanelType type, EchNamespaceContext context) {
		super(context, PersonPanelType.DISPLAY != type, 4);
		this.type = type;
		createContent();
	}

	private boolean extensionAvailable() {
		// TODO remove == null test
		return getNamespaceContext() == null || getNamespaceContext().extensionAvailable();
	}
	
	private void createContent() {
		boolean identificationVisible = type != PersonPanelType.CHANGE_RESIDENCE_TYPE && //
			type != PersonPanelType.CORRECT_NAME;
		
		if (identificationVisible) {
			createIdentification();
		}

		if (type == PersonPanelType.BIRTH) {
			createBirth();
		} else if (type != PersonPanelType.CORRECT_IDENTIFICATION) {
			createData();
		}
	}

	public void createIdentification() {
		String prefix = Constants.getConstant(PERSON.personIdentification) + ".";
		TechnicalIdsField technicalId = new TechnicalIdsField(prefix + Constants.getConstant(PERSON_IDENTIFICATION.technicalIds), TechnicalIdsField.WITH_EU_IDS, editable);
		VnField vn = new VnField(prefix + PERSON_IDENTIFICATION.vn, editable);
		
		line(prefix + PERSON_IDENTIFICATION.officialName);
		line(prefix + PERSON_IDENTIFICATION.firstName);
		line(prefix + PERSON_IDENTIFICATION.sex, prefix + PERSON_IDENTIFICATION.dateOfBirth, vn, technicalId);
		
		setRequired(prefix + PERSON_IDENTIFICATION.vn);
	}

	protected void createData() {
		boolean moveIn = type == PersonPanelType.MOVE_IN || type == PersonPanelType.CHANGE_RESIDENCE_TYPE;
		boolean correctName = type == PersonPanelType.CORRECT_NAME;
		boolean editable = type != PersonPanelType.DISPLAY;
		
		// ReportedPerson (ech0011)
		line(PERSON.originalName, PERSON.alliancePartnershipName, PERSON.aliasName, PERSON.otherName);
		if (!correctName) {
			line(PERSON.callName, PERSON.languageOfCorrespondance);
		} else {
			line(PERSON.callName, PERSON.foreign.nameOnPassport);
			return;
		}
		
		if (editable) {
			line(PERSON.maritalStatus.maritalStatus, //
					new DateOfMaritalStatusField(), new SeparationField(PERSON.separation, getNamespaceContext(), true), new CancelationReasonField());
		} else {
			line(PERSON.maritalStatus.maritalStatus, PERSON.maritalStatus.dateOfMaritalStatus, //
					new SeparationField(PERSON.separation, getNamespaceContext(), false), PERSON.cancelationReason);
		}
		
		line(PERSON.nationality, PERSON.religion);

		//
		
		if (moveIn) {
			line(PERSON.placeOfBirth, PERSON.arrivalDate);
			line(PERSON.typeOfResidence, PERSON.comesFrom);
			line(PERSON.residence, PERSON.comesFromAddress);
			
			setRequired(PERSON.arrivalDate);
		} else {
			line(PERSON.placeOfBirth, PERSON.dateOfDeath);
		
			if (type != PersonPanelType.CORRECT_PERSON && type != PersonPanelType.CHANGE_RESIDENCE_TYPE) {
				line(PERSON.typeOfResidence, PERSON.residence);
			}

			if (type != PersonPanelType.CORRECT_PERSON) {
				line(PERSON.arrivalDate, PERSON.departureDate);
				line(PERSON.comesFrom, PERSON.goesTo);
				line(PERSON.comesFromAddress, PERSON.goesToAddress);

				if (type != PersonPanelType.DISPLAY) {
					if (editable) setRequired(PERSON.arrivalDate);
				}
			}
		}		
		
		if (type == PersonPanelType.BASE_DELIVERY || type == PersonPanelType.DISPLAY) {
			line(PERSON.dataLock, PERSON.paperLock);
		}
		
		createAreas();
	}
	
	private class DateOfMaritalStatusField extends DateField implements DependingOnFieldAbove<String> {
		
		public DateOfMaritalStatusField() {
			super(PERSON.maritalStatus.dateOfMaritalStatus);
		}

		@Override
		public String getNameOfDependedField() {
			return Constants.getConstant(PERSON.maritalStatus.maritalStatus);
		}

		@Override
		public void setDependedField(EditField<String> dependedField) {
			setEnabled(!StringUtils.equals(MaritalStatus.Ledig.value, dependedField.getObject()));
		}
	}
	
	private class CancelationReasonField extends CodeEditField implements DependingOnFieldAbove<String> {
		
		public CancelationReasonField() {
			super(PERSON.cancelationReason, EchCodes.partnerShipAbolition);
		}

		@Override
		public String getNameOfDependedField() {
			return Constants.getConstant(PERSON.maritalStatus.maritalStatus);
		}

		@Override
		public void setDependedField(EditField<String> dependedField) {
			setEnabled(StringUtils.equals(dependedField.getObject(), MaritalStatus.AufgeloestePartnerschaft.value, MaritalStatus.Geschieden.value));
		}
	}
	
	private void createAreas() {
		boolean placeOfOriginWithAdd = type != PersonPanelType.BIRTH;
		
		switch (type) {
		case CHANGE_RESIDENCE_TYPE:
			area(new PlaceOfOriginField(PERSON.placeOfOrigin, placeOfOriginWithAdd, editable), PERSON.foreign);
			area(PERSON.dwellingAddress);
			area(new RelationField(PERSON.relation, getNamespaceContext(), true, editable));
			area(PERSON.occupation);
			break;
		case CORRECT_PERSON:
			area(new PlaceOfOriginField(PERSON.placeOfOrigin, placeOfOriginWithAdd, editable), PERSON.foreign);
			area(PERSON.contactPerson);
			if (extensionAvailable()) {
				area(PERSON.personExtendedInformation);
				area(PERSON.contact);
			}
			break;
		case BIRTH:
			area(new PlaceOfOriginField(PERSON.placeOfOrigin, placeOfOriginWithAdd, editable), PERSON.foreign);
			PartnerField mother = new PartnerField("mother", getNamespaceContext());
			PartnerField father = new PartnerField("father", getNamespaceContext());
			// Das ist zur Zeit nicht mehr möglich, das Mutter - Feld müsste oberhalb des Namensfeld sein
			// mother.setConnectedNameField((TextField) getField(PERSON_IDENTIFICATION.officialName));
			area(mother);
			area(father);
			break;
		default:
			area(new PlaceOfOriginField(PERSON.placeOfOrigin, placeOfOriginWithAdd, editable), PERSON.foreign, PERSON.dwellingAddress, PERSON.contactPerson);
			RelationField relationField = new RelationField(PERSON.relation, getNamespaceContext(), true, editable);
			if (extensionAvailable()) {
				area(PERSON.occupation, relationField, PERSON.personExtendedInformation, PERSON.contact);
			} else {
				area(PERSON.occupation, relationField);
			}
		}
	}

	// TODO Mutter-Feld bei Geburt weiter oben, damit Depending realisiert werden kann
	private static class MotherField extends TextEditField implements DependingOnFieldAbove<Relation> {
		
		public MotherField() {
			super(PERSON_IDENTIFICATION.officialName);
		}

		@Override
		public String getNameOfDependedField() {
			return "mother";
		}

		@Override
		public void setDependedField(EditField<Relation> field) {
		}
	}
	
	protected void createBirth() {
		line(PERSON.callName, PERSON.languageOfCorrespondance);
		line(PERSON.nationality, PERSON.religion);
		line(PERSON.placeOfBirth); // TODO: halfLine !!
		createAreas();
	}

	// getter / setter

	@Override
	public void setObject(Person person) {
		super.setObject(person);
		disableDeathFieldIfAlive(person);
		disableMoveOutFields(person);
	}

	private void disableDeathFieldIfAlive(Person person) {
		if (type == PersonPanelType.DISPLAY) {
			((TextFormField) getField(PERSON.dateOfDeath)).setEnabled(!StringUtils.isBlank(person.dateOfDeath));
		}
	}

	private void disableMoveOutFields(Person person) {
		if (type == PersonPanelType.DISPLAY) {
			((TextFormField) getField(PERSON.departureDate)).setEnabled(!StringUtils.isBlank(person.departureDate));
			((PlaceReadOnlyField) getField(PERSON.goesTo)).setEnabled(person.goesTo != null && !person.goesTo.isUnknown());
			((AddressTextField) getField(PERSON.goesToAddress)).setEnabled(person.goesTo != null && person.goesTo.mailAddress != null && !person.goesTo.mailAddress.isEmpty());
		}
	}
	
	@Override
	public void fillWithDemoData() {
		super.fillWithDemoData();

		boolean male = Math.random() < .5;
		NameWithFrequency generatedName = FirstNameGenerator.getName(male);
		((FormField<String>) getField(PERSON.personIdentification.firstName)).setObject(generatedName.name);
		CodeEditField sexField = (CodeEditField) getField(PERSON_IDENTIFICATION.sex);
		if (sexField != null) sexField.setObject(male ? "1" : "2");
	}
	
	public static void main(String... args) {
		FrameManager.setSystemLookAndFeel();
		Resources.addResourceBundle(ResourceBundle.getBundle("ch.openech.resources.OpenEch"));
		EchFormats.initialize();
		ClientToolkit.setToolkit(new SwingClientToolkit());

		PersonPanel personPanel = new PersonPanel(PersonPanelType.DISPLAY, null);
		personPanel.setObject(new Person());
		
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add((Component) personPanel.getComponent(), BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
