package ch.openech.client.e21;

import static ch.openech.dm.person.Relation.RELATION;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.mj.edit.fields.EnumEditField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.model.EmptyValidator;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CareEvent extends PersonEventEditor<Relation> {

	public CareEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected List<String> getXml(Person person, Relation relation, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.care(person.personIdentification, relation));
	}

	@Override
	protected void fillForm(Form<Relation> formPanel) {
		formPanel.line(new EnumEditField(RELATION.typeOfRelationship, TypeOfRelationship.PARENT));
		formPanel.line(RELATION.care);
		formPanel.line(RELATION.partner);
	}

	@Override
	public void validate(Relation relation, List<ValidationMessage> resultList) {
		super.validate(relation, resultList);
		validateSex(relation, resultList);
		EmptyValidator.validate(resultList, relation, RELATION.care);
		EmptyValidator.validate(resultList, relation, RELATION.partner);
	}

	@BusinessRule("Bei Änderung Sorgerecht müssen Eltern das richtige Geschlecht haben")
	private void validateSex(Relation relation, List<ValidationMessage> resultList) {
		PersonIdentification person = relation.partner;
		if (person != null) {
			TypeOfRelationship code = relation.typeOfRelationship;
			if ((code == TypeOfRelationship.Mutter || code == TypeOfRelationship.Pflegemutter) && !person.isFemale()) {
				resultList.add(new ValidationMessage(Relation.RELATION.partner, "Mutter muss weiblich sein."));
			} else if ((code == TypeOfRelationship.Vater || code == TypeOfRelationship.Pflegevater) && !person.isMale()) {
				resultList.add(new ValidationMessage(Relation.RELATION.partner, "Vater muss männlich sein."));
			}
		}
	}

	@Override
	public Relation load() {
		return new Relation();
	}

}
