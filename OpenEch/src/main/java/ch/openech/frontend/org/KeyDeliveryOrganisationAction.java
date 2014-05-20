package ch.openech.frontend.org;

import java.io.OutputStream;

import org.minimalj.backend.Backend;

import ch.openech.transaction.OrganisationExportStreamProducer;

public class KeyDeliveryOrganisationAction extends ExportAllOrganisationAction {
	
	public KeyDeliveryOrganisationAction(String orgVersion) {
		super(orgVersion);
	}
	
	protected void export(OutputStream outputStream) {
		Backend.getInstance().execute(new OrganisationExportStreamProducer(orgVersion, false), outputStream);
	}
	
}
