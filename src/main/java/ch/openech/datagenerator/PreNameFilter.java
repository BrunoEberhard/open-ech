package ch.openech.datagenerator;

import java.util.List;

import org.minimalj.util.CsvReader;

public class PreNameFilter {

	public static class PreNameAll {
		public String name, countW, countM;
	}
	
	public static void main(String[] args) {
		CsvReader reader = new CsvReader(PreNameFilter.class.getResourceAsStream("vornamen_ch.csv"));
		List<PreNameAll> names = reader.readValues(PreNameAll.class);
		for (PreNameAll name : names) {
			if (name.countM.equals("*")) {
				name.countM = "0";
			}
			if (name.countW.equals("*")) {
				name.countW = "0";
			}
			if (Integer.valueOf(name.countM) > 1000 || Integer.valueOf(name.countW) > 1000) {
				System.out.println(name.name + "," + name.countW +"," + name.countM);
			}
		}
	}

}
