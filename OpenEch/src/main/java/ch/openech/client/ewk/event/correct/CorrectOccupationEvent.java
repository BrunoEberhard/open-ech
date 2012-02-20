package ch.openech.client.ewk.event.correct;

import static ch.openech.dm.person.Person.PERSON;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class CorrectOccupationEvent extends PersonEventEditor<Person> {

	public CorrectOccupationEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(AbstractFormVisual<Person> formPanel) {
		formPanel.area(PERSON.occupation);
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		if (getEchNamespaceContext().correctOccupationMustHaveOccupation()) {
			if (person.occupation.isEmpty()) {
				resultList.add(new ValidationMessage("occupation",
						"In der aktiven Schema - Version muss hier min. 1 Beruf eingetragen sein"));
			}
		}
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
      return Collections.singletonList(writerEch0020.correctOccupation(person.personIdentification, changedPerson.occupation));

	}

}
