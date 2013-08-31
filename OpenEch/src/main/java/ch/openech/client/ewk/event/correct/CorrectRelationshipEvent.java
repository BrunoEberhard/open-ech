package ch.openech.client.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.e21.NameOfParentsField;
import ch.openech.client.e21.RelationField;
import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectRelationshipEvent extends PersonEventEditor<Person> {

	public CorrectRelationshipEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line(new RelationField(Person.PERSON.relation, echSchema, true));
		boolean includeParents = echSchema.correctRelationshipPersonIncludesParents();
		if (includeParents) {
			formPanel.line(new NameOfParentsField(Person.PERSON.nameOfParents, true));
		}
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		boolean includeParents = echSchema.correctRelationshipPersonIncludesParents();
      return Collections.singletonList(writerEch0020.correctRelationship(changedPerson, includeParents));
	}
	
}
