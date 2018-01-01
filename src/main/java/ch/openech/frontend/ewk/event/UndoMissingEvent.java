package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.xml.write.WriterEch0020;

public class UndoMissingEvent extends PersonEventEditor<Object> {

	public UndoMissingEvent(PersonPage personPage) {
		super(personPage);
	}

	@Override
	protected List<String> getXml(Person person, Object object, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoMissing(person.personIdentification()));
	}

	@Override
	protected void fillForm(Form<Object> formPanel) {
		formPanel.text("<html><b>Hinweis:</b> Eventuelle Änderungen bei einer Ehe/Partnerschaft müssen zusätzlich erfasst werden.</html>");
	}

	@Override
	public Object createObject() {
		return new Object();
	}

}
