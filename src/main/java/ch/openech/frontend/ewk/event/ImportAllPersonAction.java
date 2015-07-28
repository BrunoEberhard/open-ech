package ch.openech.frontend.ewk.event;

import java.io.InputStream;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;

import ch.openech.transaction.PersonImportStreamConsumer;

public class ImportAllPersonAction extends Action {

	@Override
	public void action() {
		InputStream inputStream = Frontend.getBrowser().load("Datei wählen");
		if (inputStream != null) {
			Backend.getInstance().execute(new PersonImportStreamConsumer(), inputStream);
		}
	}
	
}
