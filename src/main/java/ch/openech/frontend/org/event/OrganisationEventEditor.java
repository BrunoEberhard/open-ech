package ch.openech.frontend.org.event;

import java.util.List;

import org.minimalj.application.Preferences;
import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.form.Form;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.frontend.page.OrganisationPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.common.NamedId;
import ch.openech.model.organisation.Organisation;
import ch.openech.transaction.OrganisationTransaction;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public abstract class OrganisationEventEditor<T> extends XmlEditor<T, Organisation> implements XmlResult<T> {

	private final OrganisationPage organisationPage;
	
	protected OrganisationEventEditor(OrganisationPage organisationPage) {
		super(organisationPage.getEchSchema());
		this.organisationPage = organisationPage;
	}

	protected static EchSchema getNamespaceContextOrg() {
		OpenEchPreferences preferences = Preferences.getPreferences(OpenEchPreferences.class);
		return EchSchema.getNamespaceContext(preferences.applicationSchemaData.schema148);
	}

	@Override
	public Form<T> createForm() {
		EchForm<T> formPanel = new EchForm<T>(echSchema, getFormColumns());
		fillForm(formPanel);
		return formPanel;
	}
	
	protected abstract void fillForm(Form<T> formPanel);

	public Organisation getOrganisation() {
		Organisation organisation = organisationPage.load();
		organisation.technicalIds.localId.personIdCategory = NamedId.OPEN_ECH_ID_CATEGORY;
		organisation.technicalIds.localId.personId = String.valueOf(organisation.id);
		return organisation;
	}
	
	@Override
	public List<String> getXml(T object) throws Exception {
		return getXml(getOrganisation(), object, echSchema.getWriterEch0148());
	}
	
	protected abstract List<String> getXml(Organisation organisation, T object, WriterEch0148 writerEch0148) throws Exception;

	@Override
	public Organisation save(T object)  {
		try {
			List<String> xmls = getXml(object);
			return Backend.getInstance().execute(new OrganisationTransaction(xmls));
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	protected void finished(Organisation organisation) {
		Frontend.getBrowser().show(new OrganisationPage(echSchema, organisation));
	}
	
}
