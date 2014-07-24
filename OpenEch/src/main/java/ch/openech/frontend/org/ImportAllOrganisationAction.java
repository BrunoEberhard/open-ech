package ch.openech.frontend.org;

import java.io.InputStream;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ResourceAction;

import ch.openech.transaction.OrganisationImportStreamConsumer;

public class ImportAllOrganisationAction extends ResourceAction {

	public ImportAllOrganisationAction() {
	}

	@Override
	public void action() {
		InputStream inputStream = ClientToolkit.getToolkit().load("Datei wählen");
		if (inputStream != null) {
			Backend.getInstance().execute(new OrganisationImportStreamConsumer(), inputStream);
		}
	}
	
}
