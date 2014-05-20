package ch.openech.frontend.org.event;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.edit.form.IForm;
import org.minimalj.frontend.page.PageContext;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.util.SerializationContainer;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.frontend.page.OrganisationViewPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import  ch.openech.model.common.NamedId;
import  ch.openech.model.organisation.Organisation;
import ch.openech.transaction.OrganisationTransaction;
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
				firstOrganisation = (Organisation) SerializationContainer.unwrap(Backend.getInstance().execute(new OrganisationTransaction(xml)));
			} else {
				Backend.getInstance().execute(new OrganisationTransaction(xml));
			}
		}
		return firstOrganisation;
	}
	
}
