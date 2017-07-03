package ch.openech.datagenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.minimalj.util.CsvReader;

/**
 * <OL>
 * <LI>Download from https://www.bfs.admin.ch/bfs/de/home/statistiken/bevoelkerung/geburten-todesfaelle/vornamen-schweiz.assetdetail.1360749.html</LI>
 * <LI>Remove header and footer and save it as .csv
 * <LI>Use PreNameFilter to filter the less common names and replace * with 0
 * </OL>
 *
 */
public class MockPrename {
	public static final Logger LOG = Logger.getLogger(MockPrename.class.getName());
	
	public static class NameWithCount {
		public String name;
		public Integer countW, countM;
	}
	
	private static Random random = new Random();
	private static List<NameWithCount> males = new ArrayList<>(5000);
	private static List<NameWithCount> femals = new ArrayList<>(5000);
	private static int countW, countM;
	
	public static String getFirstName(boolean male) {
		return getName(male).name;
	}
	
	public static NameWithCount getName() {
		return getName(Math.random() < .5);
	}
	
	public static NameWithCount getName(boolean male) {
		if (males.isEmpty()) readNames();
		int position = random.nextInt(male ? countM : countW);
		for (NameWithCount name : male ? males : femals) {
			int count = male ? name.countM : name.countW;
			if (count < position) {
				position -= count;
			} else {
				return name;
			}
		}
		throw new IllegalStateException();
	}
	
	private static synchronized void readNames() {
		try {
			if (!males.isEmpty()) return; // other thread already read the names

			CsvReader reader = new CsvReader(PreNameFilter.class.getResourceAsStream("vornamen.csv"));
			List<NameWithCount> names = reader.readValues(NameWithCount.class);
			
			for (NameWithCount name : names) {
				if (name.countM > 0) {
					males.add(name);
					countM += name.countM;
				} else if (name.countW > 0) {
					femals.add(name);
					countW += name.countW;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
