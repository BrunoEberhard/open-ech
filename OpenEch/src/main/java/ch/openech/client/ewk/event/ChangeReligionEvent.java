package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Person.PERSON;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class ChangeReligionEvent extends PersonEventEditor<Person> {

	public ChangeReligionEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
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
        return Collections.singletonList(writerEch0020.changeReligion(person.personIdentification, changedPerson.religion));
	}

}
