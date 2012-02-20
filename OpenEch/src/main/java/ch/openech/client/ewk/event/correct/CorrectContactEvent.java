package ch.openech.client.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class CorrectContactEvent extends PersonEventEditor<Person> {

	public CorrectContactEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(AbstractFormVisual<Person> formPanel) {
		if (getEchNamespaceContext().contactHasValidTill()) {
			formPanel.line(Person.PERSON.contactPerson.validTill);
		}
		formPanel.area(Person.PERSON.contactPerson);
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	@BusinessRule("Bei Korrektur an der Zustelladresse / Kontaktadresse muss die Adresse gesetzt sein oder der Kontakt muss ganz gelöscht werden")
	public void validate(Person person, List<ValidationMessage> resultList) {
		if (person.contactPerson.address == null && person.contactPerson.person != null) {
			resultList.add(new ValidationMessage("contactPerson", "Kontaktadresse muss gesetzt sein, falls eine Person gewählt ist."));
		}
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctContact(person));
	}


}

