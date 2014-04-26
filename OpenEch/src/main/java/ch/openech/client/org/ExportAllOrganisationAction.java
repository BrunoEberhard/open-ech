package ch.openech.client.org;

import java.io.OutputStream;

import ch.openech.business.EchService;
import ch.openech.mj.server.Services;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;

public class ExportAllOrganisationAction extends ResourceAction {
	protected final String orgVersion;
	
	public ExportAllOrganisationAction(String orgVersion) {
		this.orgVersion = orgVersion;
	}
	
	public void action(IComponent context) {
		OutputStream outputStream = ClientToolkit.getToolkit().store(context, "Export");
		if (outputStream != null) {
			export(outputStream);
		}
	}

	protected void export(OutputStream outputStream) {
		Services.get(EchService.class).exportOrg(orgVersion, outputStream);
	}

}
