package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Relation.RELATION;

import java.util.Collections;
import java.util.List;

import ch.openech.client.e44.PersonIdentificationField;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.CodeEditField;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class ChangeGardianEvent extends PersonEventEditor<Relation> {

	public ChangeGardianEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}
	
	@Override
	protected void fillForm(AbstractFormVisual<Relation> formPanel) {
		formPanel.line(new CodeEditField(RELATION.typeOfRelationship, EchCodes.guardianTypeOfRelationship));
		formPanel.line(RELATION.basedOnLaw);
		formPanel.area(new PersonIdentificationField(RELATION.partner));
	}
	
	@Override
	public Relation load() {
		Relation gardian = getPerson().getGardian();
		if (gardian != null) {
			return gardian;
		} else {
			return new Relation();
		}
	}

	@Override
	protected List<String> getXml(Person person, Relation relation, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.changeGardian(person.personIdentification, relation));
	}

}
