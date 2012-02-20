package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

// return new Dimension(900, 550);
public class ChangeTypeOfResidenceEvent extends PersonEventEditor<Person> {
	
	public ChangeTypeOfResidenceEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	public FormVisual<Person> createForm() {
		return new PersonPanel(PersonPanelType.CHANGE_RESIDENCE_TYPE, getEchNamespaceContext());
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
		return Collections.singletonList(writerEch0020.changeResidenceType(person.personIdentification, changedPerson));
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		if (person.comesFrom == null || person.comesFrom.isUnknown()) {
			resultList.add(new ValidationMessage("comesFrom", "Herkunftsort erforderlich"));
		}
		if (person.comesFrom == null || person.comesFrom.mailAddress == null || person.comesFrom.mailAddress.isEmpty()) {
			resultList.add(new ValidationMessage("comesFromAddress", "Herkunftsadresse erforderlich"));
		}
	}
	
}
