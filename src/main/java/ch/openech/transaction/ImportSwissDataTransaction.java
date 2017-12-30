package ch.openech.transaction;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.model.EnumUtils;
import org.minimalj.repository.query.By;
import org.minimalj.transaction.Transaction;
import org.minimalj.util.CsvReader;

import ch.openech.model.code.FederalRegister;
import ch.openech.model.common.Canton;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.estate.Locality;
import ch.openech.xml.read.StaxEch0071;
import ch.openech.xml.read.StaxEch0072;

public class ImportSwissDataTransaction implements Transaction<Void> {
	private static final long serialVersionUID = 1L;

	public ImportSwissDataTransaction() {
	}

	@Override
	public Void execute() {
		if (!Backend.find(CountryIdentification.class, By.limit(1)).isEmpty()) {
			return null;
		}
		
		StaxEch0072 staxEch0072 = new StaxEch0072();
		for (CountryIdentification country : staxEch0072.getCountryIdentifications()) {
			Backend.insert(country);
		}
		
		StaxEch0071 staxEch0071 = new StaxEch0071();
		for (Canton canton : staxEch0071.getCantons()) {
			Backend.insert(canton);
		}

		for (MunicipalityIdentification municipality : staxEch0071.getMunicipalityIdentifications()) {
			Backend.insert(municipality);
		}
		
		int pos = 0;
		for (FederalRegister federalRegister : FederalRegister.values()) {
			MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
			municipalityIdentification.id = --pos;
			municipalityIdentification.municipalityName = EnumUtils.getText(federalRegister);
			Backend.insert(municipalityIdentification);
		}
		
		CsvReader csvReader = new CsvReader(getClass().getResourceAsStream("/ch/openech/gemeindenPlz.csv"));
		List<GemeindenPlz> gemeinden = csvReader.readValues(GemeindenPlz.class);
		for (GemeindenPlz gemeinde : gemeinden) {
			Locality locality = new Locality();
			locality.name.language = "de";
			locality.name.nameLong = gemeinde.PLZNAMK;
			locality.id = gemeinde.PLZ4 * 100 + gemeinde.PLZZ;
			if (Backend.read(Locality.class, locality.id) == null) {
				Backend.insert(locality);
			}
		}
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
