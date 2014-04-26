package ch.openech.client.ewk.event;

import java.io.OutputStream;

import ch.openech.business.EchService;
import ch.openech.mj.server.Services;

public class KeyDeliveryPersonAction extends ExportAllPersonAction {
	
	public KeyDeliveryPersonAction(String ewkVersion) {
		super(ewkVersion);
	}
	
	@Override
	protected void export(OutputStream outputStream) {
		Services.get(EchService.class).exportKeys(ewkVersion, outputStream);
	}

}
