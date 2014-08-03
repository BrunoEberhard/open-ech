package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;

import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class ChangeNationalityEvent extends PersonEventEditor<Person> {

	public ChangeNationalityEvent(EchSchema ech, Person person) {
		super(ech, person);
	}
	
	@Override
	protected void fillForm(Form<Person> objectPanel) {
		objectPanel.line(Person.PERSON.nationality);
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.changeNationality(person.personIdentification(), changedPerson.nationality));
	}

}
