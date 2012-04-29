package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Person.PERSON;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;


public class ChangeResidencePermitEvent extends PersonEventEditor<Person> {

	public ChangeResidencePermitEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.area(PERSON.foreign.residencePermit);
		formPanel.area(PERSON.foreign.residencePermitTill);
//		formPanel.line(OpenEchCodeField.residencePermit(getEchNamespaceContext().residencePermitDetailed()));
//		formPanel.line(new DateField(FOREIGN.residencePermitTill, DateField.REQUIRED)); 
		formPanel.area(PERSON.occupation);

		// bei Change ist diese Angabe obligatorisch, bei Correct nicht
		formPanel.setRequired(PERSON.foreign.residencePermitTill);
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		String residencePermit = person.foreign.residencePermit;
		if (StringUtils.isBlank(residencePermit)) {
			resultList.add(new ValidationMessage(PERSON.foreign.residencePermit, "Ausländerkategorie muss gesetzt sein"));
		}
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.changeResidencePermit(changedPerson));
    }

}
