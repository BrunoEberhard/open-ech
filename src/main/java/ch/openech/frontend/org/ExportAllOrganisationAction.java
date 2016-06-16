package ch.openech.frontend.org;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.impl.swing.toolkit.SwingFrontend;

import ch.openech.transaction.OrganisationExportTransaction;

public class ExportAllOrganisationAction extends Action {
	protected final String orgVersion;
	
	public ExportAllOrganisationAction(String orgVersion) {
		this.orgVersion = orgVersion;
	}
	
	@Override
	public void action() {
		if (Frontend.getInstance() instanceof SwingFrontend) {
			SwingFrontend swingFrontend = (SwingFrontend) Frontend.getInstance();
			File file = swingFrontend.showFileDialog("Firmendaten exportieren", "Export");
			if (file != null) {
				try (FileOutputStream fos = new FileOutputStream(file)) {
					Backend.execute(new OrganisationExportTransaction(orgVersion, exportCompleteOrganisation(), fos));
					Frontend.showMessage("Export erfolgreich");
				} catch (IOException e) {
					Frontend.showError("Export nicht möglich\n" + e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		} else {
			// sollte nie vorkommen, da die Action nur beim richtigen Frontend angeboten werden
			Frontend.showError("Nur bei lokaler Installation möglich");
		}
	}
	
	protected boolean exportCompleteOrganisation() {
		return true;
	}
}
