package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

// return new Dimension(900, 550);
public class ChangeTypeOfResidenceEvent extends PersonEventEditor<Person> {
	
	public ChangeTypeOfResidenceEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	public Form<Person> createForm() {
		return new PersonPanel(PersonEditMode.CHANGE_RESIDENCE_TYPE, echSchema);
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
		return Collections.singletonList(writerEch0020.changeResidenceType(person.personIdentification(), changedPerson));
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		if (person.comesFrom == null || person.comesFrom.isUnknown()) {
			resultList.add(new ValidationMessage(Person.$.comesFrom, "Herkunftsort erforderlich"));
		}
		if (person.comesFrom == null || person.comesFrom.mailAddress == null || person.comesFrom.mailAddress.isEmpty()) {
			resultList.add(new ValidationMessage(Person.$.comesFromAddress, "Herkunftsadresse erforderlich"));
		}
	}
	
}
