package ch.openech.test.dm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.Person;
import ch.openech.dm.tpn.ThirdPartyMove;
import ch.openech.mj.util.FieldUtils;
import ch.openech.mj.util.GenericUtils;

public class DomainClassTest {

	private Set<Class<?>> testedClasses = new HashSet<Class<?>>();
	
	@Test
	public void testOpenEchDomainClasses() {
		testDomainClass(Person.class);
		testDomainClass(Organisation.class);
		testDomainClass(ThirdPartyMove.class);
	}

	public void testDomainClass(Class<?> clazz) {
		testedClasses.clear();
		testDomainClassCheckRecursion(clazz);
	}
	
	private void testDomainClassCheckRecursion(Class<?> clazz) {
		if (!testedClasses.contains(clazz)) {
			testedClasses.add(clazz);
			testFields(clazz);
		}
	}

	private void testFields(Class<?> clazz) {
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			testField(field);
		}
	}

	private void testField(Field field) {
		if (FieldUtils.isPublic(field)) {
			testFieldType(field);
		}
	}

	private void testFieldType(Field field) {
		Class<?> fieldType = field.getType();
		String messagePrefix = field.getName() + " of " + field.getDeclaringClass().getName();

		if (fieldType == List.class) {
			Assert.assertTrue(messagePrefix + " must be final (List Fields must be final)", FieldUtils.isFinal(field));
			testListFieldType(field, messagePrefix);
		} else {
			testFieldType(fieldType, messagePrefix);
			// auf leeren Konstruktor prüfen?
		}
	}

	public void testListFieldType(Field field, String messagePrefix) {
		Class<?> listType = null;
		try {
			listType = GenericUtils.getGenericClass(field);
		} catch (Exception x) {
			// silent
		}
		Assert.assertNotNull("Could not evaluate generic of " + messagePrefix, listType);
		messagePrefix = "Generic of " + messagePrefix;
		testFieldType(listType, messagePrefix);
	}
	
	private void testFieldType(Class<?> fieldType, String messagePrefix) {
		if (!isAllowedPrimitive(fieldType)) {
			Assert.assertFalse(messagePrefix + " has invalid Type", fieldType.isPrimitive());
			Assert.assertFalse(messagePrefix + " must not be of an abstract Type", //
					Modifier.isAbstract(fieldType.getModifiers()));
			testDomainClassCheckRecursion(fieldType);
		}
	}

	private static boolean isAllowedPrimitive(Class<?> fieldType) {
		if (String.class == fieldType) return true;
		if (Integer.TYPE == fieldType) return true;
		if (Boolean.TYPE == fieldType) return true;
		if (BigDecimal.class == fieldType) return true;
		return false;
	}
}
