package ch.openech.frontend.org;

import java.io.InputStream;
import java.util.function.Consumer;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;

import ch.openech.transaction.OrganisationImportTransaction;

public class ImportAllOrganisationAction extends Action implements Consumer<InputStream> {

	@Override
	public void action() {
		Frontend.getBrowser().showInputDialog("Firmendaten importieren", this);
	}

	@Override
	public void accept(InputStream inputStream) {
		Backend.getInstance().execute(new OrganisationImportTransaction(inputStream));
	}
}
