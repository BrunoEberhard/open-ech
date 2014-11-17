package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.model.person.Person;
import ch.openech.model.person.Relation;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class ChangeGardianEvent extends PersonEventEditor<Relation> {

	public ChangeGardianEvent(EchSchema ech, Person person) {
		super(ech, person);
	}
	
	@Override
	protected void fillForm(Form<Relation> formPanel) {
		GardianMeasureEvent.fillForm(echSchema, formPanel);
	}
	
	@Override
	public Relation load() {
		return getPerson().getGardian();
	}

	@Override
	protected List<String> getXml(Person person, Relation relation, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.changeGardian(person.personIdentification(), relation));
	}

	@Override
	public void validate(Relation relation, List<ValidationMessage> resultList) {
		super.validate(relation, resultList);
		if (!echSchema.gardianRelationshipOptional()) {
			EmptyValidator.validate(resultList, relation, Relation.$.partner);
		}
	}

}
