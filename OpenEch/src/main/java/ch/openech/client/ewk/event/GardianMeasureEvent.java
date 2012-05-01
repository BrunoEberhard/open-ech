package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Relation.RELATION;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.code.EchCodes;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.CodeEditField;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class GardianMeasureEvent extends PersonEventEditor<Relation> {

	public GardianMeasureEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(Form<Relation> formPanel) {
		fillForm(getEchNamespaceContext(), formPanel);
	}
	
	static void fillForm(EchNamespaceContext namespaceContext, Form<Relation> formPanel) {
		// also used by ChangeGardianEvent
		formPanel.line(new CodeEditField(RELATION.typeOfRelationship, EchCodes.guardianTypeOfRelationship));

		formPanel.line(RELATION.basedOnLaw);
		formPanel.setRequired(RELATION.basedOnLaw, namespaceContext.basedOnLawCodeRequired());
		
		if (namespaceContext.gardianMeasureRelationshipHasCare()) {
			formPanel.line(RELATION.care);
		}
		
		formPanel.area(RELATION.partner);
		formPanel.setRequired(RELATION.partner);
	}
	
	@Override
	public Relation load() {
		return new Relation();
	}

	@Override
	protected List<String> getXml(Person person, Relation relation, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.gardianMeasure(person.personIdentification, relation));
	}
	
}
