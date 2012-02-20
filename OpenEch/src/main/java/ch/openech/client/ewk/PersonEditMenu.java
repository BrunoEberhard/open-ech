package ch.openech.client.ewk;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import ch.openech.client.e21.CareEvent;
import ch.openech.client.ewk.event.AddressLockEvent;
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
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchNamespaceContext;

public class PersonEditMenu {

	private final EchNamespaceContext echNamespaceContext;
	private PageContext pageContext;
	private Person person;
	private boolean enabled;
	
	private PersonHistoryMenuAction showHistory;

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

	private final List<Action> correctEditors = new ArrayList<Action>();

	public PersonEditMenu(EchNamespaceContext echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
		createMenuItems();
	}
	
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		if (person == null) return;
		
		this.pageContext = pageContext;
		
		ActionGroup personActionGroup = actionGroup.getOrCreateActionGroup("person");
		personActionGroup.putValue(Action.MNEMONIC_KEY, 'P');

		// TODO History of person
//		ActionGroup history = person.getOrCreateActionGroup("history");
//		person.addSeparator();

		ActionGroup partnershipGroup = personActionGroup.getOrCreateActionGroup("Partnerschaft");
		partnershipGroup.add(marriage);
		partnershipGroup.add(separation);
		partnershipGroup.add(undoSeparation);
		partnershipGroup.add(divorce);
		partnershipGroup.add(undoMarriage);
		partnershipGroup.addSeparator();
		partnershipGroup.add(partnership);
		partnershipGroup.add(undoPartnership);

		ActionGroup origin = personActionGroup.getOrCreateActionGroup("Nationalität / Heimatorte");
		origin.add(naturalizeSwiss);
		origin.add(naturalizeForeigner);
		origin.add(undoSwiss);
		origin.add(changeCitizen);
		origin.add(changeNationality);
		origin.add(changeResidencePermit);
		origin.add(renewPermit);
		origin.add(undoCitizen);

		ActionGroup occupation = personActionGroup.getOrCreateActionGroup("Beruf");
		occupation.add(changeOccupation);
		
		ActionGroup moveGroup = personActionGroup.getOrCreateActionGroup("Umzug");
		moveGroup.add(move);
		moveGroup.add(moveOut);

		ActionGroup deathGroup = personActionGroup.getOrCreateActionGroup("Tod");
		deathGroup.add(death);
		deathGroup.add(missing);
		deathGroup.add(undoMissing);

		ActionGroup gardian = personActionGroup.getOrCreateActionGroup("Vormundschaft");
		gardian.add(care);
		gardian.add(gardianMeasure);
		gardian.add(undoGardian);
		gardian.add(changeGardian);

		ActionGroup lock = personActionGroup.getOrCreateActionGroup("Sperre");
		lock.add(addressLock);
		lock.add(paperLock);

		ActionGroup change = personActionGroup.getOrCreateActionGroup("Änderung");
		change.add(changeName);
		change.add(changeReligion);
		change.add(changeTypeOfResidence);
		change.add(changeRelation);
		change.add(contact);

		fillCorrectionActionList();
		ActionGroup correct = personActionGroup.getOrCreateActionGroup("Korrektur");
		for (Action action : correctEditors) {
			correct.add(action);
		}

		//
		
