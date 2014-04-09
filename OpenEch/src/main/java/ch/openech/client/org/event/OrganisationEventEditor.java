package ch.openech.client.org.event;

import java.util.List;

import ch.openech.business.EchService;
import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.ewk.event.EchForm;
import ch.openech.client.page.OrganisationViewPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.common.NamedId;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.server.Services;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public abstract class OrganisationEventEditor<T> extends XmlEditor<T> implements XmlResult<T> {

	private final Organisation organisation;
	
	protected OrganisationEventEditor(EchSchema echSchema, Organisation organisation) {
		super(echSchema);
		this.organisation = organisation;
		this.organisation.technicalIds.localId.personIdCategory = NamedId.OPEN_ECH_ID_CATEGORY;
		this.organisation.technicalIds.localId.personId = String.valueOf(organisation.id);
	}

	protected static EchSchema getNamespaceContextOrg(PageContext context) {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		return EchSchema.getNamespaceContext(preferences.applicationSchemaData.schema148);
	}

	@Override
	public IForm<T> createForm() {
		EchForm<T> formPanel = new EchForm<T>(echSchema, getFormColumns());
		fillForm(formPanel);
		return formPanel;
	}
	
	protected abstract void fillForm(Form<T> formPanel);

	public Organisation getOrganisation() {
		return organisation;
	}
	
	@Override
	public List<String> getXml(T object) throws Exception {
		return getXml(getOrganisation(), object, echSchema.getWriterEch0148());
	}
	
	protected abstract List<String> getXml(Organisation organisation, T object, WriterEch0148 writerEch0148) throws Exception;

	@Override
	public Object save(T object) throws Exception {
		List<String> xmls = getXml(object);
		Organisation organisation = send(xmls);
		return PageLink.link(OrganisationViewPage.class, echSchema.getVersion(), organisation.getId());
	}
	
	public static Organisation send(final List<String> xmls) {
		Organisation firstOrganisation = null;
		for (String xml : xmls) {
			if (firstOrganisation == null) {
				firstOrganisation = Services.get(EchService.class).processOrg(xml);
			} else {
				Services.get(EchService.class).processOrg(xml);
			}
		}
		return firstOrganisation;
	}
	
}
