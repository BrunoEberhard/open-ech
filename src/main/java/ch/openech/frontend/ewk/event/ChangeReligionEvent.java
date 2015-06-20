package ch.openech.frontend.ewk.event;

import static ch.openech.model.person.Person.*;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.xml.write.WriterEch0020;

public class ChangeReligionEvent extends PersonEventEditor<Person> {

	public ChangeReligionEvent(PersonPage personPage) {
		super(personPage);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line($.religion);
	}

	@Override
	public Person createObject() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.changeReligion(person.personIdentification(), changedPerson.religion));
	}

}
