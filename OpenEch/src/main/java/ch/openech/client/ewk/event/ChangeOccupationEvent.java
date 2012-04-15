package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.e21.OccupationPanel;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;


// Eigentlich AddOccupationAction, da nur ein Arbeitgeber hinzugefügt werden kann
// Aber die Konvention ist, dass die Klasse gleich heisst wie die XML - Struktur
public class ChangeOccupationEvent extends PersonEventEditor<Occupation> {
	
	public ChangeOccupationEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}
	
	@Override
	public FormVisual<Occupation> createForm() {
		return new OccupationPanel(getEchNamespaceContext());
	}

	@Override
	protected void fillForm(AbstractFormVisual<Occupation> formPanel) {
		// not used
	}
	
	@Override
	public Occupation load() {
		return new Occupation();
	}

	@Override
	protected List<String> getXml(Person person, Occupation occupation, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.changeOccupation(person.personIdentification, occupation));
	}

}
