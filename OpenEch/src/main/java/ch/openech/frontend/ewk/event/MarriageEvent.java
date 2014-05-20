package ch.openech.frontend.ewk.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Enabled;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.BusinessRule;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.StringUtils;

import ch.openech.frontend.RemoveEntriesListField;
import ch.openech.frontend.e44.PersonField;
import  ch.openech.model.EchFormats;
import  ch.openech.model.person.Person;
import  ch.openech.model.person.PersonIdentification;
import  ch.openech.model.person.PlaceOfOrigin;
import  ch.openech.model.person.Relation;
import  ch.openech.model.person.types.ReasonOfAcquisition;
import  ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class MarriageEvent extends PersonEventEditor<MarriageEvent.Marriage> {

	public MarriageEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	public boolean isEnabled() {
		return getPerson().isAlive() && !getPerson().isMarried() && !getPerson().hasPartner();
	}
	
	public static class Marriage implements Validation {
		public static final Marriage MARRIAGE = Keys.of(Marriage.class);
		
		@Required
		public LocalDate dateOfMaritalStatus;
		public Boolean registerPartner2 = Boolean.TRUE;
		public Person partner1;
		public Person partner2;
		public Boolean changeName1 = Boolean.FALSE;
		public Boolean changeName2 = Boolean.FALSE;
		@Size(EchFormats.baseName) @Enabled("getChangeName1")
		public String name1;
		@Size(EchFormats.baseName) @Enabled("getChangeName2")
		public String name2;
		public final List<PlaceOfOrigin> origin1 = new ArrayList<PlaceOfOrigin>();
		public final List<PlaceOfOrigin> origin2 = new ArrayList<PlaceOfOrigin>();
		
		public Boolean getChangeName1() {
			return changeName1;
		}

		public Boolean getChangeName2() {
			return changeName2;
		}

		@Override
		public void validate(List<ValidationMessage> resultList) {
			validate(resultList, partner1, partner2);
			validateNamesNotBlank(resultList);
		}
		
		private static void validate(List<ValidationMessage> validationMessages, Person person1, Person person2) {
			validatePartnerAlive(validationMessages, MARRIAGE.partner1, person1);
			validatePartnerAlive(validationMessages, MARRIAGE.partner2, person2);
			validatePartnerSet(validationMessages, MARRIAGE.partner1, person1);
			validatePartnerSet(validationMessages, MARRIAGE.partner2, person2);
			validateSex(validationMessages, person1, person2);
		}
		
		private void validateNamesNotBlank(List<ValidationMessage> validationMessages) {
			if (Boolean.TRUE.equals(changeName1)) validateNameNotBlank(validationMessages, MARRIAGE.name1, name1);
			if (Boolean.TRUE.equals(changeName2)) validateNameNotBlank(validationMessages, MARRIAGE.name2, name2);
		}
		
		@BusinessRule("2 Eheleute für Ehe notwendig")
		private static void validatePartnerSet(List<ValidationMessage> validationMessages, Person key, Person person) {
			if (person == null) {
				validationMessages.add(new ValidationMessage(key, "Person fehlt"));
			}
		}
		
		@BusinessRule("Ehepartner müssen leben")
		private static void validatePartnerAlive(List<ValidationMessage> validationMessages, Person key, Person person) {
			if (person == null) return;
			if (person.dateOfDeath != null) {
				validationMessages.add(new ValidationMessage(key, "Person darf nicht tot sein"));
			}
		}
		
		@BusinessRule("Eheleute müssen unterschiedlichen Geschlechts sein")
		private static void validateSex(List<ValidationMessage> validationMessages, Person person1, Person person2) {
			if (person1 == null || person2 == null) return;
			if (person1.sex == null || person2.sex == null) return;
			if (person1.sex == person2.sex) {
				validationMessages.add(new ValidationMessage(MARRIAGE.partner2, "Eheleute müssen unterschiedlichen Geschlechts sein"));
			}
		}

		@BusinessRule("Geänderter Name bei Ehe, Allianzname bei Partnerschaft darf nicht leer sein, sofern er geändert wird")
		private static void validateNameNotBlank(List<ValidationMessage> validationMessages, String key, String name) {
			if (StringUtils.isBlank(name)) {
				validationMessages.add(new ValidationMessage(key, "Name kann nicht leer sein"));
			}
		}
	}
	
	public static class PartnerNameUpdater implements Form.PropertyUpdater<Person, String, Marriage> {
		@Override
		public String update(Person partner, Marriage marriage) {
			if (partner != null) {
				return partner.officialName;
			} else {
				return null;
			}
		}
	}

	public static class PartnerOriginUpdater implements Form.PropertyUpdater<Person, List<PlaceOfOrigin>, Marriage> {
		@Override
		public List<PlaceOfOrigin> update(Person partner, Marriage marriage) {
			if (partner != null) {
				return new ArrayList<>(partner.placeOfOrigin);
			} else {
				return Collections.emptyList();
			}
		}
	}

	private static final Marriage MARRIAGE = Keys.of(Marriage.class);

	@Override
	protected int getFormColumns() {
		return 2;
	}
	
	@Override
	protected void fillForm(Form<Marriage> formPanel) {
		PersonField partner1 = new PersonField(MARRIAGE.partner1); 
		PersonField partner2 = new PersonField(MARRIAGE.partner2);
	
		RemoveEntriesListField<PlaceOfOrigin> origin1 = new RemoveEntriesListField<PlaceOfOrigin>(MARRIAGE.origin1);
		RemoveEntriesListField<PlaceOfOrigin> origin2 = new RemoveEntriesListField<PlaceOfOrigin>(MARRIAGE.origin2);
		
		//
		
		formPanel.line(MARRIAGE.dateOfMaritalStatus, MARRIAGE.registerPartner2);
		formPanel.line(partner1, partner2);
		formPanel.line(MARRIAGE.changeName1, MARRIAGE.changeName2);
		formPanel.line(MARRIAGE.name1, MARRIAGE.name2);
		formPanel.line(origin1, origin2);
		
		formPanel.addDependecy(MARRIAGE.partner1, new PartnerNameUpdater(), MARRIAGE.name2);
		formPanel.addDependecy(MARRIAGE.partner1, new PartnerOriginUpdater(), MARRIAGE.origin2);

		formPanel.addDependecy(MARRIAGE.partner2, new PartnerNameUpdater(), MARRIAGE.name1);
		formPanel.addDependecy(MARRIAGE.partner2, new PartnerOriginUpdater(), MARRIAGE.origin1);
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
		xmls.add(marriage(writerEch0020, data.dateOfMaritalStatus, data.partner1.personIdentification(), data.partner2.personIdentification()));
		if (Boolean.TRUE.equals(data.registerPartner2)) {
			xmls.add(marriage(writerEch0020, data.dateOfMaritalStatus, data.partner2.personIdentification(), data.partner1.personIdentification()));
		}

		// Die Einbürgerungen müssen vor dem Namenswechsel erfolgen, sonst passen
		// eventuell die PersonenIdentifikationen nicht mehr
		LocalDate naturalizationDate = data.dateOfMaritalStatus;
		naturalizeSwiss(writerEch0020, data.partner1, data.origin1, naturalizationDate, xmls);
		naturalizeSwiss(writerEch0020, data.partner2, data.origin2, naturalizationDate, xmls);
		
		if (Boolean.TRUE.equals(data.changeName1)) {
			xmls.add(changeName(writerEch0020, person, data.name1));
		}
		if (Boolean.TRUE.equals(data.changeName2) && data.partner2.id != 0) {
			xmls.add(changeName(writerEch0020, data.partner2, data.name2));
		}

		return xmls;
	}
	
	private String marriage(WriterEch0020 writerEch0020, LocalDate dateOfMaritalStatus, PersonIdentification partner1, PersonIdentification partner2) throws Exception {
		Relation relation = new Relation();
		relation.typeOfRelationship = TypeOfRelationship.Ehepartner;
		relation.partner = partner2;
		return writerEch0020.marriage(partner1, relation, dateOfMaritalStatus);
	}
	
	private String changeName(WriterEch0020 writerEch0020, Person person, String officialName) throws Exception {
		Person personToChange = CloneHelper.clone(person);
		personToChange.officialName = officialName;
		return writerEch0020.changeName(person.personIdentification(), personToChange);
	}

	private void naturalizeSwiss(WriterEch0020 writerEch0020, Person person, List<PlaceOfOrigin> list, LocalDate naturalizationDate, List<String> xmls) throws Exception {
		for (PlaceOfOrigin existingOrigin : list) {
			PlaceOfOrigin origin = new PlaceOfOrigin();
			origin.originName = existingOrigin.originName;
			origin.cantonAbbreviation.canton = existingOrigin.cantonAbbreviation.canton;
			origin.reasonOfAcquisition = ReasonOfAcquisition.Heirat;
			origin.naturalizationDate = naturalizationDate;
			xmls.add(writerEch0020.naturalizeSwiss(person.personIdentification(), origin));
		}
	}


}
