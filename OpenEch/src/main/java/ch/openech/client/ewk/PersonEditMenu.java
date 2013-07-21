package ch.openech.client.ewk;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import ch.openech.client.e21.CareEvent;
import ch.openech.client.ewk.event.AddressLockEvent;
import ch.openech.client.ewk.event.BirthChildEvent;
import ch.openech.client.ewk.event.ChangeCitizenEvent;
import ch.openech.client.ewk.event.ChangeGardianEvent;
import ch.openech.client.ewk.event.ChangeNameEvent;
import ch.openech.client.ewk.event.ChangeNationalityEvent;
import ch.openech.client.ewk.event.ChangeOccupationEvent;
import ch.openech.client.ewk.event.ChangeReligionEvent;
import ch.openech.client.ewk.event.ChangeResidencePermitEvent;
import ch.openech.client.ewk.event.ChangeTypeOfResidenceEvent;
import ch.openech.client.ewk.event.ChangeWithSecondPersonEvent.DeathEvent;
import ch.openech.client.ewk.event.ChangeWithSecondPersonEvent.DivorceEvent;
import ch.openech.client.ewk.event.ChangeWithSecondPersonEvent.MissingEvent;
import ch.openech.client.ewk.event.ChangeWithSecondPersonEvent.SeparationEvent;
import ch.openech.client.ewk.event.ChangeWithSecondPersonEvent.UndoMarriageEvent;
import ch.openech.client.ewk.event.ChangeWithSecondPersonEvent.UndoPartnershipEvent;
import ch.openech.client.ewk.event.ChangeWithSecondPersonEvent.UndoSeparationEvent;
import ch.openech.client.ewk.event.ContactEvent;
import ch.openech.client.ewk.event.GardianMeasureEvent;
import ch.openech.client.ewk.event.MarriageEvent;
import ch.openech.client.ewk.event.MoveEvent;
import ch.openech.client.ewk.event.MoveOutEvent;
import ch.openech.client.ewk.event.NaturalizeForeignerEvent;
import ch.openech.client.ewk.event.NaturalizeSwissEvent;
import ch.openech.client.ewk.event.PaperLockEvent;
import ch.openech.client.ewk.event.PartnershipEvent;
import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.client.ewk.event.RenewPermitEvent;
import ch.openech.client.ewk.event.UndoCitizenEvent;
import ch.openech.client.ewk.event.UndoGardianEvent;
import ch.openech.client.ewk.event.UndoMissingEvent;
import ch.openech.client.ewk.event.UndoSwissEvent;
import ch.openech.client.ewk.event.correct.CorrectAddressEvent;
import ch.openech.client.ewk.event.correct.CorrectContactEvent;
import ch.openech.client.ewk.event.correct.CorrectDateOfDeathEvent;
import ch.openech.client.ewk.event.correct.CorrectIdentificationEvent;
import ch.openech.client.ewk.event.correct.CorrectLanguageOfCorrespondanceEvent;
import ch.openech.client.ewk.event.correct.CorrectMaritalDataEvent;
import ch.openech.client.ewk.event.correct.CorrectNameEvent;
import ch.openech.client.ewk.event.correct.CorrectNationalityEvent;
import ch.openech.client.ewk.event.correct.CorrectOccupationEvent;
import ch.openech.client.ewk.event.correct.CorrectOriginEvent;
import ch.openech.client.ewk.event.correct.CorrectPersonEvent;
import ch.openech.client.ewk.event.correct.CorrectPlaceOfBirthEvent;
import ch.openech.client.ewk.event.correct.CorrectRelationshipEvent;
import ch.openech.client.ewk.event.correct.CorrectReligionEvent;
import ch.openech.client.ewk.event.correct.CorrectReportingEvent;
import ch.openech.client.ewk.event.correct.CorrectResidencePermitEvent;
import ch.openech.client.page.PersonHistoryPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.SeparatorAction;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchSchema;

public class PersonEditMenu {

	private final EchSchema echSchema;
	private final PageContext context;
	private Person person;
	
	private HistoryAction showHistory;

