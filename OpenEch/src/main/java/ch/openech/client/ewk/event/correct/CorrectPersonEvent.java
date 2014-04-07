package ch.openech.client.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonEditMode;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

// Dimension(900, 580);
public class CorrectPersonEvent extends PersonEventEditor<Person> {

	public CorrectPersonEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	public IForm<Person> createForm() {
		return new PersonPanel(PersonEditMode.CORRECT_PERSON, echSchema);
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
		return Collections.singletonList(writerEch0020.correctPerson(person.personIdentification(), changedPerson));
	}
	
}
