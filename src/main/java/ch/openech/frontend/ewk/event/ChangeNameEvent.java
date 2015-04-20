package ch.openech.frontend.ewk.event;

import static ch.openech.model.person.Person.*;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;

import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class ChangeNameEvent extends PersonEventEditor<Person> {

	public ChangeNameEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> panel) {
		panel.line($.officialName);
		panel.line($.firstName);
		panel.line($.originalName, $.alliancePartnershipName);
		panel.line($.aliasName, $.otherName);
		panel.line($.callName, $.foreign.nameOnPassport);
	    if (echSchema.changeNameWithParents()) {
	    	panel.line($.nameOfParents.father.firstName, $.nameOfParents.mother.firstName);
	    	panel.line($.nameOfParents.father.officialName, $.nameOfParents.mother.officialName);
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
        return Collections.singletonList(writerEch0020.changeName(person.personIdentification(), changedPerson));
	}

}