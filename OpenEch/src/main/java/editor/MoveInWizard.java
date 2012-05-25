package editor;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import page.PersonViewPage;

import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.client.ewk.event.moveIn.NextPersonHelper;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.client.xmlpreview.XmlPreview;
import ch.openech.dm.code.TypeOfRelationship;
import ch.openech.dm.code.TypeOfRelationshipInverted;
import ch.openech.dm.person.Person;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.Wizard;
import ch.openech.mj.edit.WizardPage;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;


public class MoveInWizard extends Wizard<MoveInWizard.MoveInEditorData> {

	private final PageContext context;
	private final EchSchema echNamespaceContext;
	private final MoveInPersonWizardPage moveInPersonWizardPage;
	private final MoveInNextPersonWizardPage moveInNextPersonWizardPage;
	private int personIndex; // merge with currentPageIndex?
	
	public MoveInWizard(PageContext context, String version) {
		this.context = context;
		this.echNamespaceContext = EchSchema.getNamespaceContext(20, version);
		this.moveInPersonWizardPage = new MoveInPersonWizardPage();
		this.moveInNextPersonWizardPage = new MoveInNextPersonWizardPage();
	}

	public static class MoveInNextPerson {
		public static final MoveInNextPerson MOVE_IN_NEXT_PERSON = Constants.of(MoveInNextPerson.class);
		
		public String typeOfRelationshipInverted = TypeOfRelationshipInverted.Kind.getKey();
		public Person basePerson;
		public Person mother, father;
		public Person fosterMother, fosterFather;
		
		public TypeOfRelationshipInverted typeOfRelationshipInverted() {
			return TypeOfRelationshipInverted.lookup(typeOfRelationshipInverted);
		}
	}
		
	public static class MoveInEditorData {
		public static final MoveInEditorData MOVE_IN_EDITOR_DATA = Constants.of(MoveInEditorData.class);
		
		public final List<Person> persons = new ArrayList<Person>();
		public final List<MoveInNextPerson> nextPersons = new ArrayList<MoveInWizard.MoveInNextPerson>();
	}

	@Override
	protected WizardPage<?> getFirstPage() {
		return moveInPersonWizardPage;
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
	protected MoveInEditorData load() {
		// Editor should use newInstance
		return null;
	}
	
	@Override
	public Action[] getActions() {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		if (preferences.devMode()) {
			return new Action[]{demoAction(), new XmlAction(), cancelAction(), prevAction, nextAction, saveAction()};
		} else {
			return new Action[]{cancelAction(), prevAction, nextAction, saveAction()};
		}
	}

	private class MoveInPersonWizardPage extends WizardPage<Person> {

		@Override
		protected IForm<Person> createForm() {
			return new PersonPanel(PersonPanelType.MOVE_IN, echNamespaceContext);
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
		public WizardPage<?> getNextPage() {
			return moveInNextPersonWizardPage;
		}

		@Override
		public WizardPage<?> getPreviousPage() {
			personIndex = personIndex - 1;
			return moveInNextPersonWizardPage;
		}
		
		@Override
		protected boolean save(Person person) {
			return true;
		}

		@Override
		protected Person newInstance() {
			MoveInEditorData wizardData = MoveInWizard.this.getObject();
			if (wizardData.persons.size() <= personIndex) {
				Person person;
				if (personIndex > 0) {
					person = createNextPerson(wizardData.nextPersons.get(personIndex-1));
				} else {
					person = new Person();
					OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
					person.languageOfCorrespondance = preferences.preferencesDefaultsData.language;
					person.religion = preferences.preferencesDefaultsData.religion;
					person.residence.reportingMunicipality = CloneHelper.clone(preferences.preferencesDefaultsData.residence);
				}
				wizardData.persons.add(person);
			}
			return wizardData.persons.get(personIndex);
		}
	}
	
	private class MoveInNextPersonWizardPage extends WizardPage<MoveInNextPerson> {

		@Override
		protected IForm<MoveInNextPerson> createForm() {
			EchFormPanel<MoveInNextPerson> form = new EchFormPanel<MoveInNextPerson>(MoveInNextPerson.class, 2);
			
			form.line(MoveInNextPerson.MOVE_IN_NEXT_PERSON.typeOfRelationshipInverted);

			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.basePerson, TypeOfRelationship.Partner));

			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.mother, TypeOfRelationship.Mutter));
			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.father, TypeOfRelationship.Vater));

			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.fosterMother, TypeOfRelationship.Pflegemutter), //
					new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.fosterFather, TypeOfRelationship.Pflegevater));

			form.setRequired(MoveInNextPerson.MOVE_IN_NEXT_PERSON.typeOfRelationshipInverted);
			form.setRequired(MoveInNextPerson.MOVE_IN_NEXT_PERSON.basePerson);
			
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
			if (wizardData.nextPersons.size() <= personIndex) {
				MoveInNextPerson moveInNextPerson = new MoveInNextPerson();
				moveInNextPerson.basePerson = wizardData.persons.get(personIndex);
				wizardData.nextPersons.add(moveInNextPerson);
			}
			return wizardData.nextPersons.get(personIndex);
		}

		@Override
		protected boolean save(MoveInNextPerson object) {
			return true;
		}

		@Override
		public WizardPage<?> getNextPage() {
			personIndex = personIndex + 1;
			return moveInPersonWizardPage;
		}

		@Override
		public WizardPage<?> getPreviousPage() {
			return moveInPersonWizardPage;
		}
	}
	
	private Person createNextPerson(MoveInNextPerson moveInNextPerson) {
		TypeOfRelationshipInverted relation = moveInNextPerson.typeOfRelationshipInverted();
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
	protected boolean save(MoveInEditorData object) {
		List<String> xmlList = new ArrayList<String>();
		for (Person person : object.persons) {
			try {
				xmlList.add(getWriterEch0020().moveIn(person));
			} catch (Exception e) {
				// TODO
				e.printStackTrace();
			}
		}
		XmlEditor.send(xmlList);
		setFollowLink(Page.link(PersonViewPage.class, echNamespaceContext.getVersion(), object.persons.get(0).getId()));
		return true;
	}


	private class MoveInNextPersonField extends AbstractEditField<Person> implements DependingOnFieldAbove<String> {

		private final ComboBox<Person> comboBox;
		private final TypeOfRelationship relation;
		
		public MoveInNextPersonField(Object key, TypeOfRelationship relation) {
			super(key, true);
			
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

		@Override
		public String getNameOfDependedField() {
			return MoveInNextPerson.MOVE_IN_NEXT_PERSON.typeOfRelationshipInverted;
		}

		@Override
		public void setDependedField(EditField<String> field) {
			// TODO enable/disable der Felder
		}
		
	}

	private class XmlAction extends AbstractAction {
		public XmlAction() {
			super("Vorschau XML");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
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
	}

}