		List<Action> actions = actionGroup.getAllActions();
		showHistory.setPerson(person);
		for (Action action : actions) {
			action.setEnabled(enabled);
			
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
			this.setEnabled(editor.isEnabled());
		}
	}
	
	private class PersonHistoryMenuAction extends ResourceAction {
		private Person person;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			pageContext.show(Page.link(PersonHistoryViewPage.class, echNamespaceContext.getVersion(), person.getId()));
		}
		
		public void setPerson(Person person) {
			this.person = person;
			setEnabled(person != null);
		}
	}

	private void createMenuItems() {
		showHistory = new PersonHistoryMenuAction();

		marriage = new EditorDialogAction(new MarriageEvent(echNamespaceContext));
		separation = new EditorDialogAction(new SeparationEvent(echNamespaceContext));
		undoSeparation = new EditorDialogAction(new UndoSeparationEvent(echNamespaceContext));
		divorce = new EditorDialogAction(new DivorceEvent(echNamespaceContext));
		undoMarriage = new EditorDialogAction(new UndoMarriageEvent(echNamespaceContext));
		partnership = new EditorDialogAction(new PartnershipEvent(echNamespaceContext));
		undoPartnership = new EditorDialogAction(new UndoPartnershipEvent(echNamespaceContext));

		naturalizeSwiss = new EditorDialogAction(new NaturalizeSwissEvent(echNamespaceContext));
		naturalizeForeigner = new EditorDialogAction(new NaturalizeForeignerEvent(echNamespaceContext));
		undoSwiss = new EditorDialogAction(new UndoSwissEvent(echNamespaceContext));
		changeCitizen = new EditorDialogAction(new ChangeCitizenEvent(echNamespaceContext));
		changeNationality = new EditorDialogAction(new ChangeNationalityEvent(echNamespaceContext));
		changeResidencePermit = new EditorDialogAction(new ChangeResidencePermitEvent(echNamespaceContext));
		renewPermit = new EditorDialogAction(new RenewPermitEvent(echNamespaceContext));
		undoCitizen = new EditorDialogAction(new UndoCitizenEvent(echNamespaceContext));

		changeOccupation = new EditorDialogAction(new ChangeOccupationEvent(echNamespaceContext));

		death = new EditorDialogAction(new DeathEvent(echNamespaceContext));
		missing = new EditorDialogAction(new MissingEvent(echNamespaceContext));
		undoMissing = new EditorDialogAction(new UndoMissingEvent(echNamespaceContext));

		care = new EditorDialogAction(new CareEvent(echNamespaceContext));
		gardianMeasure = new EditorDialogAction(new GardianMeasureEvent(echNamespaceContext));
		undoGardian = new EditorDialogAction(new UndoGardianEvent(echNamespaceContext));
		changeGardian = new EditorDialogAction(new ChangeGardianEvent(echNamespaceContext));

		move = new EditorDialogAction(new MoveEvent(echNamespaceContext));
		moveOut = new EditorDialogAction(new MoveOutEvent(echNamespaceContext));

		changeName = new EditorDialogAction(new ChangeNameEvent(echNamespaceContext));
		changeReligion = new EditorDialogAction(new ChangeReligionEvent(echNamespaceContext));
		changeTypeOfResidence = new EditorDialogAction(new ChangeTypeOfResidenceEvent(echNamespaceContext));
		changeRelation = new EditorDialogAction(new CorrectRelationshipEvent(echNamespaceContext));
		contact = new EditorDialogAction(new ContactEvent(echNamespaceContext));

		correctOrigin = new EditorDialogAction(new CorrectOriginEvent(echNamespaceContext));
		correctResidencePermit = new EditorDialogAction(new CorrectResidencePermitEvent(echNamespaceContext));

		addressLock = new EditorDialogAction(new AddressLockEvent(echNamespaceContext));
		paperLock = new EditorDialogAction(new PaperLockEvent(echNamespaceContext));
	}

	private void fillCorrectionActionList() {
		addCorrectAction(new CorrectPersonEvent(echNamespaceContext));
		addCorrectAction(new CorrectAddressEvent(echNamespaceContext));
		addCorrectAction(new CorrectReportingEvent(echNamespaceContext));
		addCorrectAction(new CorrectOccupationEvent(echNamespaceContext));

		if (echNamespaceContext.additionalCorrectEvents()) {
			addCorrectAction(new CorrectIdentificationEvent(echNamespaceContext));
			addCorrectAction(new CorrectNameEvent(echNamespaceContext));
			addCorrectAction(new CorrectNationalityEvent(echNamespaceContext));
			addCorrectAction(new CorrectContactEvent(echNamespaceContext));
			addCorrectAction(new CorrectReligionEvent(echNamespaceContext));
			addCorrectAction(correctOrigin);
			addCorrectAction(correctResidencePermit);
			addCorrectAction(new CorrectMaritalDataEvent(echNamespaceContext));
			addCorrectAction(new CorrectPlaceOfBirthEvent(echNamespaceContext));
			addCorrectAction(new CorrectDateOfDeathEvent(echNamespaceContext));
			addCorrectAction(new CorrectLanguageOfCorrespondanceEvent(echNamespaceContext));
		}
	}

	private void addCorrectAction(PersonEventEditor<?> editor) {
		correctEditors.add(new EditorDialogAction(editor));
	}

	
	private void addCorrectAction(Action action) {
		correctEditors.add(action);
	}

	public void setPerson(Person person, boolean enabled) {
		this.person = person;
		this.enabled = enabled;
	}
	
	@BusinessRule("Welche Aktion in welchem Zustand ausgeführt werden darf")
	public void update(Person person, boolean enabled) {
		boolean isPerson = person != null && enabled;
		boolean isAlive = isPerson && person.isAlive();
		boolean isMarried = isPerson && person.isMarried();
		boolean isSeparated = isPerson && person.isSeparated();
		boolean hasPartner = isPerson && person.hasPartner();
		boolean isSwiss = isPerson && person.isSwiss();
		boolean hasGardianMeasure = isPerson && person.hasGardianMeasure();
		boolean isMainResidence = isPerson && person.isMainResidence();
//		this.setEnabled(person != null);

		
		death.setEnabled(isAlive);
		divorce.setEnabled(isAlive && isMarried);
		separation.setEnabled(isAlive && isMarried && !isSeparated);
		undoSeparation.setEnabled(isAlive && isSeparated);
		missing.setEnabled(isAlive);

		// TODO !!
		
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
		changeNationality.setEnabled(isAlive);
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
