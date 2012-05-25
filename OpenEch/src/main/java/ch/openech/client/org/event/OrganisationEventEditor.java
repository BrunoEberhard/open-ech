package ch.openech.client.org.event;

import java.util.List;

import page.OrganisationViewPage;

import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.util.GenericUtils;
import ch.openech.server.EchServer;
import ch.openech.server.ServerCallResult;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public abstract class OrganisationEventEditor<T> extends XmlEditor<T> implements XmlResult<T> {

	private Organisation organisation;
	
	protected OrganisationEventEditor(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}

	protected static EchSchema getNamespaceContextOrg(PageContext context) {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();
		return EchSchema.getNamespaceContext(148, preferences.applicationSchemaData.schema148);
	}

	@Override
	public IForm<T> createForm() {
		@SuppressWarnings("unchecked")
		Class<T> genericClass = (Class<T>) GenericUtils.getGenericClass(getClass());
		EchFormPanel<T> formPanel = new EchFormPanel<T>(genericClass, echSchema, getFormColumns());
		fillForm(formPanel);
		return formPanel;
	}
	
	protected abstract void fillForm(Form<T> formPanel);

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
		setFollowLink(Page.link(OrganisationViewPage.class, echSchema.getVersion(), organisation.getId()));
	}
	
	public Organisation getOrganisation() {
		return organisation;
	}
	
	@Override
	public List<String> getXml(T object) throws Exception {
		return getXml(getOrganisation(), object, echSchema.getWriterEch0148());
	}
	
	protected abstract List<String> getXml(Organisation organisation, T object, WriterEch0148 writerEch0148) throws Exception;

	// TODO merge with XmlEditor
	
	@Override
	public boolean save(T object) {
		List<String> xmls;
		try {
			xmls = getXml(object);
			send(xmls);
			if (object instanceof Organisation) {
				Organisation organisation = (Organisation) object;
				setFollowLink(Page.link(OrganisationViewPage.class, echSchema.getVersion(), organisation.getId()));
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Konnte XML nicht erstellen", e);
		}
	}

	public static boolean send(final List<String> xmls) {
		for (String xml : xmls) {
			if (!send(xml)) {
				return false;
			}
		}
		// TODO
//		if (SedexOutputGenerator.sedexOutputDirectoryAvailable()) {
//			try {
//				generateSedexOutput();
//			} catch (Exception e) {
//				throw new RuntimeException("Fehler bei Sedex Meldung erstellen", e);
//			}
//		}
		return true;
	}
	
	public static boolean send(final String xml) {
		ServerCallResult result = EchServer.getInstance().processOrg(xml);
		if (result.exception != null) {
			throw new RuntimeException("Speichern Fehlgeschlagen: " + result.exception.getMessage(), result.exception);
		}
		return true;
	}
	
}
