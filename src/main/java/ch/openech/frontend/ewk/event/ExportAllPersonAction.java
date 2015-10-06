package ch.openech.frontend.ewk.event;

import java.io.OutputStream;
import java.util.function.Consumer;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.transaction.Role;

import ch.openech.transaction.PersonExportTransaction;

@Role("su")
public class ExportAllPersonAction extends Action implements Consumer<OutputStream> {
	protected final String ewkVersion;
	
	public ExportAllPersonAction(String ewkVersion) {
		this.ewkVersion = ewkVersion;
	}
	
	@Override
	public void action() {
		Frontend.getBrowser().showOutputDialog("Personendaten exportieren", this);
	}

	@Override
	public void accept(OutputStream outputStream) {
		Integer exportCount = Backend.getInstance().execute(new PersonExportTransaction(ewkVersion, exportCompletePerson(), outputStream));
		Frontend.getBrowser().showMessage(exportCount + " Person(en) exportiert");
	}
	
	protected boolean exportCompletePerson() {
		return true;
	}
}
