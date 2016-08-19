package ch.openech.test.dm;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.util.CloneHelper;

import  ch.openech.model.common.Address;

public class AddressCloneTest {

	@Test
	public void addressClone() {
		Address address = new Address();
		address.addressLine1 = "line1";
		address.country = "DE";
		
		Address clone = CloneHelper.clone(address);

		// Prüfen ob richtig kopiert wurde
		Assert.assertEquals("line1", clone.addressLine1);

		// Prüfen ob der Clone auch keine referenz mehr hat
		address.addressLine1 = "line1Changed";
		Assert.assertEquals("line1", clone.addressLine1);

		// Dasselbe bei untergeordneten Feldern
		Assert.assertEquals("DE", clone.country);
		address.addressLine1 = "DEChanged";
		Assert.assertEquals("DE", clone.country);
	}
}
