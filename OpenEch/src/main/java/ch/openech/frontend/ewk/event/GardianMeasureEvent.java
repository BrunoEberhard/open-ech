package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.fields.EnumEditField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.model.person.Person;
import ch.openech.model.person.Relation;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class GardianMeasureEvent extends PersonEventEditor<Relation> {

	public GardianMeasureEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Relation> formPanel) {
		fillForm(echSchema, formPanel);
	}
	
	/*
	 * also used by ChangeGardianEvent
	 */
	static void fillForm(EchSchema echSchema, Form<Relation> formPanel) {
		if (echSchema.typeOfRelationship10Exsists()) {
			formPanel.line(new EnumEditField(Relation.$.typeOfRelationship, TypeOfRelationship.CARE_2_3));
		} else {
			formPanel.line(new EnumEditField(Relation.$.typeOfRelationship, TypeOfRelationship.CARE));			
		}

		formPanel.line(Relation.$.basedOnLaw);
		if (echSchema.basedOnLawAddOn()) {
			formPanel.line(Relation.$.basedOnLawAddOn);
		}
		if (echSchema.gardianMeasureRelationshipHasCare()) {
			formPanel.line(Relation.$.care);
		}
		
		formPanel.line(Relation.$.partner);
	}
	
	@Override
	public Relation load() {
		return new Relation();
	}
	
	@Override
	public void validate(Relation relation, List<ValidationMessage> resultList) {
		super.validate(relation, resultList);
		if (echSchema.gardianMeasureRelationshipHasCare()) {
			EmptyValidator.validate(resultList, relation, Relation.$.care);
		}
		if (!echSchema.gardianRelationshipOptional()) {
			EmptyValidator.validate(resultList, relation, Relation.$.partner);
		}
	}

	@Override
	protected List<String> getXml(Person person, Relation relation, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.gardianMeasure(person.personIdentification(), relation));
	}
	
}
