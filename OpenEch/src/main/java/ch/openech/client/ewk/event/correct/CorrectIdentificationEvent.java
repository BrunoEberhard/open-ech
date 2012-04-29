package ch.openech.client.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class CorrectIdentificationEvent extends PersonEventEditor<Person> {

	public CorrectIdentificationEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	public IForm<Person> createForm() {
		return new PersonPanel(PersonPanelType.CORRECT_IDENTIFICATION, getEchNamespaceContext());
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
		return Collections.singletonList(writerEch0020.correctIdentification(person.personIdentification, changedPerson));
	}
	
}
