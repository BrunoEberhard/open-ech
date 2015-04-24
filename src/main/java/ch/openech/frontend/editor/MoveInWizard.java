package ch.openech.frontend.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.minimalj.application.DevMode;
import org.minimalj.frontend.editor.Wizard;
import org.minimalj.frontend.editor.WizardStep;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ClientToolkit.Input;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.CloneHelper;

import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.frontend.ewk.event.PersonEventEditor;
import ch.openech.frontend.ewk.event.moveIn.NextPersonHelper;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.frontend.xmlpreview.XmlPreview;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.model.person.types.TypeOfRelationshipInverted;
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
		public static final MoveInNextPerson $ = Keys.of(MoveInNextPerson.class);
		
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
		public static final MoveInEditorData $ = Keys.of(MoveInEditorData.class);
		
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
	public Action[] getActions() {
		if (DevMode.isActive()) {
			return new Action[]{demoAction, new XmlAction(), cancelAction, prevAction, nextAction, saveAction};
		} else {
			return new Action[]{cancelAction, prevAction, nextAction, saveAction};
		}
	}

	private class MoveInPersonWizardStep extends WizardStep<Person> {

		@Override
		protected Form<Person> createForm() {
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
		protected Form<MoveInNextPerson> createForm() {
			EchForm<MoveInNextPerson> form = new EchForm<MoveInNextPerson>(2);
			
			form.line(MoveInNextPerson.$.typeOfRelationshipInverted);

			form.line(new MoveInNextPersonFormElement(MoveInNextPerson.$.basePerson, TypeOfRelationship.Partner));

			form.line(new MoveInNextPersonFormElement(MoveInNextPerson.$.mother, TypeOfRelationship.Mutter));
			form.line(new MoveInNextPersonFormElement(MoveInNextPerson.$.father, TypeOfRelationship.Vater));

			form.line(new MoveInNextPersonFormElement(MoveInNextPerson.$.fosterMother, TypeOfRelationship.Pflegemutter), //
					new MoveInNextPersonFormElement(MoveInNextPerson.$.fosterFather, TypeOfRelationship.Pflegevater));
			
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
	protected Object save(MoveInEditorData object) {
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
		Object insertId = PersonEventEditor.send(xmlList);
		return insertId;
	}

	private class MoveInNextPersonFormElement extends AbstractFormElement<Person> {

		private final Input<Person> comboBox;
		private final TypeOfRelationship relation;
		
		public MoveInNextPersonFormElement(Object key, TypeOfRelationship relation) {
			this(Keys.getProperty(key), relation);
		}
		
		public MoveInNextPersonFormElement(PropertyInterface property, TypeOfRelationship relation) {
			super(property);
			
			this.relation = relation;
			
			List<Person> possiblePersons = filter(MoveInWizard.this.getObject().persons);
			comboBox = ClientToolkit.getToolkit().createComboBox(possiblePersons, listener());
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
			comboBox.setValue(object);
		}

		@Override
		public Person getObject() {
			return (Person) comboBox.getValue();
		}
		
	}

	private class XmlAction extends Action {
		@Override
		public void action() {
			try {
				List<String> xmls = new ArrayList<String>();
				for (Person person : getObject().persons) {
					xmls.add(getWriterEch0020().moveIn(person));
				}
				XmlPreview.viewXml(xmls);
			} catch (Exception x) {
				ClientToolkit.getToolkit().showMessage("XML Preview fehlgeschlagen");
				Logger.getLogger(XmlAction.class.getName()).log(Level.SEVERE, "XML Preview fehlgeschlagen", x);
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
