package ch.openech.dm.types;

public enum PhoneCategory implements EchCode {

	privat_telefon, privat_mobil, privat_fax, privat_internet,
	geschaeftlich_zentrale, geschaeftlich_durchwahl, geschaeftlich_mobil, geschaeftlich_fax, geschaeftlich_internet,
	pager;

	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
			
}
