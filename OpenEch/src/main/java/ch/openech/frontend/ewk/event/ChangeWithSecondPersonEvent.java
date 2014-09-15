package ch.openech.frontend.ewk.event;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.EmptyValidator;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.BusinessRule;
import org.threeten.bp.LocalDate;

import ch.openech.frontend.e44.SecondPersonField;
import ch.openech.model.person.Person;
import ch.openech.model.person.Relation;
import ch.openech.model.person.SecondaryResidence;
import ch.openech.model.person.types.MaritalStatus;
import ch.openech.model.person.types.PartnerShipAbolition;
import ch.openech.model.person.types.Separation;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0093;

public abstract class ChangeWithSecondPersonEvent extends
		PersonEventEditor<ChangeWithSecondPersonEvent.ChangeWithSecondPersonEventData> {

	public ChangeWithSecondPersonEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	public static class ChangeWithSecondPersonEventData {
		public static final ChangeWithSecondPersonEventData DATA = Keys.of(ChangeWithSecondPersonEventData.class);
		@Required
		public LocalDate date;
		public Separation separation;
		public PartnerShipAbolition cancelationReason;
		public Boolean registerPartner = Boolean.FALSE;
		public Relation relationPartner;
		
		public boolean registerPartner() {
			return Boolean.TRUE.equals(registerPartner);
		}
	}

	protected abstract void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel);

	@Override
	protected void fillForm(Form<ChangeWithSecondPersonEventData> formPanel) {
		createSpecificForm(formPanel);
		if (getPerson().getPartner() != null) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.registerPartner);
			formPanel.line(new SecondPersonField(ChangeWithSecondPersonEventData.DATA.relationPartner));
		} else {
			formPanel.text("(Person ohne Partner)");
		}
	}

	@Override
	public ChangeWithSecondPersonEventData load() {
		ChangeWithSecondPersonEventData data = new ChangeWithSecondPersonEventData();
		if (getPerson().getPartner() != null) {
			data.relationPartner = getPerson().getPartner();
			data.registerPartner = Boolean.TRUE;
		}
		return data;
	}

	public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
		if (data.registerPartner() && data.relationPartner == null) {
			validationMessages.add(new ValidationMessage(ChangeWithSecondPersonEventData.DATA.registerPartner,
					"Für Partnereintrag ist vorhandener Partner bei Person erforderlich"));
		}
	}

	@BusinessRule("Neues Zivilstandsereignis darf nicht vor dem gültigen sein")
	protected void validateEventNotBeforeDateOfMaritalStatus(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
		LocalDate date = data.date;
		if (getPerson() != null && getPerson().maritalStatus.dateOfMaritalStatus != null
				&& date != null) {
			if (date.compareTo(getPerson().maritalStatus.dateOfMaritalStatus) < 0) {
				validationMessages.add(new ValidationMessage(ChangeWithSecondPersonEventData.DATA.date,
						"Datum darf nicht vor letztem Zivilstandsereignis sein"));
			}
		}
	}

	public static class DeathEvent extends ChangeWithSecondPersonEvent {

		public DeathEvent(EchSchema ech, Person person) {
			super(ech, person);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.death(person.personIdentification(), data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				if (TypeOfRelationship.Ehepartner == relation.typeOfRelationship) {
					xmls.add(writerEch0020.maritalStatusPartner(relation.partner, MaritalStatus.verwitwet, data.date, null));
				} else if (TypeOfRelationship.Partner == relation.typeOfRelationship) {
					xmls.add(writerEch0020.maritalStatusPartner(relation.partner, MaritalStatus.aufgeloeste_partnerschaft, data.date, PartnerShipAbolition.tod));
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
			Person.validateEventNotBeforeBirth(validationMessages, getPerson(), data.date, ChangeWithSecondPersonEventData.DATA.date);
		}
	}

	public static class MissingEvent extends ChangeWithSecondPersonEvent {

		public MissingEvent(EchSchema ech, Person person) {
			super(ech, person);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.missing(person.personIdentification(), data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				if (relation.typeOfRelationship == TypeOfRelationship.Ehepartner) {
					xmls.add(writerEch0020.maritalStatusPartner(relation.partner, MaritalStatus.verwitwet, data.date, null));
				} else if (relation.typeOfRelationship == TypeOfRelationship.Partner) {
					// Eingetragene Partnerschaft
					xmls.add(writerEch0020.maritalStatusPartner(relation.partner, MaritalStatus.aufgeloeste_partnerschaft, data.date, PartnerShipAbolition.tod));
				}
			}
			return xmls;
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			Person.validateEventNotBeforeBirth(validationMessages, getPerson(), data.date, ChangeWithSecondPersonEventData.DATA.date);
		}
	}

	public static class SeparationEvent extends ChangeWithSecondPersonEvent {

		public SeparationEvent(EchSchema ech, Person person) {
			super(ech, person);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(Person.PERSON.separation);
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.separation(person.personIdentification(), data.separation, data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.separation(relation.partner, data.separation, data.date));
			}
			return xmls;
		}
	}

	public static class UndoSeparationEvent extends ChangeWithSecondPersonEvent {

		public UndoSeparationEvent(EchSchema ech, Person person) {
			super(ech, person);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			// nothing in it
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.undoSeparation(person.personIdentification()));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.undoSeparation(relation.partner));
			}
			return xmls;
		}
	}

	public static class DivorceEvent extends ChangeWithSecondPersonEvent {

		public DivorceEvent(EchSchema ech, Person person) {
			super(ech, person);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.divorce(person.personIdentification(), data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.divorce(relation.partner, data.date));
			}
			return xmls;
		}
	}

	public static class UndoPartnershipEvent extends ChangeWithSecondPersonEvent {

		public UndoPartnershipEvent(EchSchema ech, Person person) {
			super(ech, person);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
			formPanel.line(ChangeWithSecondPersonEventData.DATA.cancelationReason);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
			EmptyValidator.validate(validationMessages, data, ChangeWithSecondPersonEventData.DATA.cancelationReason);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.undoPartnership(person.personIdentification(), data.date, data.cancelationReason));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.undoPartnership(relation.partner, data.date, data.cancelationReason));
			}
			return xmls;
		}
	}

	public static class UndoMarriageEvent extends ChangeWithSecondPersonEvent  {

		public UndoMarriageEvent(EchSchema ech, Person person) {
			super(ech, person);
		}

		@Override
		protected void createSpecificForm(Form<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			super.validate(data, validationMessages);
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.undoMarriage(person.personIdentification(), data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.undoMarriage(relation.partner, data.date));
			}
			return xmls;
		}
	}
}
