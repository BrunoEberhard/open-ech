package ch.openech.frontend.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;

import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.frontend.ewk.event.PersonEventEditor;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectIdentificationEvent extends PersonEventEditor<Person> {

	public CorrectIdentificationEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	public Form<Person> createForm() {
		return new PersonPanel(PersonEditMode.CORRECT_IDENTIFICATION, echSchema);
	}
	
	@Override
	protected void fillForm(Form<Person> formPanel) {
		// not used
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctIdentification(person.personIdentification(), changedPerson));
	}
	
}
