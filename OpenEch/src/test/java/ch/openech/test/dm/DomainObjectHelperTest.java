package ch.openech.test.dm;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.mj.db.model.ColumnAccess;

public class DomainObjectHelperTest {

	@Test
	public void access1() {
		Person person = new Person();
		person.originalName = "Tester";
		Assert.assertEquals(person.originalName, ColumnAccess.getValue(person, "originalName"));
	}
	
	@Test
	public void access2() {
		Person person = new Person();
		person.maritalStatus.maritalStatus = "T";
		
		Assert.assertEquals(person.maritalStatus.maritalStatus, //
				ColumnAccess.getValue(person, "maritalStatus"));
	}

	@Test
	public void access3() {
		TestClass2 testObject2 = new TestClass2();
		testObject2.testClass1.s1 = "TestS1";
		
		Assert.assertEquals(testObject2.testClass1.s1, //
				ColumnAccess.getValue(testObject2, "s1"));
	}
	
	@Test
	public void access4() {
		TestClass2 testObject2 = new TestClass2();
		testObject2.tc1.s1 = "TestS1";
		
		Assert.assertEquals(testObject2.tc1.s1, //
				ColumnAccess.getValue(testObject2, "tc1S1"));
	}

	@Test
	public void accessKeys() {
		Collection<String> keys = ColumnAccess.getKeys(TestClass2.class);
		Assert.assertEquals(3, keys.size());
		Assert.assertTrue(keys.contains("s1"));
		Assert.assertTrue(keys.contains("s2"));
		Assert.assertTrue(keys.contains("tc1S1"));
	}

	
	public static class TestClass1 implements Cloneable {
		public String s1;
	}

	public static class TestClass2 {
		public String s2;
		public final TestClass1 testClass1 = new TestClass1();
		public final TestClass1 tc1 = new TestClass1();
	}

	
}
