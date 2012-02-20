package ch.openech.client.ewk.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.e21.RelationField;
import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class CorrectRelationshipEvent extends PersonEventEditor<Person> {

	public CorrectRelationshipEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(AbstractFormVisual<Person> formPanel) {
		boolean includeParents = getEchNamespaceContext().correctRelationshipPersonIncludesParents();
		formPanel.area(new RelationField(Person.PERSON.relation, getEchNamespaceContext(), includeParents, true));
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		boolean includeParents = getEchNamespaceContext().correctRelationshipPersonIncludesParents();
      return Collections.singletonList(writerEch0020.correctRelationship(changedPerson, includeParents));
	}
	
}
