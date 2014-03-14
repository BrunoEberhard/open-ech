package ch.openech.mj.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Test;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.value.EqualsHelper;

public class SerializationTest {

	@Test
	public void serializeTestClass1() throws Exception {
		SerializationTestClass1 object1 = new SerializationTestClass1();
		object1.s = "Hello";
		object1.i = 23;
		object1.date = new LocalDate();
		object1.l = 456;
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
			
//			for (int i = 0; i<bytes.length && i<100 ; i++) {
//				System.out.println(bytes[i] + " - " + ((char)bytes[i]));
//			}
			
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
		public String s;
		public Integer i;
		public long l;
	}
}

