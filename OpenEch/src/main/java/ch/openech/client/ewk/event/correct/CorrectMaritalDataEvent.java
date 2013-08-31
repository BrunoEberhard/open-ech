package ch.openech.client.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectMaritalDataEvent extends PersonEventEditor<Person> {

	public CorrectMaritalDataEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line(Person.PERSON.maritalStatus.maritalStatus);
		formPanel.line(Person.PERSON.maritalStatus.dateOfMaritalStatus);

		formPanel.line(Person.PERSON.separation.separation);
		if (echSchema.separationTillAvailable()) {
			formPanel.line(Person.PERSON.separation.separationTill);
		}
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctMaritalData(changedPerson));
	}
	
}
