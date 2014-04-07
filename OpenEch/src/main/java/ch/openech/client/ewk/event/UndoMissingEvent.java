package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class UndoMissingEvent extends PersonEventEditor<Object> {

	public UndoMissingEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected List<String> getXml(Person person, Object object, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoMissing(person.personIdentification()));
	}

	@Override
	protected void fillForm(Form<Object> formPanel) {
		formPanel.text("<b>Hinweis:</b> Eventuelle Änderungen bei einer Ehe/Partnerschaft müssen zusätzlich erfasst werden.");
	}

	@Override
	public Object load() {
		return new Object();
	}

}
