package ch.openech.client.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

// Dimension(900, 580);
public class CorrectPersonEvent extends PersonEventEditor<Person> {

	public CorrectPersonEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	public FormVisual<Person> createForm() {
		return new PersonPanel(PersonPanelType.CORRECT_PERSON, getEchNamespaceContext());
	}
	
	@Override
	protected void fillForm(AbstractFormVisual<Person> formPanel) {
		// not used
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctPerson(person.personIdentification, changedPerson));
	}
	
}
