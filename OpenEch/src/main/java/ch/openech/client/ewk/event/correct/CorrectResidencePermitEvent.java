package ch.openech.client.ewk.event.correct;

import static ch.openech.dm.person.Foreign.FOREIGN;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class CorrectResidencePermitEvent extends PersonEventEditor<Foreign> {
	
	public CorrectResidencePermitEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(Form<Foreign> formPanel) {
		formPanel.line(FOREIGN.residencePermit);
		
		// bei Change ist diese Angabe obligatorisch, bei Correct nicht
		formPanel.line(FOREIGN.residencePermitTill); 
		
		formPanel.setRequired(FOREIGN.residencePermit);
	}

	@Override
	public Foreign load() {
		return getPerson().foreign;
	}

	@Override
	protected List<String> getXml(Person person, Foreign foreign, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctResidencePermit(person, foreign));
	}

}
