package ch.openech.mj.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.minimalj.util.EqualsHelper;
import org.minimalj.util.SerializationInputStream;
import org.minimalj.util.SerializationOutputStream;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.person.Person;

public class SerializationTest {

	@Test
	public void serializeTestClass1() throws Exception {
		SerializationTestClass1 object1 = new SerializationTestClass1();
		object1.s = "Hello";
		object1.i = 23;
		object1.date = new LocalDate();
		object1.l = 456;
		SerializationTestClass2 objectInList = new SerializationTestClass2();
		objectInList.s = "Marco";
		object1.list.add(objectInList);
		objectInList = new SerializationTestClass2();
		objectInList.s = "Bruno";
		object1.list.add(objectInList);
		object1.b = Boolean.TRUE;
		object1.b2 = false;
		SerializationTestClass1 object2 = serialize(SerializationTestClass1.class, object1);
		Assert.assertTrue(EqualsHelper.equals(object1, object2));
	}
	
	@Test
	public void serializePerson() throws Exception {
		Person person = DataGenerator.person();
		serialize(Person.class, person);
	}

	@Test
	public void serializePersonAsArgument() throws Exception {
		Person person = DataGenerator.person();
		serializeAsArgument(person);
	}
	

	@SuppressWarnings("unchecked")
	private <T> T serialize(Class<T> clazz, T object) throws Exception {
		try (ByteArrayOutputStream s = new ByteArrayOutputStream()) {
			SerializationOutputStream sos = new SerializationOutputStream(s);
			sos.write(object);
			s.flush();
			byte[] bytes = s.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
				SerializationInputStream sis = new SerializationInputStream(in);
				return (T) sis.read(clazz);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T serializeAsArgument(T object) throws Exception {
		try (ByteArrayOutputStream s = new ByteArrayOutputStream()) {
			SerializationOutputStream sos = new SerializationOutputStream(s);
			sos.writeArgument(object);
			s.flush();
			byte[] bytes = s.toByteArray();
			
			try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
				SerializationInputStream sis = new SerializationInputStream(in);
				return (T) sis.readArgument();
			}
		}
	}

	
	public static class SerializationTestClass1 {
		public Boolean b;
		public boolean b2;
		public LocalDate date;
		public final List<SerializationTestClass2> list = new ArrayList<>();
		public String s;
		public Integer i;
		public long l;
	}
	
	public static class SerializationTestClass2 {
		public String s;
	}

}

