package ch.openech.client.ewk;

import java.util.ArrayList;
import java.util.List;

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
import ch.openech.mj.edit.Editor;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.EditorPageAction;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchSchema;

public class PersonEditMenu {

	private final EchSchema echSchema;
	private final Person person;
	
	private final PageLink showHistory;

	private final EditorPageAction marriage, separation, undoSeparation, divorce, undoMarriage, partnership, undoPartnership;
	private final EditorPageAction naturalizeSwiss, naturalizeForeigner, undoSwiss, changeCitizen, changeNationality, changeResidencePermit, renewPermit, undoCitizen;
	private final EditorPageAction changeOccupation;
	private final EditorPageAction death, missing, undoMissing;
	private final EditorPageAction care, gardianMeasure, undoGardian, changeGardian;
	private final EditorPageAction move, moveOut;
	private final EditorPageAction changeName, changeReligion, changeTypeOfResidence, changeRelation, contact;
	private final EditorPageAction correctOrigin, correctResidencePermit;
	private final EditorPageAction addressLock, paperLock;
	private final EditorPageAction birthChild;
	
	private final List<EditorPageAction> correctEditors = new ArrayList<EditorPageAction>();

	public PersonEditMenu(EchSchema ech, PageContext pageContext, Person person) {
		this.echSchema = ech;
		this.person = person;
		
		marriage = editorLink(new MarriageEvent(ech, person));
		
		separation = editorLink(new SeparationEvent(ech, person));
		undoSeparation = editorLink(new UndoSeparationEvent(ech, person));
		divorce = editorLink(new DivorceEvent(ech, person));
		undoMarriage = editorLink(new UndoMarriageEvent(ech, person));
		partnership = editorLink(new PartnershipEvent(ech, person));
		undoPartnership = editorLink(new UndoPartnershipEvent(ech, person));

		naturalizeSwiss = editorLink(new NaturalizeSwissEvent(ech, person));
		naturalizeForeigner = editorLink(new NaturalizeForeignerEvent(ech, person));
		undoSwiss = editorLink(new UndoSwissEvent(ech, person));
		changeCitizen = editorLink(new ChangeCitizenEvent(ech, person));
		changeNationality = editorLink(new ChangeNationalityEvent(ech, person));
		changeResidencePermit = editorLink(new ChangeResidencePermitEvent(ech, person));
		renewPermit = editorLink(new RenewPermitEvent(ech, person));
		undoCitizen = editorLink(new UndoCitizenEvent(ech, person));

		changeOccupation = editorLink(new ChangeOccupationEvent(ech, person));

		death = editorLink(new DeathEvent(ech, person));
		missing = editorLink(new MissingEvent(ech, person));
		undoMissing = editorLink(new UndoMissingEvent(ech, person));

		care = editorLink(new CareEvent(ech, person));
		gardianMeasure = editorLink(new GardianMeasureEvent(ech, person));
		undoGardian = editorLink(new UndoGardianEvent(ech, person));
		changeGardian = editorLink(new ChangeGardianEvent(ech, person));

		move = editorLink(new MoveEvent(ech, person));
		moveOut = editorLink(new MoveOutEvent(ech, person));

		changeName = editorLink(new ChangeNameEvent(ech, person));
		changeReligion = editorLink(new ChangeReligionEvent(ech, person));
		changeTypeOfResidence = editorLink(new ChangeTypeOfResidenceEvent(ech, person));
		changeRelation = editorLink(new CorrectRelationshipEvent(ech, person));
		contact = editorLink(new ContactEvent(ech, person));

		correctOrigin = editorLink(new CorrectOriginEvent(ech, person));
		correctResidencePermit = editorLink(new CorrectResidencePermitEvent(ech, person));

		addressLock = editorLink(new AddressLockEvent(ech, person));
		paperLock = editorLink(new PaperLockEvent(ech, person));
		
		birthChild = editorLink(new BirthChildEvent(ech, person, (OpenEchPreferences) pageContext.getApplicationContext().getPreferences()));

		showHistory = new PageLink(PersonHistoryPage.class, echSchema.getVersion(), person.getId());
		
		update(person, true);
	}
	
