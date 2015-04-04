package ch.openech.frontend.ewk.event.birth;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.Page;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.BusinessRule;
import org.minimalj.util.CloneHelper;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.frontend.ewk.event.PersonEventEditor;
import ch.openech.frontend.page.PersonPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.common.Place;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class BirthEvent extends XmlEditor<Person> {

	private final OpenEchPreferences preferences;
	
	public BirthEvent(EchSchema ech, OpenEchPreferences preferences) {
		super(ech);
		this.preferences = preferences;
	}

	@Override
	public Form<Person> createForm() {
		return new PersonPanel(PersonEditMode.BIRTH, echSchema);
	}

	@Override
	public Person newInstance() {
		return calculatePresets(preferences);
	}

	@Override
	public List<String> getXml(Person person) throws Exception {
		WriterEch0020 writerEch0020 = echSchema.getWriterEch0020();
		return Collections.singletonList(writerEch0020.birth(person));
	}
	
	@Override
	public Page save(Person object) throws Exception {
		List<String> xmls = getXml(object);
		Person savedPerson = PersonEventEditor.send(xmls);
		return new PersonPage(echSchema, savedPerson);
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		person.validate(resultList);
		validatePlaceOfBirth(person, resultList);
	}

	@BusinessRule("Geburtsort muss bei Erfassung einer Geburt angegeben werden")
	public void validatePlaceOfBirth(Person person, List<ValidationMessage> resultList) {
		if (echSchema.birthPlaceMustNotBeUnknown() && (person.placeOfBirth == null || person.placeOfBirth.isUnknown())) {
			resultList.add(new ValidationMessage(Person.$.placeOfBirth, "Geburtsort fehlt"));
		}
	}
	
	//
	
	private static Person calculatePresets(OpenEchPreferences preferences) {
		Person person = new Person();
		person.editMode = PersonEditMode.BIRTH;
		
		if (preferences.preferencesDefaultsData.residence != null) {
			person.placeOfBirth = new Place();
			person.placeOfBirth.municipalityIdentification = CloneHelper.clone(preferences.preferencesDefaultsData.residence);
		}
		return person;
	}

}
