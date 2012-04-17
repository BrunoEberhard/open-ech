package ch.openech.client.ewk.event;

import java.util.ArrayList;
import java.util.List;

import ch.openech.client.e44.SecondPersonField;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Boolean;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.util.BusinessRule;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0093;

public abstract class ChangeWithSecondPersonEvent extends
		PersonEventEditor<ChangeWithSecondPersonEvent.ChangeWithSecondPersonEventData> {

	public ChangeWithSecondPersonEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	public static class ChangeWithSecondPersonEventData {
		public static final ChangeWithSecondPersonEventData DATA = Constants.of(ChangeWithSecondPersonEventData.class);
		@Required @Date
		public String date;
		public String separation;
		public String cancelationReason;
		@Boolean
		public String registerPartner = "0";
		public Relation relationPartner;
		
		public boolean registerPartner() {
			return "1".equals(registerPartner);
		}
	}

	protected abstract void createSpecificForm(AbstractFormVisual<ChangeWithSecondPersonEventData> formPanel);

	@Override
	protected void fillForm(AbstractFormVisual<ChangeWithSecondPersonEventData> formPanel) {
		createSpecificForm(formPanel);
		formPanel.line(ChangeWithSecondPersonEventData.DATA.registerPartner);
		formPanel.line(new SecondPersonField(ChangeWithSecondPersonEventData.DATA.relationPartner));
	}

	@Override
	public ChangeWithSecondPersonEventData load() {
		ChangeWithSecondPersonEventData data = new ChangeWithSecondPersonEventData();
		if (getPerson().getPartner() != null) {
			data.relationPartner = getPerson().getPartner();
			data.registerPartner = "1";
		}
		return data;
	}

	@BusinessRule("Neues Zivilstandsereignis darf nicht vor dem gültigen sein")
	protected void validateEventNotBeforeDateOfMaritalStatus(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
		String date = data.date;
		if (getPerson() != null && !StringUtils.isBlank(getPerson().maritalStatus.dateOfMaritalStatus)
				&& !StringUtils.isBlank(date)) {
			if (date.compareTo(getPerson().maritalStatus.dateOfMaritalStatus) < 0) {
				validationMessages.add(new ValidationMessage(ChangeWithSecondPersonEventData.DATA.date,
						"Datum darf nicht vor letztem Zivilstandsereignis sein"));
			}
		}
	}

	public static class DeathEvent extends ChangeWithSecondPersonEvent {

		public DeathEvent(EchNamespaceContext namespaceContext) {
			super(namespaceContext);
		}

		@Override
		protected void createSpecificForm(AbstractFormVisual<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.death(person.personIdentification, data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				if ("1".equals(relation.typeOfRelationship)) {
					// Ehe
					xmls.add(writerEch0020.maritalStatusPartner(relation.partner, "3", data.date, null));
				} else if ("2".equals(relation.typeOfRelationship)) {
					// Eingetragene Partnerschaft
					xmls.add(writerEch0020.maritalStatusPartner(relation.partner, "7", data.date, "4"));
				}
			}
			return xmls;
		}

		@Override
		public void generateSedexOutput(ChangeWithSecondPersonEventData object) throws Exception {
			if (getPerson().isMainResidence() && getPerson().residence.secondary != null) {
				for (MunicipalityIdentification secondaryResidence : getPerson().residence.secondary) {
					WriterEch0093 sedexWriter = new WriterEch0093(getEchNamespaceContext());
					sedexWriter.setRecepientMunicipality(secondaryResidence);
					String sedexOutput = sedexWriter.death(getPerson().personIdentification, object.date);
					SedexOutputGenerator.generateSedex(sedexOutput, sedexWriter.getEnvelope());
				}
			}
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			Person.validateEventNotBeforeBirth(validationMessages, getPerson().personIdentification, data.date, ChangeWithSecondPersonEventData.DATA.date);
		}
	}

	public static class MissingEvent extends ChangeWithSecondPersonEvent {

		public MissingEvent(EchNamespaceContext namespaceContext) {
			super(namespaceContext);
		}

		@Override
		protected void createSpecificForm(AbstractFormVisual<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.missing(person.personIdentification, data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				if ("1".equals(relation.typeOfRelationship)) {
					// Ehe
					xmls.add(writerEch0020.maritalStatusPartner(relation.partner, "3", data.date, null));
				} else if ("2".equals(relation.typeOfRelationship)) {
					// Eingetragene Partnerschaft
					xmls.add(writerEch0020.maritalStatusPartner(relation.partner, "7", data.date, "4"));
				}
			}
			return xmls;
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			Person.validateEventNotBeforeBirth(validationMessages, getPerson().personIdentification, data.date, ChangeWithSecondPersonEventData.DATA.date);
		}
	}

	public static class SeparationEvent extends ChangeWithSecondPersonEvent {

		public SeparationEvent(EchNamespaceContext namespaceContext) {
			super(namespaceContext);
		}

		@Override
		protected void createSpecificForm(AbstractFormVisual<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(Person.PERSON.separation);
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.separation(person.personIdentification, data.separation, data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.separation(relation.partner, data.separation, data.date));
			}
			return xmls;
		}
	}

	public static class UndoSeparationEvent extends ChangeWithSecondPersonEvent {

		public UndoSeparationEvent(EchNamespaceContext namespaceContext) {
			super(namespaceContext);
		}

		@Override
		protected void createSpecificForm(AbstractFormVisual<ChangeWithSecondPersonEventData> formPanel) {
			// nothing in it
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.undoSeparation(person.personIdentification));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.undoSeparation(relation.partner));
			}
			return xmls;
		}
		
		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			// nothing to validate
		}
	}

	public static class DivorceEvent extends ChangeWithSecondPersonEvent {

		public DivorceEvent(EchNamespaceContext namespaceContext) {
			super(namespaceContext);
		}

		@Override
		protected void createSpecificForm(AbstractFormVisual<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.divorce(person.personIdentification, data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.divorce(relation.partner, data.date));
			}
			return xmls;
		}
	}

	public static class UndoPartnershipEvent extends ChangeWithSecondPersonEvent {

		public UndoPartnershipEvent(EchNamespaceContext namespaceContext) {
			super(namespaceContext);
		}

		@Override
		protected void createSpecificForm(AbstractFormVisual<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
			formPanel.line(ChangeWithSecondPersonEventData.DATA.cancelationReason);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.undoPartnership(person.personIdentification, data.date, data.cancelationReason));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.undoPartnership(relation.partner, data.date, data.cancelationReason));
			}
			return xmls;
		}
	}

	public static class UndoMarriageEvent extends ChangeWithSecondPersonEvent  {

		public UndoMarriageEvent(EchNamespaceContext namespaceContext) {
			super(namespaceContext);
		}

		@Override
		protected void createSpecificForm(AbstractFormVisual<ChangeWithSecondPersonEventData> formPanel) {
			formPanel.line(ChangeWithSecondPersonEventData.DATA.date);
		}

		@Override
		public void validate(ChangeWithSecondPersonEventData data, List<ValidationMessage> validationMessages) {
			validateEventNotBeforeDateOfMaritalStatus(data, validationMessages);
		}

		@Override
		protected List<String> getXml(Person person, ChangeWithSecondPersonEventData data, WriterEch0020 writerEch0020)
				throws Exception {
			List<String> xmls = new ArrayList<String>();
			xmls.add(writerEch0020.undoMarriage(person.personIdentification, data.date));

			if (data.registerPartner()) {
				Relation relation = person.getPartner();
				xmls.add(writerEch0020.undoMarriage(relation.partner, data.date));
			}
			return xmls;
		}
	}
}
