package ch.openech.dm.code;

import ch.openech.mj.db.model.CodeValue;

public enum MaritalStatus implements CodeValue {

	Ledig(1), Verheiratet(2), Verwitwet(3), Geschieden(4), Unverheiratet(5), Partnerschaft(6), AufgeloestePartnerschaft(7);
	
	public final String value;
	
	private MaritalStatus(int value) {
		this.value = Integer.toString(value);
	}
	
	@Override
	public String getKey() {
		return value;
	}
	
	public String getDefault() {
		return Ledig.getKey();
	}
			
}
