package ch.openech.client.ewk.event;

import java.io.InputStream;

import ch.openech.business.PersonImportStreamConsumer;
import ch.openech.mj.backend.Backend;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;

public class ImportAllPersonAction extends ResourceAction {

	@Override
	public void action(IComponent context) {
		InputStream inputStream = ClientToolkit.getToolkit().load(context, "Datei wählen");
		if (inputStream != null) {
			Backend.getInstance().execute(new PersonImportStreamConsumer(), inputStream);
		}
	}
	
}
