package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class ChangeNationalityEvent extends PersonEventEditor<Person> {

	public ChangeNationalityEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}
	
	@Override
	protected void fillForm(AbstractFormVisual<Person> objectPanel) {
		objectPanel.line(Person.PERSON.nationality);
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.changeNationality(person.personIdentification, changedPerson.nationality));
	}

}
