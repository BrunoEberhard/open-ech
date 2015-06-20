package ch.openech.frontend.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.frontend.ewk.event.PersonEventEditor;
import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.xml.write.WriterEch0020;

// Dimension(900, 580);
public class CorrectPersonEvent extends PersonEventEditor<Person> {

	public CorrectPersonEvent(PersonPage personPage) {
		super(personPage);
	}

	@Override
	public Form<Person> createForm() {
		return new PersonPanel(PersonEditMode.CORRECT_PERSON, echSchema);
	}
	
	@Override
	protected void fillForm(Form<Person> formPanel) {
		// not used
	}

	@Override
	public Person createObject() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctPerson(person.personIdentification(), changedPerson));
	}
	
}
