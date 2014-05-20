package ch.openech.frontend.ewk.event;

import java.io.OutputStream;

import org.minimalj.backend.Backend;

import ch.openech.transaction.PersonExportStreamProducer;

public class KeyDeliveryPersonAction extends ExportAllPersonAction {
	
	public KeyDeliveryPersonAction(String ewkVersion) {
		super(ewkVersion);
	}
	
	@Override
	protected void export(OutputStream outputStream) {
		Backend.getInstance().execute(new PersonExportStreamProducer(ewkVersion, false), outputStream);
	}

}
