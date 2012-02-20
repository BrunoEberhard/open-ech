package ch.openech.test.dm;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.common.Address;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.value.DomainObjectState;

public class DomainObjectStateTest {

	@Test
	public void testRestoreAttribute() {
		Address address = new Address();
		address.firstName = "Bruno";
		DomainObjectState state = new DomainObjectState(address);
		address.firstName = "Brunö";
		state.restore();
		Assert.assertEquals("Bruno", address.firstName);
	}
	
	@Test
	public void testRestoreContainedObject() {
		Address address = new Address();
		address.houseNumber.houseNumber = "10";
		DomainObjectState state = new DomainObjectState(address);
		address.houseNumber.houseNumber = "11";
		state.restore();
		Assert.assertEquals("10", address.houseNumber.houseNumber);
	}

	@Test
	public void testRestoreList() {
		Person person = new Person();
		person.occupation.add(new Occupation());
		person.occupation.add(new Occupation());
		DomainObjectState state = new DomainObjectState(person);
		person.occupation.clear();
		state.restore();
		Assert.assertEquals(2, person.occupation.size());
	}
	
	@Test
	public void testRestoreListContentAttribute() {
		Person person = new Person();
		Occupation occupation = new Occupation();
		occupation.employer = "Alter Arbeitgeber";
		occupation.placeOfWork = new Address();
		occupation.placeOfWork.addressLine2 = "Zeile2";
		person.occupation.add(new Occupation());
		person.occupation.add(occupation);
		DomainObjectState state = new DomainObjectState(person);

		person.occupation.clear();
		occupation.placeOfWork.addressLine2 = "Zeile2a";
		occupation.employer = "Neuer Arbeitgeber";

		state.restore();
		Assert.assertEquals("Alter Arbeitgeber", person.occupation.get(1).employer);
		Assert.assertEquals("Zeile2", person.occupation.get(1).placeOfWork.addressLine2);
	}
	
	@Test
	public void testRestoreListContentContent1() {
		Person person = new Person();
		Occupation occupation = new Occupation();
		occupation.placeOfWork = new Address();
		occupation.placeOfWork.addressLine2 = "Zeile2";
		person.occupation.add(new Occupation());
		person.occupation.add(occupation);
		DomainObjectState state = new DomainObjectState(person);

		person.occupation.clear();
		occupation.placeOfWork = null;

		state.restore();
		Assert.assertEquals("Zeile2", person.occupation.get(1).placeOfWork.addressLine2);
	}

}
