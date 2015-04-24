package ch.openech.frontend.ewk;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.editor.EditorAction;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.util.BusinessRule;

import ch.openech.frontend.e21.CareEvent;
import ch.openech.frontend.ewk.event.AddressLockEvent;
import ch.openech.frontend.ewk.event.BirthChildEvent;
import ch.openech.frontend.ewk.event.ChangeCitizenEvent;
import ch.openech.frontend.ewk.event.ChangeGardianEvent;
import ch.openech.frontend.ewk.event.ChangeNameEvent;
import ch.openech.frontend.ewk.event.ChangeNationalityEvent;
import ch.openech.frontend.ewk.event.ChangeOccupationEvent;
import ch.openech.frontend.ewk.event.ChangeReligionEvent;
import ch.openech.frontend.ewk.event.ChangeResidencePermitEvent;
import ch.openech.frontend.ewk.event.ChangeTypeOfResidenceEvent;
import ch.openech.frontend.ewk.event.ChangeWithSecondPersonEvent.DeathEvent;
import ch.openech.frontend.ewk.event.ChangeWithSecondPersonEvent.DivorceEvent;
import ch.openech.frontend.ewk.event.ChangeWithSecondPersonEvent.MissingEvent;
import ch.openech.frontend.ewk.event.ChangeWithSecondPersonEvent.SeparationEvent;
import ch.openech.frontend.ewk.event.ChangeWithSecondPersonEvent.UndoMarriageEvent;
import ch.openech.frontend.ewk.event.ChangeWithSecondPersonEvent.UndoPartnershipEvent;
import ch.openech.frontend.ewk.event.ChangeWithSecondPersonEvent.UndoSeparationEvent;
import ch.openech.frontend.ewk.event.ContactEvent;
import ch.openech.frontend.ewk.event.GardianMeasureEvent;
import ch.openech.frontend.ewk.event.MarriageEvent;
import ch.openech.frontend.ewk.event.MoveEvent;
import ch.openech.frontend.ewk.event.MoveOutEvent;
import ch.openech.frontend.ewk.event.NaturalizeForeignerEvent;
import ch.openech.frontend.ewk.event.NaturalizeSwissEvent;
import ch.openech.frontend.ewk.event.PaperLockEvent;
import ch.openech.frontend.ewk.event.PartnershipEvent;
import ch.openech.frontend.ewk.event.PersonEventEditor;
import ch.openech.frontend.ewk.event.RenewPermitEvent;
import ch.openech.frontend.ewk.event.UndoCitizenEvent;
import ch.openech.frontend.ewk.event.UndoGardianEvent;
import ch.openech.frontend.ewk.event.UndoMissingEvent;
import ch.openech.frontend.ewk.event.UndoSwissEvent;
import ch.openech.frontend.ewk.event.correct.CorrectAddressEvent;
import ch.openech.frontend.ewk.event.correct.CorrectContactEvent;
import ch.openech.frontend.ewk.event.correct.CorrectDateOfDeathEvent;
import ch.openech.frontend.ewk.event.correct.CorrectIdentificationEvent;
import ch.openech.frontend.ewk.event.correct.CorrectLanguageOfCorrespondanceEvent;
import ch.openech.frontend.ewk.event.correct.CorrectMaritalDataEvent;
import ch.openech.frontend.ewk.event.correct.CorrectNameEvent;
import ch.openech.frontend.ewk.event.correct.CorrectNationalityEvent;
import ch.openech.frontend.ewk.event.correct.CorrectOccupationEvent;
import ch.openech.frontend.ewk.event.correct.CorrectOriginEvent;
import ch.openech.frontend.ewk.event.correct.CorrectPersonEvent;
import ch.openech.frontend.ewk.event.correct.CorrectPlaceOfBirthEvent;
import ch.openech.frontend.ewk.event.correct.CorrectRelationshipEvent;
import ch.openech.frontend.ewk.event.correct.CorrectReligionEvent;
import ch.openech.frontend.ewk.event.correct.CorrectReportingEvent;
import ch.openech.frontend.ewk.event.correct.CorrectResidencePermitEvent;
import ch.openech.frontend.page.PersonHistoryPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;

public class PersonEditMenu {

	private final EchSchema echSchema;
	private final Person person;
	
	private final Page showHistory;

