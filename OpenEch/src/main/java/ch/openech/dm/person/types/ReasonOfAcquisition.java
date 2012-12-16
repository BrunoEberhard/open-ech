package ch.openech.dm.person.types;

import ch.openech.dm.types.EchCode;

public enum ReasonOfAcquisition implements EchCode {

	Abstammung(1),
	Heirat(2), 
	Einbuergerung(3),
	Buergerrechtsanerkennung(4),
	Wiedereinbuergerung(5),
	ErleichterteEinbuergerung(6),
	Anerkennung(8),
	Unbekannt(9),
	VonGesetz(10),
	Namensaenderung(11);
	
	private String value;
	
	private ReasonOfAcquisition() {
		this(0);
	}
	
	private ReasonOfAcquisition(int value) {
		this.value = String.valueOf(value);
	}

	public String getValue() {
		return value;
	}
	
//	reasonOfAcquisition.object=Erwerbsgrund
//			reasonOfAcquisition.default=1
//			reasonOfAcquisition.key.0=1
//			reasonOfAcquisition.text.0=Abstammung
//			reasonOfAcquisition.key.1=2
//			reasonOfAcquisition.text.1=Heirat
//			reasonOfAcquisition.key.2=3
//			reasonOfAcquisition.text.2=Einbürgerung
//			reasonOfAcquisition.key.3=4
//			reasonOfAcquisition.text.3=Bürgerrechtsanerkennung
//			reasonOfAcquisition.key.4=5
//			reasonOfAcquisition.text.4=Wiedereinbürgerung/-annahme
//			reasonOfAcquisition.key.5=6
//			reasonOfAcquisition.text.5=Erleichterte Einbürgerung
//			reasonOfAcquisition.key.6=8
//			reasonOfAcquisition.text.6=Anerkennung
//			reasonOfAcquisition.key.7=9
//			reasonOfAcquisition.text.7=Unbekannt
//			reasonOfAcquisition.key.8=10
//			reasonOfAcquisition.text.8=Erwerb von Gesetzes wegen
//			reasonOfAcquisition.key.9=11
//			reasonOfAcquisition.text.9=Namensänderung mit Bürgerrechtswirkung
//
			
}
