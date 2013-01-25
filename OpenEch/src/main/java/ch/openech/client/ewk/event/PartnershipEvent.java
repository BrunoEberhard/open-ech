package ch.openech.client.ewk.event;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import ch.openech.client.e44.PersonField;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.EchFormats;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.Validation;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.util.BusinessRule;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;


// Ähnlich Marriage, aber die Heimatorte werden nicht ausgewechselt und
// "nur" der Allianzname nicht der "richtige" Name
public class PartnershipEvent extends PersonEventEditor<PartnershipEvent.Partnership> {

	public PartnershipEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}
		
	public static class Partnership implements Validation {
		public static final Partnership PARTNERSHIP = Keys.of(Partnership.class);
		
		@Required
		public LocalDate dateOfMaritalStatus;
		public Boolean registerPartner2 = Boolean.TRUE;
		public Person partner1, partner2;
		public Boolean changeName1, changeName2;
		@Size(EchFormats.baseName)
		public String name1, name2;
		
		private void validateNamesNotBlank(List<ValidationMessage> validationMessages) {
			if (Boolean.TRUE.equals(changeName1)) validateNameNotBlank(validationMessages, PARTNERSHIP.name1, name1);
			if (Boolean.TRUE.equals(changeName2)) validateNameNotBlank(validationMessages, PARTNERSHIP.name2, name2);
		}
		
		@Override
		public void validate(List<ValidationMessage> resultList) {
			validate(resultList, partner1, partner2);
			validateNamesNotBlank(resultList);
		}
		
		private static void validate(List<ValidationMessage> validationMessages, Person person1, Person person2) {
			validatePartnerAlive(validationMessages, PARTNERSHIP.partner1, person1);
			validatePartnerAlive(validationMessages, PARTNERSHIP.partner2, person2);
			validatePartnerSet(validationMessages, PARTNERSHIP.partner1, person1);
			validatePartnerSet(validationMessages, PARTNERSHIP.partner2, person2);
			validateSex(validationMessages, person1, person2);
		}
		
		@BusinessRule("2 Partner für notwendig")
		private static void validatePartnerSet(List<ValidationMessage> validationMessages, Person key, Person person) {
			if (person == null) {
				validationMessages.add(new ValidationMessage(key, "Person fehlt"));
			}
		}
		
		@BusinessRule("Partner müssen leben")
		private static void validatePartnerAlive(List<ValidationMessage> validationMessages, Person key, Person person) {
			if (person == null) return;
			if (person.dateOfDeath != null) {
				validationMessages.add(new ValidationMessage(key, "Person darf nicht tot sein"));
			}
		}
		
		@BusinessRule("Partner müssen gleichen Geschlechts sein")
		private static void validateSex(List<ValidationMessage> validationMessages, Person person1, Person person2) {
			if (person1 == null || person2 == null) return;
			if (person1.personIdentification.sex == null || person2.personIdentification.sex == null) return;
			if (person1.personIdentification.sex != person2.personIdentification.sex) {
				validationMessages.add(new ValidationMessage(PartnershipEvent.Partnership.PARTNERSHIP.partner2, "Partner müssen gleichen Geschlechts sein"));
			}
		}

		@BusinessRule("Geänderter Name bei Ehe, Allianzname bei Partnerschaft darf nicht leer sein, sofern er geändert wird")
		private static void validateNameNotBlank(List<ValidationMessage> validationMessages, String key, String name) {
			if (StringUtils.isBlank(name)) {
				validationMessages.add(new ValidationMessage(key, "Name kann nicht leer sein"));
			}
		}

	}
	
	private static final Partnership PARTNERSHIP = Keys.of(Partnership.class);

	@Override
	protected int getFormColumns() {
		return 2;
	}
	
	@Override
	protected void fillForm(Form<Partnership> formPanel) {
		PersonField partner1 = new PersonField(PARTNERSHIP.partner1); 
		PersonField partner2 = new PersonField(PARTNERSHIP.partner2);
		
		//
		
		formPanel.line(PARTNERSHIP.dateOfMaritalStatus, PARTNERSHIP.registerPartner2);
		formPanel.area(partner1, partner2);
	
		formPanel.line(PARTNERSHIP.changeName1, PARTNERSHIP.changeName2);
		formPanel.line(PARTNERSHIP.name1, PARTNERSHIP.name2);
	}
	
	@Override
	public Partnership load() {
		Partnership partnershipActionData = new Partnership();
		partnershipActionData.partner1 = getPerson();
		return partnershipActionData;
	}

	@Override
	protected List<String> getXml(Person person, Partnership data, WriterEch0020 writerEch0020) throws Exception {
		List<String> xmls = new ArrayList<String>();
		xmls.add(partnership(writerEch0020, data.dateOfMaritalStatus, data.partner1.personIdentification, data.partner2.personIdentification));
		if (Boolean.TRUE.equals(data.registerPartner2)) {
			xmls.add(partnership(writerEch0020, data.dateOfMaritalStatus, data.partner2.personIdentification, data.partner1.personIdentification));
		}
		
		if (Boolean.TRUE.equals(data.changeName1)) {
			xmls.add(changeName(writerEch0020, person, data.name1));
		}
		if (Boolean.TRUE.equals(data.changeName2) && data.partner2.isPersisent()) {
			xmls.add(changeName(writerEch0020, data.partner2, data.name2));
		}

		return xmls;
	}
	
	private String partnership(WriterEch0020 writerEch0020, LocalDate dateOfMaritalStatus, PersonIdentification partner1, PersonIdentification partner2) throws Exception {
		Relation relation = new Relation();
		relation.typeOfRelationship = TypeOfRelationship.Partner;
		relation.partner = partner2;
		return writerEch0020.partnership(partner1, relation, dateOfMaritalStatus);
	}
	
	private String changeName(WriterEch0020 writerEch0020, Person person, String officialName) throws Exception {
		Person personToChange = CloneHelper.clone(person);
		personToChange.alliancePartnershipName = officialName; // !!
		return writerEch0020.changeName(person.personIdentification, personToChange);
	}
	
}
