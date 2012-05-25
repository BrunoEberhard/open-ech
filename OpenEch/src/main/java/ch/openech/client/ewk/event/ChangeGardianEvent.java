package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class ChangeGardianEvent extends PersonEventEditor<Relation> {

	public ChangeGardianEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
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
        return Collections.singletonList(writerEch0020.changeGardian(person.personIdentification, relation));
	}

}
