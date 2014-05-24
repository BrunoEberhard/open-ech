package ch.openech.frontend.editor;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.application.DevMode;
import org.minimalj.frontend.edit.Wizard;
import org.minimalj.frontend.edit.WizardStep;
import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.edit.form.IForm;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.IAction;
import org.minimalj.frontend.toolkit.IComponent;
import org.minimalj.model.Keys;
import org.minimalj.model.PropertyInterface;
import org.minimalj.model.annotation.Required;
import org.minimalj.util.CloneHelper;

import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.frontend.ewk.event.PersonEventEditor;
import ch.openech.frontend.ewk.event.moveIn.NextPersonHelper;
import ch.openech.frontend.page.PersonViewPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.frontend.xmlpreview.XmlPreview;
import  ch.openech.model.person.Person;
import  ch.openech.model.person.PersonEditMode;
import  ch.openech.model.person.types.TypeOfRelationship;
import  ch.openech.model.person.types.TypeOfRelationshipInverted;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;


public class MoveInWizard extends Wizard<MoveInWizard.MoveInEditorData> {

	private final EchSchema echNamespaceContext;
	private final OpenEchPreferences preferences;
	private final MoveInPersonWizardStep moveInPersonWizardStep;
	private final MoveInNextPersonWizardStep moveInNextPersonWizardStep;
	
	public MoveInWizard(EchSchema ech, OpenEchPreferences preferences) {
		this.echNamespaceContext = ech;
		this.preferences = preferences;
		this.moveInPersonWizardStep = new MoveInPersonWizardStep();
		this.moveInNextPersonWizardStep = new MoveInNextPersonWizardStep();
	}

	public static class MoveInNextPerson {
		public static final MoveInNextPerson MOVE_IN_NEXT_PERSON = Keys.of(MoveInNextPerson.class);
		
		@Required
		public TypeOfRelationshipInverted typeOfRelationshipInverted = TypeOfRelationshipInverted.Kind;
		@Required // @Depends("typeOfRelationshipInverted")
		public Person basePerson;
		// @Depends("typeOfRelationshipInverted")
		public Person mother, father;
		// @Depends("typeOfRelationshipInverted")
		public Person fosterMother, fosterFather;
	}
		
	public static class MoveInEditorData {
		public static final MoveInEditorData MOVE_IN_EDITOR_DATA = Keys.of(MoveInEditorData.class);
		
		public final List<Person> persons = new ArrayList<Person>();
		public final List<MoveInNextPerson> nextPersons = new ArrayList<MoveInWizard.MoveInNextPerson>();
	}

	@Override
	protected WizardStep<?> getFirstStep() {
		return moveInPersonWizardStep;
	}
	
	private int getPersonIndex() {
		return getCurrentStepIndex() / 2;
	}
	
	protected WriterEch0020 getWriterEch0020() {
		return echNamespaceContext.getWriterEch0020();
	}
	
	public void generateSedexOutput(Person person) throws Exception {
//		if (person.comesFrom != null && !person.comesFrom.municipalityIdentification.isEmpty()) {
//			WriterEch0093 sedexWriter = new WriterEch0093(echSchema);
//			sedexWriter.setRecepientMunicipality(person.comesFrom.municipalityIdentification);
//			String sedexOutput = sedexWriter.moveIn(person);
//			SedexOutputGenerator.generateSedex(sedexOutput, sedexWriter.getEnvelope());
//		}
	}

	@Override
	public IAction[] getActions() {
		if (DevMode.isActive()) {
			return new IAction[]{demoAction, new XmlAction(), cancelAction, prevAction, nextAction, saveAction};
		} else {
			return new IAction[]{cancelAction, prevAction, nextAction, saveAction};
		}
	}

	private class MoveInPersonWizardStep extends WizardStep<Person> {

		@Override
		protected IForm<Person> createForm() {
			return new PersonPanel(PersonEditMode.MOVE_IN, echNamespaceContext);
		}

		@Override
		public String getTitle() {
			return "Person eingeben";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public String getMessage() {
			return null;
		}

		@Override
		public boolean canFinish() {
			return true;
		}

		@Override
		public WizardStep<?> getNextStep() {
			return moveInNextPersonWizardStep;
		}

		@Override
		public WizardStep<?> getPreviousStep() {
			return moveInNextPersonWizardStep;
		}
		
		@Override
		protected Person save(Person person) {
			return person;
		}

		@Override
		protected Person newInstance() {
			MoveInEditorData wizardData = MoveInWizard.this.getObject();
			int personIndex = getPersonIndex();
			if (wizardData.persons.size() <= personIndex) {
				Person person;
				if (personIndex > 0) {
					person = createNextPerson(wizardData.nextPersons.get(personIndex-1));
				} else {
					person = new Person();
					person.languageOfCorrespondance = preferences.preferencesDefaultsData.language;
					person.religion = preferences.preferencesDefaultsData.religion;
					person.residence.reportingMunicipality = CloneHelper.clone(preferences.preferencesDefaultsData.residence);
				}
				wizardData.persons.add(person);
			}
			return wizardData.persons.get(personIndex);
		}

		@Override
		public void fillWithDemoData() {
			// TODO Auto-generated method stub
			super.fillWithDemoData();
		}
	}
	
