package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Person.PERSON;

import java.util.Collections;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Nationality;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class UndoSwissEvent extends PersonEventEditor<Person> {

	public UndoSwissEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line(PERSON.nationality);
		formPanel.line(PERSON.residence);
		formPanel.line(PERSON.foreign.nameOnPassport);
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.undoSwiss(changedPerson));
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	public void validate(Person object, List<ValidationMessage> resultList) {
		Nationality nationality = object.nationality;
		if (nationality == null || nationality.nationalityCountry == null || nationality.nationalityCountry.isEmpty()) {
			resultList.add(new ValidationMessage("nationality", "Nationalität muss gesetzt sein"));
		} else if (nationality != null && nationality.nationalityCountry != null && nationality.nationalityCountry.isSwiss()) {
			resultList.add(new ValidationMessage("nationality", "Nationalität darf nicht Schweiz sein"));
		}
	}
	
}
