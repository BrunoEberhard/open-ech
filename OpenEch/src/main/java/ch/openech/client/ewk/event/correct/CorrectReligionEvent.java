package ch.openech.client.ewk.event.correct;

import static ch.openech.dm.person.Person.*;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectReligionEvent extends PersonEventEditor<Person> {

	public CorrectReligionEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line(PERSON.religion);
		
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctReligion(person, changedPerson.religion));
	}
	
}
