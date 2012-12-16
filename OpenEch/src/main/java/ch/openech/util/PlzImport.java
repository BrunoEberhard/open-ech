package ch.openech.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

import ch.openech.dm.common.Zip;

public class PlzImport {
	private static final Logger logger = Logger.getLogger(PlzImport.class.getName());
	private static PlzImport instance;
	
	private final List<Plz> plzList = new ArrayList<Plz>(5500);
	private final List<Zip> zipList = new ArrayList<Zip>(5500);
	private final Map<Integer, Plz> plzByOnrp = new HashMap<Integer, Plz>(10000);
	
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
			logger.fine("Einlesen Plz: " + (System.nanoTime() - start) / 1000 / 1000 + "ms");
		}
		return instance;
	}
	
	public List<Plz> getPlzList() {
		return Collections.unmodifiableList(plzList);
	}

	public List<Zip> getZipList() {
		return Collections.unmodifiableList(zipList);
	}

	public Plz getPlz(int onrp) {
		return plzByOnrp.get(onrp);
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
				plzList.add(plz);
				plzByOnrp.put(plz.onrp, plz);
			} catch (NoSuchElementException x) {
				// Am Schluss des Files befindet sich eine Leerlinie, daher funktioniert hasNextLine nicht als Kontrolle
				break;
			}
		}
		scanner.close();
		Collections.sort(plzList);
		for (Plz plz : plzList) {
			Zip zip = new Zip();
			zip.setPlz(plz);
			zipList.add(zip);
		}
	}
	
	public static void main(String... args){
		System.out.println(getInstance().getPlzList().size());
	}
	
}
