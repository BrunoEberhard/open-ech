package ch.openech.frontend.ewk.event;

import java.io.InputStream;
import java.util.function.Consumer;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;

import ch.openech.transaction.PersonImportTransaction;

public class ImportAllPersonAction extends Action implements Consumer<InputStream> {

	@Override
	public void action() {
		Frontend.getBrowser().showInputDialog("Personendaten importieren", this);
	}

	@Override
	public void accept(InputStream inputStream) {
		Backend.getInstance().execute(new PersonImportTransaction(inputStream));
	}
}
