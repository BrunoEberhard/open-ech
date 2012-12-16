package ch.openech.dm.person.types;

import ch.openech.dm.types.EchCode;

public enum PartnerShipAbolition implements EchCode {

	unbekannt, gerichtlichAufgeloest, ungueltig, verschollen, tod;

	@Override
	public String getValue() {
		if (this == unbekannt) {
			return "9";
		} else {
			return String.valueOf(ordinal());
		}
	}
	
//	PartnerShipAbolition=Grund der Auflösung
//			PartnerShipAbolition.unknownText= -
//			PartnerShipAbolition.default=1
//			PartnerShipAbolition.GERICHTLICH_AUFGELOEST=	Gerichtlich aufgelöste Partnerschaft
//			PartnerShipAbolition.UNGUELTIG=					Ungültigkeitserklärung
//			PartnerShipAbolition.VERSCHOLLEN=				Durch Verschollenerklärung aufgelöste Partnerschaft
//			PartnerShipAbolition.TOD=						Durch Tod aufgelöste Partnerschaft
//			PartnerShipAbolition.UNBEKANNT=					Unbekannt / Andere Gründe

			
}
