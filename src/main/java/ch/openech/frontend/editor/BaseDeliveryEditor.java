package ch.openech.frontend.editor;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.form.Form;
import org.minimalj.util.LoggingRuntimeException;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.frontend.page.PersonPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.transaction.PersonTransaction;
import ch.openech.xml.write.EchSchema;


public class BaseDeliveryEditor extends XmlEditor<Person, Person> {
	private final OpenEchPreferences preferences;
	
	public BaseDeliveryEditor(EchSchema ech, OpenEchPreferences preferences) {
		super(ech);
		this.preferences = preferences;
	}
	
	@Override
	public Form<Person> createForm() {
		return new PersonPanel(PersonEditMode.BASE_DELIVERY, echSchema);
	}

	@Override
	public Person createObject() {
		Person person = new Person();
		person.editMode = PersonEditMode.BASE_DELIVERY;
		person.religion = preferences.preferencesDefaultsData.religion;
		person.residence.reportingMunicipality = preferences.preferencesDefaultsData.residence;
		// sonst noch was?
		return person;
	}

    @Override
    public Person save(Person person) {
		String xml = getXml(person).get(0);
		Person savedPerson = Backend.getInstance().execute(new PersonTransaction(xml));
		return savedPerson;
	}
    
    @Override
    protected void finished(Person result) {
    	Frontend.getBrowser().show(new PersonPage(echSchema, result));
    }
	
	@Override
	public List<String> getXml(Person person) {
		try {
			return Collections.singletonList(echSchema.getWriterEch0020().person(person));
		} catch (Exception e) {
			throw new LoggingRuntimeException(e, logger, "XML generation failed");
		}
	}
	
}
