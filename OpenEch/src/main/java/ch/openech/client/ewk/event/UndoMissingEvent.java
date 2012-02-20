package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class UndoMissingEvent extends PersonEventEditor<Object> {

	public UndoMissingEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected List<String> getXml(Person person, Object object, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoMissing(person.personIdentification));
	}

	@Override
	protected void fillForm(AbstractFormVisual<Object> formPanel) {
		formPanel.text("<html><b>Hinweis:</b> Eventuelle Änderungen bei einer<p>Ehe/Partnerschaft müssen zusätzlich erfasst werden.</html>");
	}

	@Override
	public Object load() {
		return new Object();
	}

}
