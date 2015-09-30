package ch.openech.frontend.ewk.event.birth;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.sql.EmptyObjects;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.form.Form;
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

public class BirthEvent extends XmlEditor<Person, Person> {

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
	public Person createObject() {
		return calculatePresets(preferences);
	}

	@Override
	public List<String> getXml(Person person) throws Exception {
		WriterEch0020 writerEch0020 = echSchema.getWriterEch0020();
		return Collections.singletonList(writerEch0020.birth(person));
	}
	
	@Override
	public Person save(Person object) {
		try {
			List<String> xmls = getXml(object);
			return PersonEventEditor.send(xmls);
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	protected void finished(Person result) {
		Frontend.getBrowser().show(new PersonPage(echSchema, result));
	}
	
	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		person.validate(resultList);
		validatePlaceOfBirth(person, resultList);
	}

	@BusinessRule("Geburtsort muss bei Erfassung einer Geburt angegeben werden")
	public void validatePlaceOfBirth(Person person, List<ValidationMessage> resultList) {
		if (echSchema.birthPlaceMustNotBeUnknown()) {
			if (person.placeOfBirth == null || EmptyObjects.isEmpty(person.placeOfBirth.countryIdentification)) {
				resultList.add(new ValidationMessage(Person.$.placeOfBirth, "Geburtsland fehlt"));
			}
			if (person.placeOfBirth != null && person.placeOfBirth.isSwiss() && person.placeOfBirth.municipalityIdentification == null) {
				resultList.add(new ValidationMessage(Person.$.placeOfBirth, "Für Geburtsland Schweiz muss ein gültiger Ort eingetragen werden"));
			}
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
