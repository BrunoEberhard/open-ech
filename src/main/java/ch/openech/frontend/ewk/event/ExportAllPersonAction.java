package ch.openech.frontend.ewk.event;

import java.io.OutputStream;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.Action;

import ch.openech.transaction.PersonExportStreamProducer;

public class ExportAllPersonAction extends Action {
	protected final String ewkVersion;
	
	public ExportAllPersonAction(String ewkVersion) {
		this.ewkVersion = ewkVersion;
	}
	
	@Override
	public void action() {
		OutputStream outputStream = ClientToolkit.getToolkit().store("Export");
		if (outputStream != null) {
			export(outputStream);
		}
	}

	protected void export(OutputStream outputStream) {
		Backend.getInstance().execute(new PersonExportStreamProducer(ewkVersion, true), outputStream);
	}
}
