package ch.openech.client.org;

import java.io.InputStream;

import ch.openech.business.EchService;
import ch.openech.mj.server.Services;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;

public class ImportAllOrganisationAction extends ResourceAction {

	public ImportAllOrganisationAction() {
	}

	@Override
	public void action(IComponent context) {
		InputStream inputStream = ClientToolkit.getToolkit().load(context, "Datei wählen");
		if (inputStream != null) {
			Services.get(EchService.class).importOrg(inputStream);
		}
	}
	
}
