package ch.openech.test.dm;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.common.Address;
import ch.openech.mj.edit.value.CloneHelper;

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
