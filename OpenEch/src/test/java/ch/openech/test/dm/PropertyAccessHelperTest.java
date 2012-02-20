package ch.openech.test.dm;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.value.PropertyAccessor;

public class PropertyAccessHelperTest {

	@Test
	public void accessString() {
		Person person = new Person();
		person.originalName = "Tester";
		Assert.assertEquals(person.originalName, PropertyAccessor.get(person, "originalName"));
	}
	
	@Test
	public void accessSubString() {
		Person person = new Person();
		person.maritalStatus.maritalStatus = "T";
		
		Assert.assertEquals(person.maritalStatus.maritalStatus, //
				PropertyAccessor.get(person, "maritalStatus.maritalStatus"));
	}

	@Test
	public void accessSubString2() {
		TestClass2 testObject2 = new TestClass2();
		testObject2.testClass1.s1 = "TestS1";
		
		Assert.assertEquals(testObject2.testClass1.s1, //
				PropertyAccessor.get(testObject2, "testClass1.s1"));
	}
	
	@Test
	public void accessSubString3() {
		TestClass2 testObject2 = new TestClass2();
		testObject2.tc1.s1 = "TestS1";
		
		Assert.assertEquals(testObject2.tc1.s1, //
				PropertyAccessor.get(testObject2, "tc1.s1"));
	}

	@Test
	public void accessViaGetter() {
		TestClass2 testObject2 = new TestClass2();
		testObject2.setS3("access5");
		
		Assert.assertEquals(testObject2.getS3(), //
				PropertyAccessor.get(testObject2, "s3"));
		
		PropertyAccessor.set(testObject2, "s3", "access5a");
		
		Assert.assertEquals(testObject2.getS3(), //
				PropertyAccessor.get(testObject2, "s3"));
	}
	
	@Test
	public void accessViaIs() {
		TestClass2 testObject2 = new TestClass2();
		testObject2.setB1(true);
		
		Assert.assertEquals(testObject2.isB1(), //
				PropertyAccessor.get(testObject2, "b1"));
		
		PropertyAccessor.set(testObject2, "b1", Boolean.FALSE);
		
		Assert.assertEquals(testObject2.isB1(), //
				PropertyAccessor.get(testObject2, "b1"));
	}
	
	@Test
	public void accessChildViaIs() {
		TestClass2 testObject2 = new TestClass2();
		testObject2.testClass1.b2 = true;
		
		Assert.assertEquals(testObject2.testClass1.isB2(), //
				PropertyAccessor.get(testObject2, "testClass1.b2"));

		testObject2.testClass1.b2 = false;
		
		Assert.assertEquals(testObject2.testClass1.isB2(), //
				PropertyAccessor.get(testObject2, "testClass1.b2"));
	}
	
	public static class TestClass1 {
		public String s1;
		private boolean b2;
		
		public boolean isB2() {
			return b2;
		}
	}

	public static class TestClass2 {
		public String s2;
		private String s3;
		private boolean b1;
		public final TestClass1 testClass1 = new TestClass1();
		public final TestClass1 tc1 = new TestClass1();
		
		public String getS3() {
			return s3;
		}
		
		public void setS3(String s3) {
			this.s3 = s3;
		}
		
		public boolean isB1() {
			return b1;
		}
		
		public void setB1(boolean b1) {
			this.b1 = b1;
		}
		
	}

	
}
