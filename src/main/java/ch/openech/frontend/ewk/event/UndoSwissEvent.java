package ch.openech.frontend.ewk.event;

import static ch.openech.model.person.Person.*;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.model.person.Nationality;
import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class UndoSwissEvent extends PersonEventEditor<Person> {

	public UndoSwissEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line($.nationality);
		formPanel.line($.foreign.residencePermit);
		formPanel.line($.foreign.nameOnPassport);
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
			resultList.add(new ValidationMessage($.nationality, "Nationalität muss gesetzt sein"));
		} else if (nationality != null && nationality.nationalityCountry != null && nationality.nationalityCountry.isSwiss()) {
			resultList.add(new ValidationMessage($.nationality, "Nationalität darf nicht Schweiz sein"));
		}
	}
	
}
