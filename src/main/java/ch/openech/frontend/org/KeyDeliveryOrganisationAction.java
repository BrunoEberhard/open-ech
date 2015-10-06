package ch.openech.frontend.org;

public class KeyDeliveryOrganisationAction extends ExportAllOrganisationAction {
	
	public KeyDeliveryOrganisationAction(String orgVersion) {
		super(orgVersion);
	}
	
	@Override
	protected boolean exportCompleteOrganisation() {
		return false;
	}
}
