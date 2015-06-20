package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.xml.write.WriterEch0020;

public class UndoGardianEvent extends PersonEventEditor<Object> {

	public UndoGardianEvent(PersonPage personPage) {
		super(personPage);
	}

	@Override
	protected List<String> getXml(Person person, Object object, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoGardian(person.personIdentification()));
	}

	@Override
	protected void fillForm(Form<Object> formPanel) {
		formPanel.text("Soll die vormundschaftliche Massnahme aufgehoben\n(und damit die Beziehung entfernt) werden?");
	}

	@Override
	public Object createObject() {
		return null;
	}

}