	private class MoveInNextPersonWizardStep extends WizardStep<MoveInNextPerson> {

		@Override
		protected IForm<MoveInNextPerson> createForm() {
			EchForm<MoveInNextPerson> form = new EchForm<MoveInNextPerson>(2);
			
			form.line(MoveInNextPerson.MOVE_IN_NEXT_PERSON.typeOfRelationshipInverted);

			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.basePerson, TypeOfRelationship.Partner));

			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.mother, TypeOfRelationship.Mutter));
			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.father, TypeOfRelationship.Vater));

			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.fosterMother, TypeOfRelationship.Pflegemutter), //
					new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.fosterFather, TypeOfRelationship.Pflegevater));
			
			return form;
		}

		@Override
		public String getTitle() {
			return "Weitere Person erfassen";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public String getMessage() {
			return null;
		}

		@Override
		public boolean canFinish() {
			return false;
		}

		@Override
		protected MoveInNextPerson newInstance() {
			MoveInEditorData wizardData = MoveInWizard.this.getObject();
			int personIndex = getPersonIndex();
			if (wizardData.nextPersons.size() <= personIndex) {
				MoveInNextPerson moveInNextPerson = new MoveInNextPerson();
				moveInNextPerson.basePerson = wizardData.persons.get(personIndex);
				wizardData.nextPersons.add(moveInNextPerson);
			}
			return wizardData.nextPersons.get(personIndex);
		}

		@Override
		protected Object save(MoveInNextPerson object) {
			return SAVE_SUCCESSFUL;
		}

		@Override
		public WizardStep<?> getNextStep() {
			return moveInPersonWizardStep;
		}

		@Override
		public WizardStep<?> getPreviousStep() {
			return moveInPersonWizardStep;
		}
	}
	
	private Person createNextPerson(MoveInNextPerson moveInNextPerson) {
		TypeOfRelationshipInverted relation = moveInNextPerson.typeOfRelationshipInverted;
		switch (relation) {
		case Partner:
			return NextPersonHelper.createNextPersonPartner(moveInNextPerson.basePerson, true);
		case AllianzPartner:
			return NextPersonHelper.createNextPersonPartner(moveInNextPerson.basePerson, false);
		case Kind:
			return NextPersonHelper.createNextPersonChild(moveInNextPerson);
		default:
			return NextPersonHelper.createPersonWithoutRelation(moveInNextPerson.basePerson);
		}
	}

	@Override
	protected String save(MoveInEditorData object) {
		// Personen, die mit man previous verlassen hat werden nicht mitgespeichert,
		// da sie eventuell ungültig sein könnten
		for (int i = object.persons.size() - 1; i > getPersonIndex(); i--) {
			object.persons.remove(i);
		}
		
		List<String> xmlList = new ArrayList<String>();
		for (Person person : object.persons) {
			try {
				xmlList.add(getWriterEch0020().moveIn(person));
			} catch (Exception e) {
				// TODO
				e.printStackTrace();
			}
		}
		Person person = PersonEventEditor.send(xmlList);
		return PageLink.link(PersonViewPage.class, echNamespaceContext.getVersion(), person.getId());
	}


	private class MoveInNextPersonField extends AbstractEditField<Person> {

		private final ComboBox<Person> comboBox;
		private final TypeOfRelationship relation;
		
		public MoveInNextPersonField(Object key, TypeOfRelationship relation) {
			this(Keys.getProperty(key), relation);
		}
		
		public MoveInNextPersonField(PropertyInterface property, TypeOfRelationship relation) {
			super(property, true);
			
			this.relation = relation;
			
			comboBox = ClientToolkit.getToolkit().createComboBox(listener());
			List<Person> possiblePersons = filter(MoveInWizard.this.getObject().persons);
			comboBox.setObjects(possiblePersons);
		}
		
		private List<Person> filter(List<Person> persons) {
			List<Person> filteredPersons = new ArrayList<Person>();
			for (Person person : persons) {
				boolean possible = true;
				switch (relation) {
				case Mutter:
				case Pflegemutter:
					possible = person.isFemale();
					break;
				case Vater:
				case Pflegevater:
					possible = person.isMale();
					break;
				default:
					possible = true;
					break;
				}
				if (possible) {
					filteredPersons.add(person);
				}
			}
			return filteredPersons;
		}

		@Override
		public IComponent getComponent() {
			return comboBox;
		}

		@Override
		public void setObject(Person object) {
			comboBox.setSelectedObject(object);
		}

		@Override
		public Person getObject() {
			return (Person) comboBox.getSelectedObject();
		}
		
	}

	private class XmlAction implements IAction {
		@Override
		public void action(IComponent context) {
			try {
				List<String> xmls = new ArrayList<String>();
				for (Person person : getObject().persons) {
					xmls.add(getWriterEch0020().moveIn(person));
				}
				XmlPreview.viewXml(context, xmls);
			} catch (Exception x) {
				throw new RuntimeException("XML Preview fehlgeschlagen", x);
			}
		}

		@Override
		public String getName() {
			return "Vorschau XML";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public void setChangeListener(ActionChangeListener changeListener) {
			// doesn't change
		}
	}

}