package ch.openech.client.ewk.event.birth;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.client.ewk.XmlEditor;
import ch.openech.client.preferences.PreferenceData;
import ch.openech.dm.common.Place;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class BirthEvent extends XmlEditor<Person> {

	public BirthEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	public FormVisual<Person> createForm() {
		return new PersonPanel(PersonPanelType.BIRTH, getEchNamespaceContext());
	}

	@Override
	public Person load() {
		return calculatePresets();
	}

	@Override
	public List<String> getXml(Person person) throws Exception {
		WriterEch0020 writerEch0020 = getEchNamespaceContext().getWriterEch0020();
		return Collections.singletonList(writerEch0020.birth(person));
	}
	
	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		person.validate(resultList);
		validatePlaceOfBirth(person, resultList);
	}

	@BusinessRule("Geburtsort muss bei Erfassung einer Geburt angegeben werden")
	public void validatePlaceOfBirth(Person person, List<ValidationMessage> resultList) {
		if (getEchNamespaceContext().birthPlaceMustNotBeUnknown() && (person.placeOfBirth == null || person.placeOfBirth.isUnknown())) {
			resultList.add(new ValidationMessage("placeOfBirth", "Geburtsort fehlt"));
		}
	}
	
	//
	
	private static Person calculatePresets() {
		Person person = new Person();

		PreferenceData preferenceData = PreferenceData.load();
		if (preferenceData.preferencesDefaultsData.residence != null) {
			person.placeOfBirth = new Place();
			person.placeOfBirth.setMunicipalityIdentification(preferenceData.preferencesDefaultsData.residence);
		}
		return person;
	}

}
