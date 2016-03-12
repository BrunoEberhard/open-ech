package ch.openech.frontend.ewk.event;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.impl.swing.toolkit.SwingFrontend;
import org.minimalj.transaction.Role;

import ch.openech.OpenEchRoles;
import ch.openech.transaction.PersonExportTransaction;

@Role(OpenEchRoles.importExport)
public class ExportAllPersonAction extends Action {
	protected final String ewkVersion;
	
	public ExportAllPersonAction(String ewkVersion) {
		this.ewkVersion = ewkVersion;
	}
	
	@Override
	public void action() {
		if (Frontend.getInstance() instanceof SwingFrontend) {
			SwingFrontend swingFrontend = (SwingFrontend) Frontend.getInstance();
			File file = swingFrontend.showFileDialog("Personendaten exportieren", "Export");
			if (file != null) {
				try (FileOutputStream fos = new FileOutputStream(file)) {
					Integer exportCount = Backend.getInstance().execute(new PersonExportTransaction(ewkVersion, exportCompletePerson(), fos));
					Frontend.showMessage(exportCount + " Person(en) exportiert");
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

	protected boolean exportCompletePerson() {
		return true;
	}
}
