package ch.openech.frontend.ewk.event.correct;

import static ch.openech.model.person.Person.*;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.frontend.ewk.event.PersonEventEditor;
import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.xml.write.WriterEch0020;

public class CorrectOccupationEvent extends PersonEventEditor<Person> {

	public CorrectOccupationEvent(PersonPage personPage) {
		super(personPage);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line($.occupation);
	}

	@Override
	public Person createObject() {
		return getPerson();
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		if (echSchema.correctOccupationMustHaveOccupation()) {
			if (person.occupation.isEmpty()) {
				resultList.add(new ValidationMessage(Person.$.occupation,
						"In der aktiven Schema - Version muss hier min. 1 Beruf eingetragen sein"));
			}
		}
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
      return Collections.singletonList(writerEch0020.correctOccupation(person.personIdentification(), changedPerson.occupation));

	}

}
