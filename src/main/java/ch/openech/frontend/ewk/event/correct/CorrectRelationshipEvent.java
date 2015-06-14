package ch.openech.frontend.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.e21.NameOfParentsFormElement;
import ch.openech.frontend.e21.RelationFormElement;
import ch.openech.frontend.ewk.event.PersonEventEditor;
import  ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectRelationshipEvent extends PersonEventEditor<Person> {

	public CorrectRelationshipEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line(new RelationFormElement(Person.$.relation, echSchema, true));
		boolean includeParents = echSchema.correctRelationshipPersonIncludesParents();
		if (includeParents) {
			formPanel.line(new NameOfParentsFormElement(Person.$.nameOfParents, true));
		}
	}

	@Override
	public Person createObject() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		boolean includeParents = echSchema.correctRelationshipPersonIncludesParents();
      return Collections.singletonList(writerEch0020.correctRelationship(changedPerson, includeParents));
	}
	
}
