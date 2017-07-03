package ch.openech.datagenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// http://www.genealogienetz.de/reg/CH/surcom-m.htm
public class MockName {
		
	public static String officialName() {
		if (names.isEmpty()) {
			readNames();
		}
		return names.get(random.nextInt(names.size()));
	}

	private static Random random = new Random();
	private static List<String> names = new ArrayList<>(200);
	
	private static synchronized void readNames() {
		try (InputStreamReader r = new InputStreamReader(MockName.class.getResourceAsStream("officialnames.txt"), StandardCharsets.UTF_8)) {
	        try (BufferedReader reader = new BufferedReader(r)) {
	            names = new ArrayList<>();
	            while (true) {
	                String line = reader.readLine();
	                if (line == null)
	                    break;
	                names.add(line);
	            }
	        }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static int streetCount = 0;
	
	private enum StreetName {
		Bahnhofstrasse(1368), Hauptstrasse(1269), Dorfstrasse(1193), Industriestrasse(523), Schulstrasse(440),
		Oberdorfstrasse(424), Posstrasse (362), Schulhausstrasse(351), Kirchweg(347), Birkenweg(338),
		Kirchgasse(307), Kirchstrasse(301), Bergstrasse(295), Bahnhofplatz(288), Unterdorfstrasse(284),
		Gartenstrasse(272), Rosenweg(257), Bachstrasse(253), Ringstrasse(248);
		
		private int amount;
		
		private StreetName(int amount) {
			this.amount = amount;
			streetCount += amount;
		}
	}
	
	public static String street() {
		while (true) {
			int r = (int)(Math.random() * streetCount);
			
			for (StreetName name : StreetName.values()) {
				if (r < name.amount) return name.name();
				else r = r - name.amount;
			}
		}
	}
}
