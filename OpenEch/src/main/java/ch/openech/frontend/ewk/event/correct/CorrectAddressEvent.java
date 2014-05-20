package ch.openech.frontend.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;

import ch.openech.frontend.ewk.event.PersonEventEditor;
import  ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

// Eigentlich CorrectDwellingAddress
public class CorrectAddressEvent extends PersonEventEditor<Person> {
	
	public CorrectAddressEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line(Person.PERSON.dwellingAddress);
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctAddress(changedPerson));
	}
}
