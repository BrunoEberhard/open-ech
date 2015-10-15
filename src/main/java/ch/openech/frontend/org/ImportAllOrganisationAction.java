package ch.openech.frontend.org;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.impl.swing.toolkit.SwingFrontend;

import ch.openech.transaction.OrganisationImportTransaction;

public class ImportAllOrganisationAction extends Action {

	@Override
	public void action() {
		if (Frontend.getInstance() instanceof SwingFrontend) {
			SwingFrontend swingFrontend = (SwingFrontend) Frontend.getInstance();
			File file = swingFrontend.showFileDialog("Firmendaten importieren", "Import");
			if (file != null) {
				try (FileInputStream fis = new FileInputStream(file)) {
					Backend.getInstance().execute(new OrganisationImportTransaction(fis));
					Frontend.getBrowser().showMessage("Import erfolgreich");
				} catch (IOException e) {
					Frontend.getBrowser().showError("Import nicht möglich\n" + e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		} else {
			// sollte nie vorkommen, da die Action nur beim richtigen Frontend angeboten werden
			Frontend.getBrowser().showError("Nur bei lokaler Installation möglich");
		}
	}
}
