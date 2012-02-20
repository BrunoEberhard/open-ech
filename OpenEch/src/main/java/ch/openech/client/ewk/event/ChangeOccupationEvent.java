package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Occupation.OCCUPATION;

import java.util.Collections;
import java.util.List;

import ch.openech.client.e10.AddressField;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;


// Eigentlich AddOccupationAction, da nur ein Arbeitgeber hinzugefügt werden kann
// Aber die Konvention ist, dass die Klasse gleich heisst wie die XML - Struktur
public class ChangeOccupationEvent extends PersonEventEditor<Occupation> {
	
	public ChangeOccupationEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}
	
	@Override
	protected void fillForm(AbstractFormVisual<Occupation> formPanel) {
		// TODO das ist das gleiche Formular wie bei OccupationFrame
		formPanel.line(OCCUPATION.kindOfEmployment);
		formPanel.line(OCCUPATION.jobTitle);
		formPanel.line(OCCUPATION.employer);
		formPanel.area(new AddressField(OCCUPATION.placeOfWork, false, false, false));
		formPanel.area(new AddressField(OCCUPATION.placeOfEmployer, false, false, false));
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
