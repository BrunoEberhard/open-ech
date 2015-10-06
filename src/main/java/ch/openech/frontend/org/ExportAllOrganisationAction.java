package ch.openech.frontend.org;

import java.io.OutputStream;
import java.util.function.Consumer;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;

import ch.openech.transaction.OrganisationExportTransaction;

public class ExportAllOrganisationAction extends Action implements Consumer<OutputStream> {
	protected final String orgVersion;
	
	public ExportAllOrganisationAction(String orgVersion) {
		this.orgVersion = orgVersion;
	}
	
	@Override
	public void action() {
		Frontend.getBrowser().showOutputDialog("Firmendaten exportieren", this);
	}

	@Override
	public void accept(OutputStream outputStream) {
		Backend.getInstance().execute(new OrganisationExportTransaction(orgVersion, exportCompleteOrganisation(), outputStream));
	}
	
	protected boolean exportCompleteOrganisation() {
		return true;
	}
}
