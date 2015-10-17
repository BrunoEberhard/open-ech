package ch.openech.frontend.ewk.event;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.impl.swing.toolkit.SwingFrontend;

import ch.openech.transaction.PersonImportTransaction;

public class ImportAllPersonAction extends Action {

	@Override
	public void action() {
		if (Frontend.getInstance() instanceof SwingFrontend) {
			SwingFrontend swingFrontend = (SwingFrontend) Frontend.getInstance();
			File file = swingFrontend.showFileDialog("Personendaten importieren", "Import");
			if (file != null) {
				try (FileInputStream fis = new FileInputStream(file)) {
					Backend.getInstance().execute(new PersonImportTransaction(fis));
					Frontend.showMessage("Import erfolgreich");
				} catch (IOException e) {
					Frontend.showError("Import nicht möglich\n" + e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		} else {
			// sollte nie vorkommen, da die Action nur beim richtigen Frontend angeboten werden
			Frontend.showError("Nur bei lokaler Installation möglich");
		}
	}
}