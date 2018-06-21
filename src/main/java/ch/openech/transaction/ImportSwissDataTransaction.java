package ch.openech.transaction;

import org.minimalj.backend.Backend;
import org.minimalj.repository.query.By;
import org.minimalj.transaction.Transaction;

import ch.ech.ech0071.Canton;
import ch.ech.ech0071.Municipality;
import ch.ech.ech0071.Nomenclature;
import ch.ech.ech0072.Countries;
import ch.ech.ech0072.CountryInformation;
import ch.openech.xml.EchReader;


public class ImportSwissDataTransaction implements Transaction<Void> {
	private static final long serialVersionUID = 1L;

	public ImportSwissDataTransaction() {
	}

	@Override
	public Void execute() {
		if (!Backend.find(CountryInformation.class, By.limit(1)).isEmpty()) {
			return null;
		}
		
		try (EchReader reader = new EchReader(getClass().getResourceAsStream("/eCH0072.xml"))) {
			Countries countries = (Countries) reader.read();
			for (CountryInformation country : countries.country) {
				Backend.insert(country);
			}
		}

		try (EchReader reader = new EchReader(getClass().getResourceAsStream("/eCH0071.xml"))) {
			Nomenclature nomenclature = (Nomenclature) reader.read();
			for (Canton canton : nomenclature.cantons.canton) {
				Backend.insert(canton);
			}
			for (Municipality municipality : nomenclature.municipalities.municipality) {
				Backend.insert(municipality);
			}
		}

		/*
		int pos = 0;
		for (FederalRegister federalRegister : FederalRegister.values()) {
			Municipality municipality = new Municipality();
			municipality.id = --pos;
			municipality.municipalityName = EnumUtils.getText(federalRegister);
			Backend.insert(municipality);
		}
		*/
		
//		CsvReader csvReader = new CsvReader(getClass().getResourceAsStream("/ch/openech/gemeindenPlz.csv"));
//		List<GemeindenPlz> gemeinden = csvReader.readValues(GemeindenPlz.class);
//		for (GemeindenPlz gemeinde : gemeinden) {
//			Locality locality = new Locality();
//			// locality.name.language = "de";
////			locality.name.nameLong = gemeinde.PLZNAMK;
////			locality.id = gemeinde.PLZ4 * 100 + gemeinde.PLZZ;
////			if (Backend.read(Locality.class, locality.id) == null) {
////				Backend.insert(locality);
////			}
//			locality.swissZipCode = gemeinde.PLZ4;
//			locality.swissZipCodeAddOn = gemeinde.PLZZ != null && gemeinde.PLZZ != 0 ? "" + gemeinde.PLZZ : null;;
//			locality.name.nameLong = gemeinde.PLZNAMK;
//			if (Backend.read(Locality.class, locality.swissZipCode) == null) {
//				Backend.insert(locality);
//			}
//		}
		return null;
	}
	
	// https://www.bfs.admin.ch/bfs/de/home/grundlagen/agvch/gwr-korrespondenztabelle.html
	public static class GemeindenPlz {
		public Integer PLZ4, PLZZ;
		public String PLZNAMK;
		// public String ANTEIL_GEMEINDE, KTKZ;
		// public Integer GDENR;
		// public String GDENAMK;
	}

	/*
	public static void main(String[] args) {
		CsvReader csvReader = new CsvReader(ImportSwissDataTransaction.class.getResourceAsStream("/ch/openech/gemeindenPlz.csv"));
		List<GemeindenPlz> gemeinden = csvReader.readValues(GemeindenPlz.class);
		System.out.println(gemeinden.get(24).PLZNAMK);
	}
	*/
}