	private Action marriage, separation, undoSeparation, divorce, undoMarriage, partnership, undoPartnership;
	private Action naturalizeSwiss, naturalizeForeigner, undoSwiss, changeCitizen, changeNationality,
			changeResidencePermit, renewPermit, undoCitizen;
	private Action changeOccupation;
	private Action death, missing, undoMissing;
	private Action care, gardianMeasure, undoGardian, changeGardian;
	private Action move, moveOut;
	private Action changeName, changeReligion, changeTypeOfResidence, changeRelation, contact;
	private Action correctOrigin, correctResidencePermit;
	private Action addressLock, paperLock;
	private Action birthChild;
	
	private final List<Action> correctEditors = new ArrayList<Action>();

	public PersonEditMenu(PageContext context, EchSchema echNamespaceContext) {
		this.context = context;
		this.echSchema = echNamespaceContext;
		createMenuItems();
	}
	
	public void fillActionGroup(ActionGroup personActionGroup) {
		if (person == null) return;

		personActionGroup.putValue(Action.NAME, "Person");
		personActionGroup.putValue(Action.MNEMONIC_KEY, 'P');

		ActionGroup partnershipGroup = personActionGroup.getOrCreateActionGroup("person_partnership");
		partnershipGroup.add(marriage);
		partnershipGroup.add(separation);
		partnershipGroup.add(undoSeparation);
		partnershipGroup.add(divorce);
		partnershipGroup.add(undoMarriage);
		partnershipGroup.addSeparator();
		partnershipGroup.add(partnership);
		partnershipGroup.add(undoPartnership);

		ActionGroup origin = personActionGroup.getOrCreateActionGroup("person_nationality");
		origin.add(naturalizeSwiss);
		origin.add(naturalizeForeigner);
		origin.add(undoSwiss);
		origin.add(changeCitizen);
		origin.add(changeNationality);
		origin.add(changeResidencePermit);
		origin.add(renewPermit);
		origin.add(undoCitizen);

		ActionGroup occupation = personActionGroup.getOrCreateActionGroup("person_occupation");
		occupation.add(changeOccupation);
		
		ActionGroup moveGroup = personActionGroup.getOrCreateActionGroup("person_move");
		moveGroup.add(move);
		moveGroup.add(moveOut);

		ActionGroup deathGroup = personActionGroup.getOrCreateActionGroup("person_death");
		deathGroup.add(death);
		deathGroup.add(missing);
		deathGroup.add(undoMissing);

		ActionGroup gardian = personActionGroup.getOrCreateActionGroup("person_care");
		gardian.add(care);
		gardian.add(gardianMeasure);
		gardian.add(undoGardian);
		gardian.add(changeGardian);

		ActionGroup lock = personActionGroup.getOrCreateActionGroup("person_lock");
		lock.add(addressLock);
		lock.add(paperLock);

		ActionGroup change = personActionGroup.getOrCreateActionGroup("person_change");
		change.add(changeName);
		change.add(changeReligion);
		change.add(changeTypeOfResidence);
		change.add(changeRelation);
		change.add(contact);

		fillCorrectionActionList();
		ActionGroup correct = personActionGroup.getOrCreateActionGroup("correction");
		for (Action action : correctEditors) {
			correct.add(action);
		}

		personActionGroup.add(new SeparatorAction());
		personActionGroup.add(birthChild); 
		
		personActionGroup.add(new SeparatorAction());
		personActionGroup.add(showHistory); 
		
		//
		
		List<Action> actions = personActionGroup.getAllActions();
		showHistory.setPerson(person);
		for (Action action : actions) {
			if (action instanceof PersonEditMenuAction) {
				PersonEditMenuAction personEditMenuAction = (PersonEditMenuAction) action;
				personEditMenuAction.setPerson(person);
			}
		}
	}

	private class PersonEditMenuAction extends EditorDialogAction {
		private final PersonEventEditor<?> editor;
		
		public PersonEditMenuAction(PersonEventEditor<?> editor) {
			super(editor);
			this.editor = editor;
		}
		
		public void setPerson(Person person) {
			editor.setPerson(person);
		}
	}
	
