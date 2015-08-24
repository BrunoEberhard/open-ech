package ch.openech.frontend.page;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.application.Preferences;
import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.ActionGroup;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.transaction.persistence.ReadTransaction;
import org.minimalj.util.BusinessRule;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.e21.CareEvent;
import ch.openech.frontend.ewk.PersonPanel;
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
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.xml.write.EchSchema;

public class PersonPage extends ObjectPage<Person> {

	private final EchSchema echSchema;
	private int version;
	private transient PersonActionGroup actionGroup;
	private transient PersonHistoryPage historyPage;
	
	public PersonPage(EchSchema echSchema, Person person) {
		super(person);
		this.echSchema = echSchema;
		this.version = person.version;
	}

	public PersonPage(EchSchema echSchema, Object personId) {
		super(Person.class, personId);
		this.echSchema = echSchema;
		this.version = 0;
	}
	
	@Override
	public void setObject(Person person) {
		super.setObject(person);
		this.version = person.version;
		if (actionGroup != null) {
			actionGroup.refresh();
		}
		if (historyPage != null) {
			historyPage.refresh();
		}
	}
	
	public EchSchema getEchSchema() {
		return echSchema;
	}
	
	@Override
	public List<Action> getActions() {
		if (version == 0) {
			if (actionGroup == null) {
				actionGroup = new PersonActionGroup();
			}
			return actionGroup.getItems();
		} else {
			return null;
		}
	}
	
	@Override
	public Person load() {
		Person person;
		if (version == 0) {
			person = Backend.persistence().read(Person.class, getObjectId());
		} else {
			person = Backend.getInstance().execute(new ReadTransaction<Person>(Person.class, getObjectId(), version));
		}
		return person;
	}

	@Override
	public Form<Person> createForm() {
		return new PersonPanel(PersonEditMode.DISPLAY, echSchema);
	}
	
	private static final int MAX_NAME_LENGTH = 10;
	
	@Override
	public String getTitle() {
		Person person = getObject();
		String title;
		if (person.officialName != null) {
			if (person.officialName.length() <= MAX_NAME_LENGTH) {
				title = person.officialName;
			} else {
				title = person.officialName.substring(0, MAX_NAME_LENGTH-1) + "..";
			}
			if (person.firstName != null) {
				title = title + ", ";
				if (person.firstName.length() <= MAX_NAME_LENGTH) {
					title = title + person.firstName;
				} else {
					title = title + person.firstName.substring(0, MAX_NAME_LENGTH-1) + "..";
				}
			}
		} else {
			title = Resources.getString("Person");
		}
		return title;
	}
	
	public class PersonActionGroup extends ActionGroup {

		private final Action marriage, separation, undoSeparation, divorce, undoMarriage, partnership, undoPartnership;
		private final Action naturalizeSwiss, naturalizeForeigner, undoSwiss, changeCitizen, changeNationality, changeResidencePermit, renewPermit, undoCitizen;
		private final Action changeOccupation;
		private final Action death, missing, undoMissing;
		private final Action care, gardianMeasure, undoGardian, changeGardian;
		private final Action move, moveOut;
		private final Action changeName, changeReligion, changeTypeOfResidence, changeRelation, contact;
		private final Action correctOrigin, correctResidencePermit;
		private final Action addressLock, paperLock;
		private final Action birthChild;
		
		private final List<Action> correctActions = new ArrayList<>();

