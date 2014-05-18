package ch.openech.client.ewk.event;

import java.io.OutputStream;

import ch.openech.business.PersonExportStreamProducer;
import ch.openech.mj.backend.Backend;

public class KeyDeliveryPersonAction extends ExportAllPersonAction {
	
	public KeyDeliveryPersonAction(String ewkVersion) {
		super(ewkVersion);
	}
	
	@Override
	protected void export(OutputStream outputStream) {
		Backend.getInstance().execute(new PersonExportStreamProducer(ewkVersion, false), outputStream);
	}

}
