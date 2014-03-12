package ch.openech.client.editor;

import java.util.Collections;
import java.util.List;

import ch.openech.business.EchService;
import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.page.PersonViewPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonEditMode;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.server.Services;
import ch.openech.mj.util.LoggingRuntimeException;
import ch.openech.xml.write.EchSchema;


public class BaseDeliveryEditor extends XmlEditor<Person> {
	private final OpenEchPreferences preferences;
	
	public BaseDeliveryEditor(EchSchema ech, OpenEchPreferences preferences) {
		super(ech);
		this.preferences = preferences;
	}
	
	@Override
	public IForm<Person> createForm() {
		return new PersonPanel(PersonEditMode.BASE_DELIVERY, echSchema);
	}

	@Override
	public Person newInstance() {
		Person person = new Person();
		person.editMode = PersonEditMode.BASE_DELIVERY;
		person.religion = preferences.preferencesDefaultsData.religion;
		person.residence.reportingMunicipality = preferences.preferencesDefaultsData.residence;
		// sonst noch was?
		return person;
	}

    @Override
    public Object save(Person person) {
		String xml = getXml(person).get(0);
		person = Services.get(EchService.class).process(xml);
		return PageLink.link(PersonViewPage.class, echSchema.getVersion(), person.getId());
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
