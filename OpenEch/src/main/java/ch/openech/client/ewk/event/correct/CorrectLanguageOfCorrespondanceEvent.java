package ch.openech.client.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

// Der Typo kommt vom Schema
public class CorrectLanguageOfCorrespondanceEvent extends PersonEventEditor<Person> {
	
	public CorrectLanguageOfCorrespondanceEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(AbstractFormVisual<Person> formPanel) {
		formPanel.line(Person.PERSON.languageOfCorrespondance);
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctLanguageOfCorrespondance(changedPerson));
	}
}