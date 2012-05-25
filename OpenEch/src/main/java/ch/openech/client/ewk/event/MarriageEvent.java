package ch.openech.client.ewk.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.openech.client.RemoveEntriesListField;
import ch.openech.client.e44.PersonField;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.EchFormats;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Boolean;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.edit.fields.CheckBoxStringField;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.TextEditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.util.BusinessRule;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class MarriageEvent extends PersonEventEditor<MarriageEvent.Marriage> {

	public MarriageEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}

	public static class Marriage {
		@Required @Date
		public String dateOfMaritalStatus;
		@Boolean
		public String registerPartner2 = "1";
		public Person partner1, partner2;
		@Boolean
		public String changeName1, changeName2;
		@FormatName(EchFormats.baseName)
		public String name1, name2;
		public final List<PlaceOfOrigin> origin1 = new ArrayList<PlaceOfOrigin>();
		public final List<PlaceOfOrigin> origin2 = new ArrayList<PlaceOfOrigin>();
	}
	
	private static final Marriage MARRIAGE = Constants.of(Marriage.class);

	@Override
	protected int getFormColumns() {
		return 2;
	}
	
	@Override
	protected void fillForm(Form<Marriage> formPanel) {
		PersonField partner1 = new PersonField(MARRIAGE.partner1); 
		PersonField partner2 = new PersonField(MARRIAGE.partner2);
	
		NewPersonNameField name1 = new NewPersonNameField(MARRIAGE.name1, MARRIAGE.partner2);
		NewPersonNameField name2 = new NewPersonNameField(MARRIAGE.name2, MARRIAGE.partner1);
	
		RemoveEntriesListField<PlaceOfOrigin> origin1 = new OriginListField(MARRIAGE.origin1, MARRIAGE.partner2);
		RemoveEntriesListField<PlaceOfOrigin> origin2 = new OriginListField(MARRIAGE.origin2, MARRIAGE.partner1);
		
		//
		
		formPanel.line(MARRIAGE.dateOfMaritalStatus, MARRIAGE.registerPartner2);
		
		formPanel.area(partner1, partner2);
	
		formPanel.line(MARRIAGE.changeName1, MARRIAGE.changeName2);
		formPanel.line(name1, name2);
		
		formPanel.area(origin1, origin2);
	}

	private static class OriginListField extends RemoveEntriesListField<PlaceOfOrigin> implements DependingOnFieldAbove<Person> {

		private final String dependingOnFieldName;
		
		public OriginListField(Object key, Object dependingOnFieldKey) {
			super(key);
			this.dependingOnFieldName = Constants.getConstant(dependingOnFieldKey);
		}

		@Override
		public String getNameOfDependedField() {
			return dependingOnFieldName;
		}

		@Override
		public void setDependedField(EditField<Person> field) {
			Person person = field.getObject();
			List<PlaceOfOrigin> origins;
			if (person != null) {
				origins = person.placeOfOrigin;
			} else {
				origins = Collections.emptyList();
			}
			setValues(origins);
			if (getObject() != null) {
				getObject().clear();
				getObject().addAll(origins);
				fireObjectChange();
			}
		}
	}
	
	private static class NewPersonNameField extends TextEditField implements DependingOnFieldAbove<Person> {

		private final String dependingOnFieldName;
		
		public NewPersonNameField(Object key, Object dependingOnFieldKey) {
			super(Constants.getConstant(key), Marriage.class);
			this.dependingOnFieldName = Constants.getConstant(dependingOnFieldKey);
		}

		@Override
		public String getNameOfDependedField() {
			return dependingOnFieldName;
		}

		@Override
		public void setDependedField(EditField<Person> field) {
			Person person = field.getObject();
			if (person != null) {
				setObject(person.personIdentification.officialName);
//						name = person.alliancePartnershipName;
			}
		}
	}
	
	@Override
	public Marriage load() {
		Marriage marriageActionData = new Marriage();
		marriageActionData.partner1 = getPerson();
		marriageActionData.origin2.addAll(getPerson().placeOfOrigin);
		return marriageActionData;
	}

	@Override
	protected List<String> getXml(Person person, Marriage data, WriterEch0020 writerEch0020) throws Exception {
		List<String> xmls = new ArrayList<String>();

		// Die Reihenfolge der Events ist wichtig, da nach einer Namensänderung
		// die ursprüngliche Bezeichnung des Partners schon nicht mehr die aktuelle ist
		xmls.add(marriage(writerEch0020, data.dateOfMaritalStatus, data.partner1.personIdentification, data.partner2.personIdentification));
		if (CheckBoxStringField.isTrue(data.registerPartner2)) {
			xmls.add(marriage(writerEch0020, data.dateOfMaritalStatus, data.partner2.personIdentification, data.partner1.personIdentification));
		}

		// Die Einbürgerungen müssen vor dem Namenswechsel erfolgen, sonst passen
		// eventuell die PersonenIdentifikationen nicht mehr
		String naturalizationDate = data.dateOfMaritalStatus;
		naturalizeSwiss(writerEch0020, data.partner1, data.origin1, naturalizationDate, xmls);
		naturalizeSwiss(writerEch0020, data.partner2, data.origin2, naturalizationDate, xmls);
		
		if (CheckBoxStringField.isTrue(data.changeName1)) {
			xmls.add(changeName(writerEch0020, person, data.name1));
		}
		if (CheckBoxStringField.isTrue(data.changeName2) && data.partner2.isPersisent()) {
			xmls.add(changeName(writerEch0020, data.partner2, data.name2));
		}

		return xmls;
	}
	
	private String marriage(WriterEch0020 writerEch0020, String dateOfMaritalStatus, PersonIdentification partner1, PersonIdentification partner2) throws Exception {
		Relation relation = new Relation();
		relation.typeOfRelationship = "1";
		relation.partner = partner2;
		return writerEch0020.marriage(partner1, relation, dateOfMaritalStatus);
	}
	
	private String changeName(WriterEch0020 writerEch0020, Person person, String officialName) throws Exception {
		Person personToChange = CloneHelper.clone(person);
		personToChange.personIdentification.officialName = officialName;
		return writerEch0020.changeName(person.personIdentification, personToChange);
	}

	private void naturalizeSwiss(WriterEch0020 writerEch0020, Person person, List<PlaceOfOrigin> list, String naturalizationDate, List<String> xmls) throws Exception {
		for (PlaceOfOrigin existingOrigin : list) {
			PlaceOfOrigin origin = new PlaceOfOrigin();
			origin.originName = existingOrigin.originName;
			origin.canton = existingOrigin.canton;
			origin.reasonOfAcquisition = "2"; // Heirat
			origin.naturalizationDate = naturalizationDate;
			xmls.add(writerEch0020.naturalizeSwiss(person.personIdentification, origin));
		}
	}

	@Override
	public void validate(Marriage data, List<ValidationMessage> resultList) {
		validate(resultList, data.partner1, data.partner2, true);
		validateNamesNotBlank(data, resultList);
	}
	
	static void validate(List<ValidationMessage> validationMessages, Person person1, Person person2, boolean marriage) {
		validatePartnerAlive(validationMessages, "partner1", person1);
		validatePartnerAlive(validationMessages, "partner2", person2);
		validatePartnerSet(validationMessages, "partner1", person1);
		validatePartnerSet(validationMessages, "partner2", person2);
		validateSex(validationMessages, person1, person2, marriage);
	}
	
	private void validateNamesNotBlank(Marriage data, List<ValidationMessage> validationMessages) {
		if (CheckBoxStringField.isTrue(data.changeName1)) validateNameNotBlank(validationMessages, "name1", data.name1);
		if (CheckBoxStringField.isTrue(data.changeName2)) validateNameNotBlank(validationMessages, "name2", data.name2);
	}
	
	@BusinessRule("2 Eheleute/Partner für Ehe notwendig")
	static void validatePartnerSet(List<ValidationMessage> validationMessages, String key, Person person) {
		if (person == null) {
			validationMessages.add(new ValidationMessage(key, "Person fehlt"));
		}
	}
	
	@BusinessRule("Ehepartner/Partner müssen leben")
	static void validatePartnerAlive(List<ValidationMessage> validationMessages, String key, Person person) {
		if (person == null) return;
		if (!StringUtils.isBlank(person.dateOfDeath)) {
			validationMessages.add(new ValidationMessage(key, "Person darf nicht tot sein"));
		}
	}
	
	@BusinessRule("Eheleute müssen unterschiedlichen Geschlechts sein, Partner gleichen Geschlechts")
	static void validateSex(List<ValidationMessage> validationMessages, Person person1, Person person2, boolean marriage) {
		if (person1 == null || person2 == null) return;
		if (StringUtils.isBlank(person1.personIdentification.sex) || StringUtils.isBlank(person2.personIdentification.sex)) return;
		if (marriage) {
			if (StringUtils.equals(person1.personIdentification.sex, person2.personIdentification.sex)) {
				validationMessages.add(new ValidationMessage("partner2", "Eheleute müssen unterschiedlichen Geschlechts sein"));
			}
		} else {
			if (!StringUtils.equals(person1.personIdentification.sex, person2.personIdentification.sex)) {
				validationMessages.add(new ValidationMessage("partner2", "Partener müssen gleichen Geschlechts sein"));
			}
		}
	}

	@BusinessRule("Geänderter Name bei Ehe, Allianzname bei Partnerschaft darf nicht leer sein, sofern er geändert wird")
	static void validateNameNotBlank(List<ValidationMessage> validationMessages, String key, String name) {
		if (StringUtils.isBlank(name)) {
			validationMessages.add(new ValidationMessage(key, "Name kann nicht leer sein"));
		}
	}

}