	private final EditorAction marriage, separation, undoSeparation, divorce, undoMarriage, partnership, undoPartnership;
	private final EditorAction naturalizeSwiss, naturalizeForeigner, undoSwiss, changeCitizen, changeNationality, changeResidencePermit, renewPermit, undoCitizen;
	private final EditorAction changeOccupation;
	private final EditorAction death, missing, undoMissing;
	private final EditorAction care, gardianMeasure, undoGardian, changeGardian;
	private final EditorAction move, moveOut;
	private final EditorAction changeName, changeReligion, changeTypeOfResidence, changeRelation, contact;
	private final EditorAction correctOrigin, correctResidencePermit;
	private final EditorAction addressLock, paperLock;
	private final EditorAction birthChild;
	
	private final List<EditorAction> correctEditors = new ArrayList<>();

	public PersonEditMenu(EchSchema ech, Person person) {
		this.echSchema = ech;
		this.person = person;
		
		marriage = editorAction(new MarriageEvent(ech, person));
		
		separation = editorAction(new SeparationEvent(ech, person));
		undoSeparation = editorAction(new UndoSeparationEvent(ech, person));
		divorce = editorAction(new DivorceEvent(ech, person));
		undoMarriage = editorAction(new UndoMarriageEvent(ech, person));
		partnership = editorAction(new PartnershipEvent(ech, person));
		undoPartnership = editorAction(new UndoPartnershipEvent(ech, person));

		naturalizeSwiss = editorAction(new NaturalizeSwissEvent(ech, person));
		naturalizeForeigner = editorAction(new NaturalizeForeignerEvent(ech, person));
		undoSwiss = editorAction(new UndoSwissEvent(ech, person));
		changeCitizen = editorAction(new ChangeCitizenEvent(ech, person));
		changeNationality = editorAction(new ChangeNationalityEvent(ech, person));
		changeResidencePermit = editorAction(new ChangeResidencePermitEvent(ech, person));
		renewPermit = editorAction(new RenewPermitEvent(ech, person));
		undoCitizen = editorAction(new UndoCitizenEvent(ech, person));

		changeOccupation = editorAction(new ChangeOccupationEvent(ech, person));

		death = editorAction(new DeathEvent(ech, person));
		missing = editorAction(new MissingEvent(ech, person));
		undoMissing = editorAction(new UndoMissingEvent(ech, person));

		care = editorAction(new CareEvent(ech, person));
		gardianMeasure = editorAction(new GardianMeasureEvent(ech, person));
		undoGardian = editorAction(new UndoGardianEvent(ech, person));
		changeGardian = editorAction(new ChangeGardianEvent(ech, person));

		move = editorAction(new MoveEvent(ech, person));
		moveOut = editorAction(new MoveOutEvent(ech, person));

		changeName = editorAction(new ChangeNameEvent(ech, person));
		changeReligion = editorAction(new ChangeReligionEvent(ech, person));
		changeTypeOfResidence = editorAction(new ChangeTypeOfResidenceEvent(ech, person));
		changeRelation = editorAction(new CorrectRelationshipEvent(ech, person));
		contact = editorAction(new ContactEvent(ech, person));

		correctOrigin = editorAction(new CorrectOriginEvent(ech, person));
		correctResidencePermit = editorAction(new CorrectResidencePermitEvent(ech, person));

		addressLock = editorAction(new AddressLockEvent(ech, person));
		paperLock = editorAction(new PaperLockEvent(ech, person));
		
		birthChild = editorAction(new BirthChildEvent(ech, person, (OpenEchPreferences) ClientToolkit.getToolkit().getApplicationContext().getPreferences()));

		showHistory = new PersonHistoryPage(echSchema, person);
		
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
		for (EditorAction action : correctEditors) {
			correct.add(action);
		}

		menu.addSeparator();
		menu.add(birthChild); 
		
		menu.addSeparator();
		menu.add(showHistory); 
		
		return menu;
	}

	private EditorAction editorAction(PersonEventEditor<?> editor) {
		EditorAction action = new EditorAction(editor);
		action.setEnabled(editor.isEnabled());
		return action;
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
		correctEditors.add(new EditorAction(editor)); // always enbled
	}

	
	private void addCorrectAction(EditorAction action) {
		correctEditors.add(action);
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
		for (EditorAction action : correctEditors) {
			action.setEnabled(isPerson);
		}
		correctOrigin.setEnabled(isPerson && isSwiss);
		correctResidencePermit.setEnabled(isPerson && !isSwiss);
		care.setEnabled(isAlive);
		contact.setEnabled(isPerson);
	}

}
