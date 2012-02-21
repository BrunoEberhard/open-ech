package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

// Müsste wohl eigentlich ChangeContact heissen, heisst es aber im Schema nicht
public class ContactEvent extends PersonEventEditor<Person> {

	public ContactEvent(EchNamespaceContext namespaceContext) {
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
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.contact(changedPerson));
	}

	@Override
	@BusinessRule("Bei Änderungen an der Zustelladresse / Kontaktadresse muss die Adresse gesetzt sein")
	public void validate(Person person, List<ValidationMessage> resultList) {
		if (person.contactPerson.address == null) {
			resultList.add(new ValidationMessage("contact", "Kontaktadresse muss gesetzt sein"));
		}
	}

}
