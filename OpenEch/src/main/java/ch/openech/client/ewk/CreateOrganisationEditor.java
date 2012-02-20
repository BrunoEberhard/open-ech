package ch.openech.client.ewk;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.server.EchServer;
import ch.openech.xml.read.StaxEch0108;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0108;


public class CreateOrganisationEditor extends XmlEditor<Organisation> implements XmlResult<Organisation> {

	public CreateOrganisationEditor(EchNamespaceContext echNamespaceContext) {
		super(echNamespaceContext);
	}

	@Override
	public FormVisual<Organisation> createForm() {
		return new OrganisationPanel();
	}

	@Override
	public Organisation load() {
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
	public boolean save(Organisation object) {
		String xml;
		try {
			xml = new WriterEch0108(getEchNamespaceContext()).organisation(object);
			Organisation result = StaxEch0108.process(xml);
			EchServer.getInstance().getPersistence().organisation().insert(result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> getXml(Organisation object) throws Exception {
		return Collections.singletonList(new WriterEch0108(getEchNamespaceContext()).organisation(object));
	}

}
