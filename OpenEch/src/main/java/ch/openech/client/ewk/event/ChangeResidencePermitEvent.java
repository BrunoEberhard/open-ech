package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Person.PERSON;

import java.util.Collections;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;


public class ChangeResidencePermitEvent extends PersonEventEditor<Person> {

	public ChangeResidencePermitEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line(PERSON.foreign.residencePermit);
		formPanel.line(PERSON.foreign.residencePermitTill);
		formPanel.area(PERSON.occupation);

		formPanel.setRequired(PERSON.foreign.residencePermit);
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
