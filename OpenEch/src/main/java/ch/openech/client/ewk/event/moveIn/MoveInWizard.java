package ch.openech.client.ewk.event.moveIn;

import java.util.ArrayList;
import java.util.List;

import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.client.ewk.XmlEditor;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.code.TypeOfRelationship;
import ch.openech.dm.code.TypeOfRelationshipInverted;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.Wizard;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;


public class MoveInWizard extends Wizard<MoveInWizard.MoveInEditorData> {

	private final EchNamespaceContext echNamespaceContext;
	private final Person person;
	private final MoveInEditorData data;
	
	public MoveInWizard(String version) {
		this(EchNamespaceContext.getNamespaceContext(20, version));
	}
	
	public MoveInWizard(EchNamespaceContext echNamespaceContext) {
		this(echNamespaceContext, null);
	}
	
	public MoveInWizard(EchNamespaceContext echNamespaceContext, Person person) {
		this.echNamespaceContext = echNamespaceContext;
		this.person = person;
		this.data = new MoveInEditorData();
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

	protected WriterEch0020 getWriterEch0020() {
		return echNamespaceContext.getWriterEch0020();
	}
	
	public void generateSedexOutput(Person person) throws Exception {
//		if (person.comesFrom != null && !person.comesFrom.municipalityIdentification.isEmpty()) {
//			WriterEch0093 sedexWriter = new WriterEch0093(getEchNamespaceContext());
//			sedexWriter.setRecepientMunicipality(person.comesFrom.municipalityIdentification);
//			String sedexOutput = sedexWriter.moveIn(person);
//			SedexOutputGenerator.generateSedex(sedexOutput, sedexWriter.getEnvelope());
//		}
	}

	@Override
	protected MoveInEditorData load() {
		return data;
	}

	@Override
	protected MoveInEditorData getObject() {
		return data;
	}

	@Override
	protected Editor<?> getNextEditor() {
		if (data.persons.isEmpty()) {
			return new MoveInWizardEditor(new Person());
		} else if (data.persons.size() == data.nextPersons.size()) {
			return new MoveInWizardEditor(createNextPerson(data.nextPersons.get(data.nextPersons.size()-1)));
		} else {
			return new MoveInNextPersonEditor();
		}
	}

	private Person createNextPerson(MoveInNextPerson moveInNextPerson) {
		TypeOfRelationshipInverted relation = moveInNextPerson.typeOfRelationshipInverted();
		switch (relation) {
		case Partner:
			return createNextPersonPartner(moveInNextPerson.basePerson, true);
		case AllianzPartner:
			return createNextPersonPartner(moveInNextPerson.basePerson, false);
		case Kind:
			return createNextPersonChild(moveInNextPerson);
		default:
			return createPersonWithoutRelation(moveInNextPerson.basePerson);
		}
	}
	
	@BusinessRule("Beim Zuzug werden bei Eingabe einer weiteren Person Daten von den früheren Personen übernommen (Partner)")
	private static Person createNextPersonPartner(Person partner, boolean married) {
		Person nextPerson = new Person();
		Relation relation = new Relation();
		Relation relation2 = new Relation();
	
		if (married) {
			nextPerson.personIdentification.officialName = partner.personIdentification.officialName;
			boolean partnerIsMale = "1".equals(partner.personIdentification.sex);
			nextPerson.personIdentification.sex = partnerIsMale ? "2" : "1";
			relation.typeOfRelationship = relation2.typeOfRelationship = "1";
		} else {
			nextPerson.alliancePartnershipName = partner.alliancePartnershipName;
			nextPerson.personIdentification.sex = partner.personIdentification.sex;
			relation.typeOfRelationship = relation2.typeOfRelationship = "2";
		}
		nextPerson.maritalStatus.maritalStatus = partner.maritalStatus.maritalStatus;
		nextPerson.religion = partner.religion;
		nextPerson.typeOfResidence = partner.typeOfResidence;
		nextPerson.residence.reportingMunicipality = partner.residence.reportingMunicipality;
		nextPerson.residence.setSecondary(partner.residence.secondary);
		nextPerson.arrivalDate = partner.arrivalDate;
		nextPerson.comesFrom = partner.comesFrom;
		nextPerson.dwellingAddress = partner.dwellingAddress;
	
		// Folgendes würde nichts nützen, da das Panel später vor dem
		// Speichern ein anderes Objekt zurückgibt
		// relation.partner = person;
		partner.relation.add(relation);
	
		relation2.partner = partner.personIdentification;
		nextPerson.relation.add(relation2);
		return nextPerson;
	}

	@BusinessRule("Beim Zuzug werden bei Eingabe einer weiteren Person Daten von den früheren Personen übernommen (Kind)")
	public static Person createNextPersonChild(MoveInNextPerson moveInNextPerson) {
		Person child = new Person();
		Person basePerson = moveInNextPerson.basePerson;

		child.personIdentification.officialName = basePerson.personIdentification.officialName;
		child.religion = basePerson.religion;
		child.typeOfResidence = basePerson.typeOfResidence;
		child.residence.reportingMunicipality = basePerson.residence.reportingMunicipality;
		child.residence.setSecondary(basePerson.residence.secondary);
		child.arrivalDate = basePerson.arrivalDate;
		child.comesFrom = basePerson.comesFrom;
		child.dwellingAddress = basePerson.dwellingAddress;

		if (moveInNextPerson.mother != null) {
			child.relation.add(createChildRelation(TypeOfRelationship.Mutter, moveInNextPerson.mother));
		}
		if (moveInNextPerson.father != null) {
			child.relation.add(createChildRelation(TypeOfRelationship.Vater, moveInNextPerson.father));
		}
		if (moveInNextPerson.fosterMother != null) {
			child.relation.add(createChildRelation(TypeOfRelationship.Pflegemutter, moveInNextPerson.fosterMother));
		}
		if (moveInNextPerson.fosterFather != null) {
			child.relation.add(createChildRelation(TypeOfRelationship.Pflegevater, moveInNextPerson.fosterFather));
		}
		
		return child;
	}

	public static Relation createChildRelation(TypeOfRelationship typeOfRelationship, Person person) {
		Relation relation = new Relation();
		relation.typeOfRelationship = typeOfRelationship.getKey();
		relation.partner = person.personIdentification;
		return relation;
	}
	
	@BusinessRule("Beim Zuzug werden bei Eingabe einer weiteren Person Daten von den früheren Personen übernommen (Person ohne spezielle Beziehung)")
	public static Person createPersonWithoutRelation(Person person) {
		Person nextPerson = new Person();
		nextPerson.religion = person.religion;
		nextPerson.typeOfResidence = person.typeOfResidence;
		nextPerson.residence.reportingMunicipality = person.residence.reportingMunicipality;
		nextPerson.residence.setSecondary(person.residence.secondary);
		nextPerson.arrivalDate = person.arrivalDate;
		nextPerson.comesFrom = person.comesFrom;
		nextPerson.dwellingAddress = person.dwellingAddress;
		return nextPerson;
	}

	@Override
	protected Editor<?> getPrevEditor() {
		return new MoveInWizardEditor(new Person());
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
		return true;
	}

	private class MoveInWizardEditor extends Editor<Person> {

		private final Person person;
		
		public MoveInWizardEditor(Person person) {
			this.person = person;
		}
		
		@Override
		protected FormVisual<Person> createForm() {
			return new PersonPanel(PersonPanelType.MOVE_IN, echNamespaceContext);
		}

		@Override
		protected Person load() {
			return person;
		}

		@Override
		protected void validate(Person object, List<ValidationMessage> resultList) {
			// noting special to validate
		}

		@Override
		protected boolean save(Person person) {
			MoveInWizard.this.getObject().persons.add(person);
			return true;
		}
		
	}

	private class MoveInNextPersonEditor extends Editor<MoveInNextPerson> {

		@Override
		protected FormVisual<MoveInNextPerson> createForm() {
			EchFormPanel<MoveInNextPerson> form = new EchFormPanel<MoveInNextPerson>(MoveInNextPerson.class, 2);
			
			form.line(MoveInNextPerson.MOVE_IN_NEXT_PERSON.typeOfRelationshipInverted);

			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.basePerson, TypeOfRelationship.Partner));

			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.mother, TypeOfRelationship.Mutter));
			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.father, TypeOfRelationship.Vater));

			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.fosterMother, TypeOfRelationship.Pflegemutter));
			form.line(new MoveInNextPersonField(MoveInNextPerson.MOVE_IN_NEXT_PERSON.fosterFather, TypeOfRelationship.Pflegevater));

			
			return form;
		}

		@Override
		protected MoveInNextPerson load() {
			return new MoveInNextPerson();
		}

		@Override
		protected void validate(MoveInNextPerson object, List<ValidationMessage> resultList) {
			// noting special to validate
		}

		@Override
		protected boolean save(MoveInNextPerson object) {
			MoveInWizard.this.getObject().nextPersons.add(object);
			return true;
		}
		
	}
	
	private class MoveInNextPersonField extends AbstractEditField<Person> implements DependingOnFieldAbove<String> {

		private final ComboBox comboBox;
		private final TypeOfRelationship relation;
		
		public MoveInNextPersonField(Object key, TypeOfRelationship relation) {
			super(key);
			
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
		public IComponent getComponent0() {
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
}