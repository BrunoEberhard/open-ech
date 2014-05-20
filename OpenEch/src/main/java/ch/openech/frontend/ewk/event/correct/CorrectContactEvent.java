package ch.openech.frontend.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.BusinessRule;

import ch.openech.frontend.ewk.event.PersonEventEditor;
import  ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectContactEvent extends PersonEventEditor<Person> {

	public CorrectContactEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		if (echSchema.contactHasValidTill()) {
			formPanel.line(Person.PERSON.contactPerson.validTill);
		}
		formPanel.line(Person.PERSON.contactPerson);
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

