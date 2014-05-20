package ch.openech.frontend.ewk.event.correct;

import static  ch.openech.model.person.Person.*;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;

import ch.openech.frontend.ewk.event.PersonEventEditor;
import  ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectNameEvent extends PersonEventEditor<Person> {

	public CorrectNameEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> form) {
		form.line(PERSON.originalName);
		form.line(PERSON.alliancePartnershipName);
		form.line(PERSON.aliasName);
		form.line(PERSON.otherName);
		form.line(PERSON.callName);
		form.line(PERSON.foreign.nameOnPassport);
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctName(changedPerson));
	}

}
