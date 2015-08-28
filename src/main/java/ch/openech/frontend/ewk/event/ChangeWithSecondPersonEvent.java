package ch.openech.frontend.ewk.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.validation.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.BusinessRule;

import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.Relation;
import ch.openech.model.person.SecondaryResidence;
import ch.openech.model.person.types.MaritalStatus;
import ch.openech.model.person.types.PartnerShipAbolition;
import ch.openech.model.person.types.Separation;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0093;

public abstract class ChangeWithSecondPersonEvent extends
		PersonEventEditor<ChangeWithSecondPersonEvent.ChangeWithSecondPersonEventData> {

	public ChangeWithSecondPersonEvent(PersonPage personPage) {
		super(personPage);
	}

	public static class ChangeWithSecondPersonEventData {
		public static final ChangeWithSecondPersonEventData $ = Keys.of(ChangeWithSecondPersonEventData.class);
		@NotEmpty
		public LocalDate date;
		public Separation separation;
		public PartnerShipAbolition cancelationReason;
		public Boolean registerPartner = Boolean.FALSE;
		public Person partnerPerson;
		
		public boolean registerPartner() {
			return Boolean.TRUE.equals(registerPartner);
		}
	}

	protected abstract void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel);

	@Override
	protected void fillForm(Form<ChangeWithSecondPersonEventData> formPanel) {
		createSpecificForm(formPanel);
		if (getPerson().getPartner() != null) {
			formPanel.line(ChangeWithSecondPersonEventData.$.registerPartner);
			formPanel.line(ChangeWithSecondPersonEventData.$.partnerPerson);
		} else {
			formPanel.text("(Person ohne Partner)");
		}
	}

	@Override
	public ChangeWithSecondPersonEventData createObject() {
		ChangeWithSecondPersonEventData data = new ChangeWithSecondPersonEventData();
		Relation partnerRelation = getPerson().getPartner();
		if (partnerRelation != null && partnerRelation.partner.person != null) {
			data.partnerPerson = partnerRelation.partner.person;
			data.registerPartner = Boolean.TRUE;
		}
		return data;
	}

	@Override
	public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
		if (data.registerPartner() && data.partnerPerson == null) {
			validationMessages.add(new ValidationMessage(ChangeWithSecondPersonEventData.$.registerPartner,
					"Für Partnereintrag ist vorhandener Partner bei Person erforderlich"));
		}
	}

	@BusinessRule("Neues Zivilstandsereignis darf nicht vor dem gültigen sein")
	protected void validateEventNotBeforeDateOfMaritalStatus(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
		LocalDate date = data.date;
		if (getPerson() != null && getPerson().maritalStatus.dateOfMaritalStatus != null
				&& date != null) {
			if (date.compareTo(getPerson().maritalStatus.dateOfMaritalStatus) < 0) {
				validationMessages.add(new ValidationMessage(ChangeWithSecondPersonEventData.$.date,
						"Datum darf nicht vor letztem Zivilstandsereignis sein"));
			}
		}
	}

	public static class DeathEvent extends ChangeWithSecondPersonEvent {

		public DeathEvent(PersonPage personPage) {
			super(personPage);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.$.date);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020) throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.death(person.personIdentification(), data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				if (TypeOfRelationship.Ehepartner == relation.typeOfRelationship) {
					xmls.add(writerEch0020.maritalStatusPartner(data.partnerPerson.personIdentification(), MaritalStatus.verwitwet, data.date, null));
				} else if (TypeOfRelationship.Partner == relation.typeOfRelationship) {
					xmls.add(writerEch0020.maritalStatusPartner(data.partnerPerson.personIdentification(), MaritalStatus.aufgeloeste_partnerschaft, data.date, PartnerShipAbolition.tod));
				}
			}
			return xmls;
		}

		@Override
		public void generateSedexOutput(ChangeWithSecondPersonEventData object) throws Exception {
			if (getPerson().isMainResidence() && getPerson().residence.secondary != null) {
				for (SecondaryResidence secondaryResidence : getPerson().residence.secondary) {
					WriterEch0093 sedexWriter = new WriterEch0093(echSchema);
					sedexWriter.setRecepientMunicipality(secondaryResidence.municipalityIdentification);
					String sedexOutput = sedexWriter.death(getPerson().personIdentification(), object.date);
					SedexOutputGenerator.generateSedex(sedexOutput, sedexWriter.getEnvelope());
				}
			}
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			Person.validateEventNotBeforeBirth(validationMessages, getPerson(), data.date, ChangeWithSecondPersonEventData.$.date);
		}
	}

	public static class MissingEvent extends ChangeWithSecondPersonEvent {

		public MissingEvent(PersonPage personPage) {
			super(personPage);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.$.date);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020) throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.missing(person.personIdentification(), data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				if (relation.typeOfRelationship == TypeOfRelationship.Ehepartner) {
					xmls.add(writerEch0020.maritalStatusPartner(data.partnerPerson.personIdentification(), MaritalStatus.verwitwet, data.date, null));
				} else if (relation.typeOfRelationship == TypeOfRelationship.Partner) {
					// Eingetragene Partnerschaft
					xmls.add(writerEch0020.maritalStatusPartner(data.partnerPerson.personIdentification(), MaritalStatus.aufgeloeste_partnerschaft, data.date, PartnerShipAbolition.tod));
				}
			}
			return xmls;
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			Person.validateEventNotBeforeBirth(validationMessages, getPerson(), data.date, ChangeWithSecondPersonEventData.$.date);
		}
	}

	public static class SeparationEvent extends ChangeWithSecondPersonEvent {

		public SeparationEvent(PersonPage personPage) {
			super(personPage);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(Person.$.separation);
			formPanel.line(ChangeWithSecondPersonEventData.$.date);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020) throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.separation(person.personIdentification(), data.separation, data.date));

			if (data.registerPartner()) {
				xmls.add(writerEch0020.separation(data.partnerPerson.personIdentification(), data.separation, data.date));
			}
			return xmls;
		}
	}

	public static class UndoSeparationEvent extends ChangeWithSecondPersonEvent {

		public UndoSeparationEvent(PersonPage personPage) {
			super(personPage);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			// nothing in it
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020) throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.undoSeparation(person.personIdentification()));

			if (data.registerPartner()) {
				xmls.add(writerEch0020.undoSeparation(data.partnerPerson.personIdentification()));
			}
			return xmls;
		}
	}

	public static class DivorceEvent extends ChangeWithSecondPersonEvent {

		public DivorceEvent(PersonPage personPage) {
			super(personPage);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.$.date);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020) throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.divorce(person.personIdentification(), data.date));

			if (data.registerPartner()) {
				xmls.add(writerEch0020.divorce(data.partnerPerson.personIdentification(), data.date));
			}
			return xmls;
		}
	}

	public static class UndoPartnershipEvent extends ChangeWithSecondPersonEvent {

		public UndoPartnershipEvent(PersonPage personPage) {
			super(personPage);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.$.date);
			formPanel.line(ChangeWithSecondPersonEventData.$.cancelationReason);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
			EmptyValidator.validate(validationMessages, data, ChangeWithSecondPersonEventData.$.cancelationReason);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020) throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.undoPartnership(person.personIdentification(), data.date, data.cancelationReason));

			if (data.registerPartner()) {
				xmls.add(writerEch0020.undoPartnership(data.partnerPerson.personIdentification(), data.date, data.cancelationReason));
			}
			return xmls;
		}
	}

	public static class UndoMarriageEvent extends ChangeWithSecondPersonEvent  {

		public UndoMarriageEvent(PersonPage personPage) {
			super(personPage);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.$.date);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020) throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.undoMarriage(person.personIdentification(), data.date));

			if (data.registerPartner()) {
				xmls.add(writerEch0020.undoMarriage(data.partnerPerson.personIdentification(), data.date));
			}
			return xmls;
		}
	}
}
