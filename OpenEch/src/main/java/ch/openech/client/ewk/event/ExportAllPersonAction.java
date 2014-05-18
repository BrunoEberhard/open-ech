package ch.openech.client.ewk.event;

import java.io.OutputStream;

import ch.openech.business.PersonExportStreamProducer;
import ch.openech.mj.backend.Backend;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ResourceAction;

public class ExportAllPersonAction extends ResourceAction {
	protected final String ewkVersion;
	
	public ExportAllPersonAction(String ewkVersion) {
		this.ewkVersion = ewkVersion;
	}
	
	@Override
	public void action(IComponent context) {
		OutputStream outputStream = ClientToolkit.getToolkit().store(context, "Export");
		if (outputStream != null) {
			export(outputStream);
		}
	}

	protected void export(OutputStream outputStream) {
		Backend.getInstance().execute(new PersonExportStreamProducer(ewkVersion, true), outputStream);
	}
}
