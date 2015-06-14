package ch.openech.frontend.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.ewk.event.PersonEventEditor;
import  ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectMaritalDataEvent extends PersonEventEditor<Person> {

	public CorrectMaritalDataEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line(Person.$.maritalStatus.maritalStatus);
		formPanel.line(Person.$.maritalStatus.dateOfMaritalStatus);

		formPanel.line(Person.$.separation.separation);
		if (echSchema.separationTillAvailable()) {
			formPanel.line(Person.$.separation.separationTill);
		}
	}

	@Override
	public Person createObject() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctMaritalData(changedPerson));
	}
	
}
