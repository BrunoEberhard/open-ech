package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.BusinessRule;

import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

// Müsste wohl eigentlich ChangeContact heissen, heisst es aber im Schema nicht
public class ContactEvent extends PersonEventEditor<Person> {

	public ContactEvent(EchSchema ech, Person person) {
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
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.contact(changedPerson));
	}

	@Override
	@BusinessRule("Bei Änderungen an der Zustelladresse / Kontaktadresse muss die Adresse gesetzt sein")
	public void validate(Person person, List<ValidationMessage> resultList) {
		if (person.contactPerson.address == null) {
			resultList.add(new ValidationMessage(Person.PERSON.contactPerson.address, "Kontaktadresse muss gesetzt sein"));
		}
	}

}

