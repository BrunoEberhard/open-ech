package ch.openech.mj.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.util.Codes;

import ch.openech.model.common.CountryIdentification;
import ch.openech.test.server.AbstractServerTest;

public class CountryOrderTest extends AbstractServerTest {

	@Test
	public void testCountryOrder() {
		List<CountryIdentification> countries = Codes.get(CountryIdentification.class);
		
		List<CountryIdentification> countriesSorted = new ArrayList<>(countries);
		countriesSorted.sort(new CountryComparator());
		
		Assert.assertArrayEquals(countriesSorted.toArray(), countries.toArray());
	}
	
	private static class CountryComparator implements Comparator<CountryIdentification> {

		@Override
		public int compare(CountryIdentification o1, CountryIdentification o2) {
			return Collator.getInstance().compare(o1.countryNameShort, o2.countryNameShort);
		}
		
	}
	
}
