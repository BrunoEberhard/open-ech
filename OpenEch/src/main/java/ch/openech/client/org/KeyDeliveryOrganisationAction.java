package ch.openech.client.org;

import java.io.OutputStream;

import ch.openech.business.OrganisationExportStreamProducer;
import ch.openech.mj.backend.Backend;

public class KeyDeliveryOrganisationAction extends ExportAllOrganisationAction {
	
	public KeyDeliveryOrganisationAction(String orgVersion) {
		super(orgVersion);
	}
	
	protected void export(OutputStream outputStream) {
		Backend.getInstance().execute(new OrganisationExportStreamProducer(orgVersion, false), outputStream);
	}
	
}
