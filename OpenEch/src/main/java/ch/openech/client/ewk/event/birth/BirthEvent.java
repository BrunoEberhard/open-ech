package ch.openech.client.ewk.event.birth;

import java.util.Collections;
import java.util.List;

import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.page.PersonViewPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.common.Place;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonEditMode;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class BirthEvent extends XmlEditor<Person> {

	private final OpenEchPreferences preferences;
	
	public BirthEvent(EchSchema ech, OpenEchPreferences preferences) {
		super(ech);
		this.preferences = preferences;
	}

	@Override
	public IForm<Person> createForm() {
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
	public Object save(Person object) throws Exception {
		if (super.save(object) != null) {
			return PageLink.link(PersonViewPage.class, echSchema.getVersion(), object.getId());
		} else {
			return SAVE_FAILED;
		}
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		person.validate(resultList);
		validatePlaceOfBirth(person, resultList);
	}

	@BusinessRule("Geburtsort muss bei Erfassung einer Geburt angegeben werden")
	public void validatePlaceOfBirth(Person person, List<ValidationMessage> resultList) {
		if (echSchema.birthPlaceMustNotBeUnknown() && (person.placeOfBirth == null || person.placeOfBirth.isUnknown())) {
			resultList.add(new ValidationMessage(Person.PERSON.placeOfBirth, "Geburtsort fehlt"));
		}
	}
	
	//
	
	private static Person calculatePresets(OpenEchPreferences preferences) {
		Person person = new Person();
		person.editMode = PersonEditMode.BIRTH;
		
		if (preferences.preferencesDefaultsData.residence != null) {
			person.placeOfBirth = new Place();
			person.placeOfBirth.setMunicipalityIdentification(preferences.preferencesDefaultsData.residence);
		}
		return person;
	}

}
