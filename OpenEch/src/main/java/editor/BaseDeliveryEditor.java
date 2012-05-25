package editor;

import java.util.Collections;
import java.util.List;

import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.PageContext;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchSchema;


public class BaseDeliveryEditor extends XmlEditor<Person> {
	
	public BaseDeliveryEditor(PageContext context, String version) {
		super(EchSchema.getNamespaceContext(20, version), (OpenEchPreferences) context.getApplicationContext().getPreferences() );
	}
	
	@Override
	public IForm<Person> createForm() {
		return new PersonPanel(PersonPanelType.BASE_DELIVERY, echSchema);
	}

	@Override
	public Person newInstance() {
		Person person = new Person();
		person.religion = preferences.preferencesDefaultsData.religion;
		person.residence.reportingMunicipality = preferences.preferencesDefaultsData.residence;
		// sonst noch was?
		return person;
	}

	@Override
	public boolean save(Person person) {
		String xml;
		try {
			xml = getXml(person).get(0);
			EchServer.getInstance().process(xml);
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Person konnte nicht gespeichert werden", e);
		}
	}
	
	@Override
	public List<String> getXml(Person person) throws Exception {
		return Collections.singletonList(echSchema.getWriterEch0020().person(person));
	}

}
