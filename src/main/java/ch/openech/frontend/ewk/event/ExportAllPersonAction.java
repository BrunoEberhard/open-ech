package ch.openech.frontend.ewk.event;

import java.io.OutputStream;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.transaction.Role;

import ch.openech.transaction.PersonExportStreamProducer;

@Role("su")
public class ExportAllPersonAction extends Action {
	protected final String ewkVersion;
	
	public ExportAllPersonAction(String ewkVersion) {
		this.ewkVersion = ewkVersion;
	}
	
	@Override
	public void action() {
		OutputStream outputStream = Frontend.getBrowser().store("Export");
		if (outputStream != null) {
			export(outputStream);
		}
	}

	protected void export(OutputStream outputStream) {
		Backend.getInstance().execute(new PersonExportStreamProducer(ewkVersion, true, outputStream));
	}
}
