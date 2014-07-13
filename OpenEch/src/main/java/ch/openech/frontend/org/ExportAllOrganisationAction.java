package ch.openech.frontend.org;

import java.io.OutputStream;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IContext;
import org.minimalj.frontend.toolkit.ResourceAction;

import ch.openech.transaction.OrganisationExportStreamProducer;

public class ExportAllOrganisationAction extends ResourceAction {
	protected final String orgVersion;
	
	public ExportAllOrganisationAction(String orgVersion) {
		this.orgVersion = orgVersion;
	}
	
	public void action(IContext context) {
		OutputStream outputStream = ClientToolkit.getToolkit().store(context, "Export");
		if (outputStream != null) {
			export(outputStream);
		}
	}

	protected void export(OutputStream outputStream) {
		Backend.getInstance().execute(new OrganisationExportStreamProducer(orgVersion, true), outputStream);
	}

}
