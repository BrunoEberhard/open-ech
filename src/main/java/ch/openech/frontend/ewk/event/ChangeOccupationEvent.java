package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.e21.OccupationPanel;
import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Occupation;
import ch.openech.model.person.Person;
import ch.openech.xml.write.WriterEch0020;


// Eigentlich AddOccupationAction, da nur ein Arbeitgeber hinzugefügt werden kann
// Aber die Konvention ist, dass die Klasse gleich heisst wie die XML - Struktur
public class ChangeOccupationEvent extends PersonEventEditor<Occupation> {
	
	public ChangeOccupationEvent(PersonPage personPage) {
		super(personPage);
	}
	
	@Override
	public Form<Occupation> createForm() {
		return new OccupationPanel(echSchema);
	}

	@Override
	protected void fillForm(Form<Occupation> formPanel) {
		// not used
	}
	
	@Override
	public Occupation createObject() {
		return new Occupation(echSchema);
	}

	
//	@Override
//	public Occupation newInstance() {
//		return new Occupation(echSchema);
//	}

	@Override
	protected List<String> getXml(Person person, Occupation occupation, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.changeOccupation(person.personIdentification(), occupation));
	}

}
