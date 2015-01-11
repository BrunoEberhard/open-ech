package ch.openech.frontend.ewk.event;

import java.io.InputStream;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ResourceAction;

import ch.openech.transaction.PersonImportStreamConsumer;

public class ImportAllPersonAction extends ResourceAction {

	@Override
	public void action() {
		InputStream inputStream = ClientToolkit.getToolkit().load("Datei wählen");
		if (inputStream != null) {
			Backend.getInstance().execute(new PersonImportStreamConsumer(), inputStream);
		}
	}
	
}
