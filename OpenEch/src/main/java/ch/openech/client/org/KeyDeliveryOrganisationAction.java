package ch.openech.client.org;

import java.io.OutputStream;

import ch.openech.business.EchService;
import ch.openech.mj.server.Services;

public class KeyDeliveryOrganisationAction extends ExportAllOrganisationAction {
	
	public KeyDeliveryOrganisationAction(String orgVersion) {
		super(orgVersion);
	}
	
	protected void export(OutputStream outputStream) {
		Services.get(EchService.class).exportOrgKeys(orgVersion, outputStream);
	}
	
}
