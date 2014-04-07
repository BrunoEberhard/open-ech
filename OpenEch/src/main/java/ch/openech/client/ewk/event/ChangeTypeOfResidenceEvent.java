package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonEditMode;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

// return new Dimension(900, 550);
public class ChangeTypeOfResidenceEvent extends PersonEventEditor<Person> {
	
	public ChangeTypeOfResidenceEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	public IForm<Person> createForm() {
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
			resultList.add(new ValidationMessage(Person.PERSON.comesFrom, "Herkunftsort erforderlich"));
		}
		if (person.comesFrom == null || person.comesFrom.mailAddress == null || person.comesFrom.mailAddress.isEmpty()) {
			resultList.add(new ValidationMessage(Person.PERSON.comesFromAddress, "Herkunftsadresse erforderlich"));
		}
	}
	
}
