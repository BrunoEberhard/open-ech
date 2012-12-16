package ch.openech.test.dm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Test;

import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.Person;
import ch.openech.dm.tpn.ThirdPartyMove;
import ch.openech.mj.db.model.EnumUtils;
import ch.openech.mj.db.model.PropertyInterface;
import ch.openech.mj.edit.value.Properties;
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
			testConstructor(clazz);
			testFields(clazz);
		}
	}

	private void testConstructor(Class<?> clazz) {
		if (Enum.class.isAssignableFrom(clazz)) {
			try {
				EnumUtils.createEnum((Class<Enum>) clazz, "Test");
			} catch (Exception e) {
				Assert.fail("Not possible to create runtime instance of enum " + clazz.getName() + ". Possibly ther is no empty constructor");
			}
		} else {
			try {
				Assert.assertTrue(Modifier.isPublic(clazz.getConstructor().getModifiers()));
			} catch (NoSuchMethodException e) {
				Assert.fail(clazz.getName() + " has no public empty constructor");
			}
		}
	}
	
	private void testFields(Class<?> clazz) {
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			testField(field);
		}
	}

	private void testField(Field field) {
		if (FieldUtils.isPublic(field) && !FieldUtils.isStatic(field) && !FieldUtils.isTransient(field)) {
			testFieldType(field);
			testNoMethodsForPublicField(field);
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
		if (Integer.class == fieldType) return true;
		if (Boolean.class == fieldType) return true;
		if (BigDecimal.class == fieldType) return true;
		if (LocalDate.class == fieldType) return true;
		return false;
	}
	
	private void testNoMethodsForPublicField(Field field) {
		PropertyInterface property = Properties.getProperty(field.getDeclaringClass(), field.getName());
		Assert.assertNotNull("No property for " + field.getName(), property);
		Assert.assertFalse("A public attribute must not have getter or setter methods: " + field.getDeclaringClass().getName() + "." + field.getName(), //
				property.getClass().getSimpleName().startsWith("Method"));
	}
}
