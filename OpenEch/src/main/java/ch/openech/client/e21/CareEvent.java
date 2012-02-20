package ch.openech.client.e21;

import static ch.openech.dm.person.Relation.RELATION;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.CodeEditField;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.BusinessRule;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class CareEvent extends PersonEventEditor<Relation> {

	public CareEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected List<String> getXml(Person person, Relation relation, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.care(person.personIdentification, relation));
	}

	@Override
	protected void fillForm(AbstractFormVisual<Relation> formPanel) {
		formPanel.line(new CodeEditField(RELATION.typeOfRelationship, EchCodes.careTypeOfRelationship));
		formPanel.line(RELATION.care);
		formPanel.area(RELATION.partner);
	}

	@Override
	public void validate(Relation relation, List<ValidationMessage> resultList) {
		validatePerson(relation, resultList);
		validateSex(relation, resultList);
	}

	private void validatePerson(Relation relation, List<ValidationMessage> resultList) {
		PersonIdentification person = relation.partner;
		if (person == null) {
			resultList.add(new ValidationMessage(RELATION.partner, "Auswahl der Person erforderlich"));
		}
	}
		
	@BusinessRule("Bei Änderung Sorgerecht müssen Eltern das richtige Geschlecht haben")
	private void validateSex(Relation relation, List<ValidationMessage> resultList) {
		PersonIdentification person = relation.partner;
		if (person != null) {
			String code = relation.typeOfRelationship;
			if (StringUtils.equals(code, "3", "6") && !person.isFemale()) {
				resultList.add(new ValidationMessage("partner", "Mutter muss weiblich sein."));
			} else if (StringUtils.equals(code, "4", "5") && !person.isMale()) {
				resultList.add(new ValidationMessage("partner", "Vater muss männlich sein."));
			}
		}
	}

	@Override
	public Relation load() {
		return new Relation();
	}

}
