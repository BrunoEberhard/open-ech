package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class UndoGardianEvent extends PersonEventEditor<Object> {

	public UndoGardianEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected List<String> getXml(Person person, Object object, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoGardian(person.personIdentification));
	}

	@Override
	protected void fillForm(AbstractFormVisual<Object> formPanel) {
		formPanel.text("Soll die vormundschaftliche Massnahme aufgehoben\n(und damit die Beziehung entfernt) werden?");
	}

	@Override
	public Object load() {
		return null;
	}

}