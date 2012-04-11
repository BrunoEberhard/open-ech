package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Person.PERSON;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class ChangeNameEvent extends PersonEventEditor<Person> {

	public ChangeNameEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(AbstractFormVisual<Person> panel) {
		panel.line(PERSON.personIdentification.officialName);
		panel.line(PERSON.personIdentification.firstName);
		panel.line(PERSON.originalName, PERSON.alliancePartnershipName);
		panel.line(PERSON.aliasName, PERSON.otherName);
		panel.line(PERSON.callName, PERSON.foreign.nameOnPassport);
	    if (getEchNamespaceContext().changeNameWithParents()) {
	    	panel.line("fatherFirstNameAtBirth", "motherFirstNameAtBirth");
	    	panel.line("fatherOfficialNameAtBirth", "motherOfficialNameAtBirth");
	    }
	}

	@Override
	protected int getFormColumns() {
		return 2;
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.changeName(person.personIdentification, changedPerson));
	}

}
