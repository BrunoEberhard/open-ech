package ch.openech.frontend.ewk.event;

public class KeyDeliveryPersonAction extends ExportAllPersonAction {
	
	public KeyDeliveryPersonAction(String ewkVersion) {
		super(ewkVersion);
	}
	
	protected boolean exportCompletePerson() {
		return false;
	}
}
