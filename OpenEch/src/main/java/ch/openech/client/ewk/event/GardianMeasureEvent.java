package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Relation.*;

import java.util.Collections;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.mj.edit.fields.EnumEditField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.model.EmptyValidator;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class GardianMeasureEvent extends PersonEventEditor<Relation> {

	public GardianMeasureEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
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
			formPanel.line(new EnumEditField(RELATION.typeOfRelationship, TypeOfRelationship.CARE_2_3));
		} else {
			formPanel.line(new EnumEditField(RELATION.typeOfRelationship, TypeOfRelationship.CARE));			
		}

		formPanel.line(RELATION.basedOnLaw);
		if (echSchema.basedOnLawAddOn()) {
			formPanel.line(RELATION.basedOnLawAddOn);
		}
		if (echSchema.gardianMeasureRelationshipHasCare()) {
			formPanel.line(RELATION.care);
		}
		
		formPanel.line(RELATION.partner);
	}
	
	@Override
	public Relation load() {
		return new Relation();
	}
	
	@Override
	public void validate(Relation relation, List<ValidationMessage> resultList) {
		super.validate(relation, resultList);
		if (echSchema.gardianMeasureRelationshipHasCare()) {
			EmptyValidator.validate(resultList, relation, RELATION.care);
		}
		if (!echSchema.gardianRelationshipOptional()) {
			EmptyValidator.validate(resultList, relation, RELATION.partner);
		}
	}

	@Override
	protected List<String> getXml(Person person, Relation relation, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.gardianMeasure(person.personIdentification, relation));
	}
	
}
