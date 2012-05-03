package ch.openech.client.ewk;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.IForm;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;


public class BaseDeliveryEditor extends XmlEditor<Person> {
	private final WriterEch0020 writerEch0020;
	
	public BaseDeliveryEditor(String version) {
		super(EchNamespaceContext.getNamespaceContext(20, version));
		this.writerEch0020 = new WriterEch0020(getEchNamespaceContext());
	}
	
	public BaseDeliveryEditor(EchNamespaceContext echNamespaceContext) {
		super(echNamespaceContext);
		this.writerEch0020 = new WriterEch0020(echNamespaceContext);
	}

	@Override
	public IForm<Person> createForm() {
		return new PersonPanel(PersonPanelType.BASE_DELIVERY, getEchNamespaceContext());
	}

	@Override
	public Person newInstance() {
		Person person = new Person();
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
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
		return Collections.singletonList(writerEch0020.person(person));
	}

}
