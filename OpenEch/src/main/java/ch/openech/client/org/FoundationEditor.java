package ch.openech.client.org;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.org.OrganisationPanel.OrganisationPanelType;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.server.EchServer;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0148;


public class FoundationEditor extends XmlEditor<Organisation> implements XmlResult<Organisation> {
	private final WriterEch0148 writerEch0148;
	
	public FoundationEditor(EchNamespaceContext echNamespaceContext) {
		super(echNamespaceContext);
		this.writerEch0148 = new WriterEch0148(echNamespaceContext);
	}

	@Override
	public FormVisual<Organisation> createForm() {
		return new OrganisationPanel(OrganisationPanelType.FOUNDATION, getEchNamespaceContext());
	}

	@Override
	public Organisation newInstance() {
		Organisation organisation = new Organisation();
		
		// TODO Presets
		
//		for (String key : getStringKeys()) {
//			String setting = AbstractApplication.preferences().get(key, null);
//			if (setting != null) {
//				set(key, setting);
//			}
//		}
//		if (reportingMunicipalityField != null) {
//			// TODO das sollte das Formpanel von sich aus können
//			MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
//			for (String key : municipalityIdentification.keySet()) {
//				String setting = AbstractApplication.preferences().get(key, null);
//				if (setting != null) {
//					municipalityIdentification.set(key, setting);
//				}
//			}
//			Residence residence = residenceField.getObject();
//			residence.reportingMunicipality = municipalityIdentification;
//			residenceField.setObject(residence);
//		}

		return organisation;
	}

	@Override
	public void validate(Organisation object, List<ValidationMessage> resultList) {
	}

	@Override
	public boolean save(Organisation organisation) {
		String xml;
		try {
			xml = getXml(organisation).get(0);
			EchServer.getInstance().processOrg(xml);
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Organisation konnte nicht gespeichert werden", e);
		}
	}
	
	@Override
	public List<String> getXml(Organisation organisation) throws Exception {
		return Collections.singletonList(writerEch0148.foundation(organisation));
	}

}
