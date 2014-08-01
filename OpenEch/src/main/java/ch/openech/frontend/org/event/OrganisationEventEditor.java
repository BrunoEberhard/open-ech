package ch.openech.frontend.org.event;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.frontend.toolkit.ClientToolkit;

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

public abstract class OrganisationEventEditor<T> extends XmlEditor<T> implements XmlResult<T> {

	private final Organisation organisation;
	
	protected OrganisationEventEditor(EchSchema echSchema, Organisation organisation) {
		super(echSchema);
		this.organisation = organisation;
		this.organisation.technicalIds.localId.personIdCategory = NamedId.OPEN_ECH_ID_CATEGORY;
		this.organisation.technicalIds.localId.personId = String.valueOf(organisation.id);
	}

	protected static EchSchema getNamespaceContextOrg() {
		OpenEchPreferences preferences = (OpenEchPreferences) ClientToolkit.getToolkit().getApplicationContext().getPreferences();
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
		Long insertId = send(xmls);
		return PageLink.link(OrganisationPage.class, echSchema.getVersion(), insertId.toString());
	}
	
	public static Long send(final List<String> xmls) {
		Long firstInsertId = Backend.getInstance().execute(new OrganisationTransaction(xmls));
		return firstInsertId;
	}
	
}