		public PersonActionGroup() {
			super(Resources.getString("Person"));
			
			marriage = new MarriageEvent(PersonPage.this);
			
			separation = new SeparationEvent(PersonPage.this);
			undoSeparation = new UndoSeparationEvent(PersonPage.this);
			divorce = new DivorceEvent(PersonPage.this);
			undoMarriage = new UndoMarriageEvent(PersonPage.this);
			partnership = new PartnershipEvent(PersonPage.this);
			undoPartnership = new UndoPartnershipEvent(PersonPage.this);

			naturalizeSwiss = new NaturalizeSwissEvent(PersonPage.this);
			naturalizeForeigner = new NaturalizeForeignerEvent(PersonPage.this);
			undoSwiss = new UndoSwissEvent(PersonPage.this);
			changeCitizen = new ChangeCitizenEvent(PersonPage.this);
			changeNationality = new ChangeNationalityEvent(PersonPage.this);
			changeResidencePermit = new ChangeResidencePermitEvent(PersonPage.this);
			renewPermit = new RenewPermitEvent(PersonPage.this);
			undoCitizen = new UndoCitizenEvent(PersonPage.this);

			changeOccupation = new ChangeOccupationEvent(PersonPage.this);

			death = new DeathEvent(PersonPage.this);
			missing = new MissingEvent(PersonPage.this);
			undoMissing = new UndoMissingEvent(PersonPage.this);

			care = new CareEvent(PersonPage.this);
			gardianMeasure = new GardianMeasureEvent(PersonPage.this);
			undoGardian = new UndoGardianEvent(PersonPage.this);
			changeGardian = new ChangeGardianEvent(PersonPage.this);

			move = new MoveEvent(PersonPage.this);
			moveOut = new MoveOutEvent(PersonPage.this);

			changeName = new ChangeNameEvent(PersonPage.this);
			changeReligion = new ChangeReligionEvent(PersonPage.this);
			changeTypeOfResidence = new ChangeTypeOfResidenceEvent(PersonPage.this);
			changeRelation = new CorrectRelationshipEvent(PersonPage.this);
			contact = new ContactEvent(PersonPage.this);

			correctOrigin = new CorrectOriginEvent(PersonPage.this);
			correctResidencePermit = new CorrectResidencePermitEvent(PersonPage.this);

			addressLock = new AddressLockEvent(PersonPage.this);
			paperLock = new PaperLockEvent(PersonPage.this);
			
			OpenEchPreferences preferences = Preferences.getPreferences(OpenEchPreferences.class);
			birthChild = new BirthChildEvent(PersonPage.this, preferences);

			historyPage = new PersonHistoryPage(PersonPage.this);
			
			ActionGroup partnershipGroup = addGroup("ActionGroup.person_partnership");
			partnershipGroup.add(marriage);
			partnershipGroup.add(separation);
			partnershipGroup.add(undoSeparation);
			partnershipGroup.add(divorce);
			partnershipGroup.add(undoMarriage);
			partnershipGroup.addSeparator();
			partnershipGroup.add(partnership);
			partnershipGroup.add(undoPartnership);

			ActionGroup origin = addGroup("ActionGroup.person_nationality");
			origin.add(naturalizeSwiss);
			origin.add(naturalizeForeigner);
			origin.add(undoSwiss);
			origin.add(changeCitizen);
			origin.add(changeNationality);
			origin.add(changeResidencePermit);
			origin.add(renewPermit);
			origin.add(undoCitizen);

			ActionGroup occupation = addGroup("ActionGroup.person_occupation");
			occupation.add(changeOccupation);
			
			ActionGroup moveGroup = addGroup("ActionGroup.person_move");
			moveGroup.add(move);
			moveGroup.add(moveOut);

			ActionGroup deathGroup = addGroup("ActionGroup.person_death");
			deathGroup.add(death);
			deathGroup.add(missing);
			deathGroup.add(undoMissing);

			ActionGroup gardian = addGroup("ActionGroup.person_care");
			gardian.add(care);
			gardian.add(gardianMeasure);
			gardian.add(undoGardian);
			gardian.add(changeGardian);

			ActionGroup lock = addGroup("ActionGroup.person_lock");
			lock.add(addressLock);
			lock.add(paperLock);

			ActionGroup change = addGroup("ActionGroup.person_change");
			change.add(changeName);
			change.add(changeReligion);
			change.add(changeTypeOfResidence);
			change.add(changeRelation);
			change.add(contact);

			fillCorrectionActionList();
			ActionGroup correct = addGroup("ActionGroup.correction");
			for (Action action : correctActions) {
				correct.add(action);
			}

			addSeparator();
			add(birthChild); 
			
			addSeparator();
			addDetail(historyPage); 
			
			refresh();
		}

		private void fillCorrectionActionList() {
			addCorrectAction(new CorrectPersonEvent(PersonPage.this));
			addCorrectAction(new CorrectAddressEvent(PersonPage.this));
			addCorrectAction(new CorrectReportingEvent(PersonPage.this));
			addCorrectAction(new CorrectOccupationEvent(PersonPage.this));

			if (echSchema.additionalCorrectEvents()) {
				addCorrectAction(new CorrectIdentificationEvent(PersonPage.this));
				addCorrectAction(new CorrectNameEvent(PersonPage.this));
				addCorrectAction(new CorrectNationalityEvent(PersonPage.this));
				addCorrectAction(new CorrectContactEvent(PersonPage.this));
				addCorrectAction(new CorrectReligionEvent(PersonPage.this));
				addCorrectAction(correctOrigin);
				addCorrectAction(correctResidencePermit);
				addCorrectAction(new CorrectMaritalDataEvent(PersonPage.this));
				addCorrectAction(new CorrectPlaceOfBirthEvent(PersonPage.this));
				addCorrectAction(new CorrectDateOfDeathEvent(PersonPage.this));
				addCorrectAction(new CorrectLanguageOfCorrespondanceEvent(PersonPage.this));
			}
		}

		private void addCorrectAction(Action action) {
			correctActions.add(action);
		}
		
		@BusinessRule("Welche Aktion in welchem Zustand von Personen ausgeführt werden darf")
		// Could be delegated to the Actions itself
		public void refresh() {
			Person person = PersonPage.this.getObject();
			
			boolean isHistorical = person.version != 0;
			boolean isPerson = person != null && !isHistorical;
			
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
			for (Action action : correctActions) {
				action.setEnabled(isPerson);
			}
			correctOrigin.setEnabled(isPerson && isSwiss);
			correctResidencePermit.setEnabled(isPerson && !isSwiss);
			care.setEnabled(isAlive);
			contact.setEnabled(isPerson);
		}

		@Override
		public ActionGroup addGroup(String name) {
			return super.addGroup(Resources.getString(name));
		}
	}

}
