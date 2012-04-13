package ch.openech.client.ewk.event;

import java.util.ArrayList;
import java.util.List;

import ch.openech.client.e44.PersonField;
import ch.openech.dm.EchFormats;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.Relation;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Boolean;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.edit.fields.CheckBoxStringField;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.fields.TextEditField;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;


// Ähnlich Marriage, aber die Heimatorte werden nicht ausgewechselt und
// "nur" der Allianzname nicht der "richtige" Name
public class PartnershipEvent extends PersonEventEditor<PartnershipEvent.Partnership> {

	public PartnershipEvent(EchNamespaceContext echNamespaceContext) {
		super(echNamespaceContext);
	}
		
	public static class Partnership {
		@Date
		public String dateOfMaritalStatus;
		@Boolean
		public String registerPartner2 = "1";
		public Person partner1, partner2;
		@Boolean
		public String changeName1, changeName2;
		@FormatName(EchFormats.baseName)
		public String name1, name2;
	}
	
	private static final Partnership PARTNERSHIP = Constants.of(Partnership.class);

	@Override
	protected int getFormColumns() {
		return 2;
	}
	
	@Override
	protected void fillForm(AbstractFormVisual<Partnership> formPanel) {
		PersonField partner1 = new PersonField(PARTNERSHIP.partner1); 
		PersonField partner2 = new PersonField(PARTNERSHIP.partner2);
		
		//
		
		formPanel.line(new DateField(PARTNERSHIP.dateOfMaritalStatus, DateField.REQUIRED), PARTNERSHIP.registerPartner2);
		formPanel.area(partner1, partner2);
	
		formPanel.line(PARTNERSHIP.changeName1, PARTNERSHIP.changeName2);
		formPanel.line(PARTNERSHIP.name1, PARTNERSHIP.name2);
	}

	// TODO da bin ich mir fachlich nicht mehr sicher, wie das mit dem Allianz-Namen
	// funktionieren sollte
	private static class NewPersonNameField extends TextEditField implements DependingOnFieldAbove<Person> {

		private final String dependingOnFieldName;
		
		public NewPersonNameField(Object key, Object dependingOnFieldKey) {
			super(Constants.getConstant(key), Partnership.class);
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
				setObject(person.alliancePartnershipName);
			}
		}
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
		if (CheckBoxStringField.isTrue(data.registerPartner2)) {
			xmls.add(partnership(writerEch0020, data.dateOfMaritalStatus, data.partner2.personIdentification, data.partner1.personIdentification));
		}
		
		if (CheckBoxStringField.isTrue(data.changeName1)) {
			xmls.add(changeName(writerEch0020, person, data.name1));
		}
		if (CheckBoxStringField.isTrue(data.changeName2) && data.partner2.isPersisent()) {
			xmls.add(changeName(writerEch0020, data.partner2, data.name2));
		}

		return xmls;
	}
	
	private String partnership(WriterEch0020 writerEch0020, String dateOfMaritalStatus, PersonIdentification partner1, PersonIdentification partner2) throws Exception {
		Relation relation = new Relation();
		relation.typeOfRelationship = "2";
		relation.partner = partner2;
		return writerEch0020.partnership(partner1, relation, dateOfMaritalStatus);
	}
	
	private String changeName(WriterEch0020 writerEch0020, Person person, String officialName) throws Exception {
		Person personToChange = CloneHelper.clone(person);
		personToChange.alliancePartnershipName = officialName; // !!
		return writerEch0020.changeName(person.personIdentification, personToChange);
	}
	
	@Override
	public void validate(Partnership data, List<ValidationMessage> resultList) {
		MarriageEvent.validate(resultList, data.partner1, data.partner2, false);
		validateNamesNotBlank(data, resultList);
	}
	
	private void validateNamesNotBlank(Partnership data, List<ValidationMessage> validationMessages) {
		if (CheckBoxStringField.isTrue(data.changeName1)) MarriageEvent.validateNameNotBlank(validationMessages, "name1", data.name1);
		if (CheckBoxStringField.isTrue(data.changeName2)) MarriageEvent.validateNameNotBlank(validationMessages, "name2", data.name2);
	}
}