	private class HistoryAction extends ResourceAction {
		private Person person;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			context.show(Page.link(PersonHistoryPage.class, echSchema.getVersion(), person.getId()));
		}
		
		public void setPerson(Person person) {
			this.person = person;
			setEnabled(person != null);
		}
	}	

	private void createMenuItems() {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		
		marriage = new PersonEditMenuAction(new MarriageEvent(echSchema, preferences));
		separation = new PersonEditMenuAction(new SeparationEvent(echSchema, preferences));
		undoSeparation = new PersonEditMenuAction(new UndoSeparationEvent(echSchema, preferences));
		divorce = new PersonEditMenuAction(new DivorceEvent(echSchema, preferences));
		undoMarriage = new PersonEditMenuAction(new UndoMarriageEvent(echSchema, preferences));
		partnership = new PersonEditMenuAction(new PartnershipEvent(echSchema, preferences));
		undoPartnership = new PersonEditMenuAction(new UndoPartnershipEvent(echSchema, preferences));

		naturalizeSwiss = new PersonEditMenuAction(new NaturalizeSwissEvent(echSchema, preferences));
		naturalizeForeigner = new PersonEditMenuAction(new NaturalizeForeignerEvent(echSchema, preferences));
		undoSwiss = new PersonEditMenuAction(new UndoSwissEvent(echSchema, preferences));
		changeCitizen = new PersonEditMenuAction(new ChangeCitizenEvent(echSchema, preferences));
		changeNationality = new PersonEditMenuAction(new ChangeNationalityEvent(echSchema, preferences));
		changeResidencePermit = new PersonEditMenuAction(new ChangeResidencePermitEvent(echSchema, preferences));
		renewPermit = new PersonEditMenuAction(new RenewPermitEvent(echSchema, preferences));
		undoCitizen = new PersonEditMenuAction(new UndoCitizenEvent(echSchema, preferences));

		changeOccupation = new PersonEditMenuAction(new ChangeOccupationEvent(echSchema, preferences));

		death = new PersonEditMenuAction(new DeathEvent(echSchema, preferences));
		missing = new PersonEditMenuAction(new MissingEvent(echSchema, preferences));
		undoMissing = new PersonEditMenuAction(new UndoMissingEvent(echSchema, preferences));

		care = new PersonEditMenuAction(new CareEvent(echSchema, preferences));
		gardianMeasure = new PersonEditMenuAction(new GardianMeasureEvent(echSchema, preferences));
		undoGardian = new PersonEditMenuAction(new UndoGardianEvent(echSchema, preferences));
		changeGardian = new PersonEditMenuAction(new ChangeGardianEvent(echSchema, preferences));

		move = new PersonEditMenuAction(new MoveEvent(echSchema, preferences));
		moveOut = new PersonEditMenuAction(new MoveOutEvent(echSchema, preferences));

		changeName = new PersonEditMenuAction(new ChangeNameEvent(echSchema, preferences));
		changeReligion = new PersonEditMenuAction(new ChangeReligionEvent(echSchema, preferences));
		changeTypeOfResidence = new PersonEditMenuAction(new ChangeTypeOfResidenceEvent(echSchema, preferences));
		changeRelation = new PersonEditMenuAction(new CorrectRelationshipEvent(echSchema, preferences));
		contact = new PersonEditMenuAction(new ContactEvent(echSchema, preferences));

		correctOrigin = new PersonEditMenuAction(new CorrectOriginEvent(echSchema, preferences));
		correctResidencePermit = new PersonEditMenuAction(new CorrectResidencePermitEvent(echSchema, preferences));

		addressLock = new PersonEditMenuAction(new AddressLockEvent(echSchema, preferences));
		paperLock = new PersonEditMenuAction(new PaperLockEvent(echSchema, preferences));
		
		birthChild = new PersonEditMenuAction(new BirthChildEvent(echSchema, preferences));

		showHistory = new HistoryAction();
	}

	private void fillCorrectionActionList() {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();

		addCorrectAction(new CorrectPersonEvent(echSchema, preferences));
		addCorrectAction(new CorrectAddressEvent(echSchema, preferences));
		addCorrectAction(new CorrectReportingEvent(echSchema, preferences));
		addCorrectAction(new CorrectOccupationEvent(echSchema, preferences));

		if (echSchema.additionalCorrectEvents()) {
			addCorrectAction(new CorrectIdentificationEvent(echSchema, preferences));
			addCorrectAction(new CorrectNameEvent(echSchema, preferences));
			addCorrectAction(new CorrectNationalityEvent(echSchema, preferences));
			addCorrectAction(new CorrectContactEvent(echSchema, preferences));
			addCorrectAction(new CorrectReligionEvent(echSchema, preferences));
			addCorrectAction(correctOrigin);
			addCorrectAction(correctResidencePermit);
			addCorrectAction(new CorrectMaritalDataEvent(echSchema, preferences));
			addCorrectAction(new CorrectPlaceOfBirthEvent(echSchema, preferences));
			addCorrectAction(new CorrectDateOfDeathEvent(echSchema, preferences));
			addCorrectAction(new CorrectLanguageOfCorrespondanceEvent(echSchema, preferences));
		}
	}

	private void addCorrectAction(PersonEventEditor<?> editor) {
		correctEditors.add(new PersonEditMenuAction(editor));
	}

	
	private void addCorrectAction(Action action) {
		correctEditors.add(action);
	}

	public void setPerson(Person person, boolean enabled) {
		this.person = person;
		update(person, enabled);
	}
	
	@BusinessRule("Welche Aktion in welchem Zustand von Personen ausgeführt werden darf")
	public void update(Person person, boolean enabled) {
		boolean isPerson = person != null && enabled;
		boolean isAlive = isPerson && person.isAlive();
		boolean isMarried = isPerson && person.isMarried();
		boolean isSeparated = isPerson && person.isSeparated();
		boolean hasPartner = isPerson && person.hasPartner();
		boolean isSwiss = isPerson && person.isSwiss();
		boolean hasGardianMeasure = isPerson && person.hasGardianMeasure();
		boolean isMainResidence = isPerson && person.isMainResidence();
		
		death.setEnabled(isAlive);
		divorce.setEnabled(isAlive && isMarried);
		separation.setEnabled(isAlive && isMarried && !isSeparated);
		undoSeparation.setEnabled(isAlive && isSeparated);
		missing.setEnabled(isAlive);
		undoMissing.setEnabled(isPerson && !isAlive);
		undoPartnership.setEnabled(isAlive & hasPartner);
		changeOccupation.setEnabled(isAlive);
		marriage.setEnabled(isAlive && !isMarried && !hasPartner);
		undoMarriage.setEnabled(isAlive && isMarried);
		partnership.setEnabled(isAlive && !isMarried && !hasPartner);
		changeName.setEnabled(isPerson);
		naturalizeSwiss.setEnabled(isAlive && isSwiss);
		undoSwiss.setEnabled(isAlive && isSwiss);
		changeCitizen.setEnabled(isAlive && isSwiss);
		naturalizeForeigner.setEnabled(isAlive && !isSwiss);
		changeNationality.setEnabled(isAlive && !isSwiss);
		undoCitizen.setEnabled(isAlive && isSwiss);
		changeReligion.setEnabled(isAlive);
		gardianMeasure.setEnabled(isAlive && !hasGardianMeasure);
		undoGardian.setEnabled(hasGardianMeasure);
		changeGardian.setEnabled(isAlive && hasGardianMeasure);
		changeTypeOfResidence.setEnabled(isAlive && !isMainResidence);
		moveOut.setEnabled(isAlive);
		move.setEnabled(isAlive);
		changeResidencePermit.setEnabled(isAlive && !isSwiss);
		renewPermit.setEnabled(isAlive && !isSwiss);
		changeRelation.setEnabled(isAlive);
		for (Action action : correctEditors) {
			action.setEnabled(isPerson);
		}
		correctOrigin.setEnabled(isPerson && isSwiss);
		correctResidencePermit.setEnabled(isPerson && !isSwiss);
		care.setEnabled(isAlive);
		contact.setEnabled(isPerson);
	}

}
