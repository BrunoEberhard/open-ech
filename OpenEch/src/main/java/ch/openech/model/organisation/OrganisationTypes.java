package ch.openech.model.organisation;

import ch.openech.model.types.EchCode;

public class OrganisationTypes {

	public static enum FoundationReason implements EchCode {
		neugruendung, spaltung, fusion;
		
		@Override
		public String getValue() {
			return Integer.toString(ordinal() + 1);
		}
	}

	public static enum LiquidationReason implements EchCode {
		uebernahme, fusion, liquidation;
		
		@Override
		public String getValue() {
			return Integer.toString(ordinal() + 1);
		}
	}
	
	public static enum OrganisationEntryStatus implements EchCode {
		unbekannt, eingetragen, nichtEingetragen;
				
		@Override
		public String getValue() {
			return Integer.toString(ordinal());
		}
	}

	public static enum OrganisationStatus implements EchCode {
		aktiv, geloescht;

		@Override
		public String getValue() {
			return Integer.toString(ordinal() + 1);
		}
	}

	public static enum UidregPublicStatus implements EchCode {
		gesperrt, oeffentlich;
				
		@Override
		public String getValue() {
			return Integer.toString(ordinal());
		}
	}

	public static enum UidregLiquidationReason implements EchCode {
		absorptionsfusion, geschaeftsaufgabe, keineBewilligung, geschaeftuebergabe, argeAbgeschlossen, verstorben, dubletteFehler;

		@Override
		public String getValue() {
			return Integer.toString(ordinal() + 1);
		}
	}

	public static enum UidregStatusEnterpriseDetail implements EchCode {
		provisorisch, inReaktivierung, definitiv, inMutation, geloescht, definitivGeloescht, annulliert;

		@Override
		public String getValue() {
			return Integer.toString(ordinal() + 1);
		}
	}
	
	public static enum UidregOrganisationType implements EchCode {
		juristischePerson, universitaererMedizinalberuf, notar, natuerlichePerson, einfacheGesellschaft, oeffentlichRechtlichesUnternehmen, verwaltungseinheit,
		verein, stiftung, auslaendischeNiederlassung;

		@Override
		public String getValue() {
			return Integer.toString(ordinal() + 1);
		}
	}
			
}
