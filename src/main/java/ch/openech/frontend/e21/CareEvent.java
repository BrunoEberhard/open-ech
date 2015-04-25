package ch.openech.frontend.e21;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.EnumFormElement;
import org.minimalj.model.validation.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.BusinessRule;

import ch.openech.frontend.ewk.event.PersonEventEditor;
import ch.openech.model.person.Person;
import ch.openech.model.person.Relation;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.model.types.Sex;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CareEvent extends PersonEventEditor<Relation> {

	public CareEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected List<String> getXml(Person person, Relation relation, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.care(person.personIdentification(), relation));
	}

	@Override
	protected void fillForm(Form<Relation> formPanel) {
		formPanel.line(new EnumFormElement(Relation.$.typeOfRelationship, TypeOfRelationship.PARENT));
		formPanel.line(Relation.$.care);
		formPanel.line(Relation.$.partner);
	}

	@Override
	public void validate(Relation relation, List<ValidationMessage> resultList) {
		super.validate(relation, resultList);
		validateSex(relation, resultList);
		EmptyValidator.validate(resultList, relation, Relation.$.care);
		EmptyValidator.validate(resultList, relation, Relation.$.partner);
	}

	@BusinessRule("Bei Änderung Sorgerecht müssen Eltern das richtige Geschlecht haben")
	private void validateSex(Relation relation, List<ValidationMessage> resultList) {
		if (relation.partner == null) return;
		Sex sexOfPartner = relation.partner.sex;
		if (sexOfPartner != null) {
			TypeOfRelationship code = relation.typeOfRelationship;
			if ((code == TypeOfRelationship.Mutter || code == TypeOfRelationship.Pflegemutter) && sexOfPartner != Sex.weiblich) {
				resultList.add(new ValidationMessage(Relation.$.partner, "Mutter muss weiblich sein."));
			} else if ((code == TypeOfRelationship.Vater || code == TypeOfRelationship.Pflegevater) && sexOfPartner != Sex.maennlich) {
				resultList.add(new ValidationMessage(Relation.$.partner, "Vater muss männlich sein."));
			}
		}
	}

	@Override
	public Relation load() {
		return new Relation();
	}

}