	public ActionGroup getActions() {
		if (person == null) return null;
		ActionGroup menu = new ActionGroup("Person");
//		personActionGroup.putValue(Action.MNEMONIC_KEY, 'P');

		ActionGroup partnershipGroup = menu.addGroup("ActionGroup.person_partnership");
		partnershipGroup.add(marriage);
		partnershipGroup.add(separation);
		partnershipGroup.add(undoSeparation);
		partnershipGroup.add(divorce);
		partnershipGroup.add(undoMarriage);
		partnershipGroup.addSeparator();
		partnershipGroup.add(partnership);
		partnershipGroup.add(undoPartnership);

		ActionGroup origin = menu.addGroup("ActionGroup.person_nationality");
		origin.add(naturalizeSwiss);
		origin.add(naturalizeForeigner);
		origin.add(undoSwiss);
		origin.add(changeCitizen);
		origin.add(changeNationality);
		origin.add(changeResidencePermit);
		origin.add(renewPermit);
		origin.add(undoCitizen);

		ActionGroup occupation = menu.addGroup("ActionGroup.person_occupation");
		occupation.add(changeOccupation);
		
		ActionGroup moveGroup = menu.addGroup("ActionGroup.person_move");
		moveGroup.add(move);
		moveGroup.add(moveOut);

		ActionGroup deathGroup = menu.addGroup("ActionGroup.person_death");
		deathGroup.add(death);
		deathGroup.add(missing);
		deathGroup.add(undoMissing);

		ActionGroup gardian = menu.addGroup("ActionGroup.person_care");
		gardian.add(care);
		gardian.add(gardianMeasure);
		gardian.add(undoGardian);
		gardian.add(changeGardian);

		ActionGroup lock = menu.addGroup("ActionGroup.person_lock");
		lock.add(addressLock);
		lock.add(paperLock);

		ActionGroup change = menu.addGroup("ActionGroup.person_change");
		change.add(changeName);
		change.add(changeReligion);
		change.add(changeTypeOfResidence);
		change.add(changeRelation);
		change.add(contact);

		fillCorrectionActionList();
		ActionGroup correct = menu.addGroup("ActionGroup.correction");
		for (EditorPageAction action : correctEditors) {
			correct.add(action);
		}

		menu.addSeparator();
		menu.add(birthChild); 
		
		menu.addSeparator();
		menu.add(showHistory); 
		
		return menu;
	}

	private EditorPageAction editorLink(PersonEventEditor<?> editor) {
		return new EditorPageAction(editor, editor.isEnabled());
	}
	
	private void fillCorrectionActionList() {
		addCorrectAction(new CorrectPersonEvent(echSchema, person));
		addCorrectAction(new CorrectAddressEvent(echSchema, person));
		addCorrectAction(new CorrectReportingEvent(echSchema, person));
		addCorrectAction(new CorrectOccupationEvent(echSchema, person));

		if (echSchema.additionalCorrectEvents()) {
			addCorrectAction(new CorrectIdentificationEvent(echSchema, person));
			addCorrectAction(new CorrectNameEvent(echSchema, person));
			addCorrectAction(new CorrectNationalityEvent(echSchema, person));
			addCorrectAction(new CorrectContactEvent(echSchema, person));
			addCorrectAction(new CorrectReligionEvent(echSchema, person));
			addCorrectAction(correctOrigin);
			addCorrectAction(correctResidencePermit);
			addCorrectAction(new CorrectMaritalDataEvent(echSchema, person));
			addCorrectAction(new CorrectPlaceOfBirthEvent(echSchema, person));
			addCorrectAction(new CorrectDateOfDeathEvent(echSchema, person));
			addCorrectAction(new CorrectLanguageOfCorrespondanceEvent(echSchema, person));
		}
	}

	private void addCorrectAction(Editor<?> editor) {
		correctEditors.add(new EditorPageAction(editor)); // always enbled
	}

	
	private void addCorrectAction(EditorPageAction action) {
		correctEditors.add(action);
	}

	@BusinessRule("Welche Aktion in welchem Zustand von Personen ausgeführt werden darf")
	private void update(Person person, boolean enabled) {
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
		for (EditorPageAction action : correctEditors) {
			action.setEnabled(isPerson);
		}
		correctOrigin.setEnabled(isPerson && isSwiss);
		correctResidencePermit.setEnabled(isPerson && !isSwiss);
		care.setEnabled(isAlive);
		contact.setEnabled(isPerson);
	}

}
