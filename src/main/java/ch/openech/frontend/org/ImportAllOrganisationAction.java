package ch.openech.frontend.org;

import java.io.InputStream;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.Action;

import ch.openech.transaction.OrganisationImportStreamConsumer;

public class ImportAllOrganisationAction extends Action {

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
