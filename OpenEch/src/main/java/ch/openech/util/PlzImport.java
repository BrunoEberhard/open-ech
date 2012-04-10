package ch.openech.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ch.openech.dm.common.Plz;

public class PlzImport {
	
	private static PlzImport instance;
	
	private final List<Plz> zipCodes = new ArrayList<Plz>(5500);
	
	private PlzImport() {
		String path = this.getClass().getPackage().getName().replace('.', '/');
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path + "/plz_p1.txt");
		try {
			process(inputStream);
		} catch (Exception x) {
			throw new RuntimeException("Read of zip codes failed", x);
		}
	}

	public static synchronized PlzImport getInstance() {
		if (instance == null) {
			long start = System.nanoTime();
			instance = new PlzImport();
			System.out.println((System.nanoTime() - start) / 1000 / 1000);
		}
		return instance;
	}
	
	
	public List<Plz> getZipCodes() {
		return Collections.unmodifiableList(zipCodes);
	}

	private void process(InputStream inputStream) throws Exception {
		Scanner scanner = new Scanner(inputStream, "ISO-8859-1");
		scanner.useDelimiter("\t|\n");
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			try {
				Plz plz = new Plz();
				plz.onrp = scanner.nextInt();
				scanner.next(); // skip typ
				plz.postleitzahl = scanner.nextInt();
				plz.zusatzziffern = scanner.nextInt();
				scanner.next(); // skip bezeichnung 18
				plz.ortsbezeichnung = scanner.next();
				plz.kanton = scanner.next();
				scanner.next(); // skip sprachecode 1
				scanner.next(); // skip sprachecode 2
				scanner.next(); // skip Sortierfile
				scanner.next(); // skip Briefzustellung
				scanner.next(); // skip Bfs
				scanner.next(); // skip Gültigkeitsdatum
				zipCodes.add(plz);
			} catch (NoSuchElementException x) {
				// Am Schluss des Files befindet sich eine Leerlinie, daher funktioniert hasNextLine nicht als Kontrolle
				break;
			}
		}
		Collections.sort(zipCodes);
	}
	
	public static void main(String... args){
		System.out.println(getInstance().getZipCodes().size());
	}
	
}
