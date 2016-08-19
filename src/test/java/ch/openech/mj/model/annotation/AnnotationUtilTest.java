package ch.openech.mj.model.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.AnnotationUtil;

import  ch.openech.model.EchFormats;
import  ch.openech.model.common.Address;
import  ch.openech.model.person.Person;

public class AnnotationUtilTest {

	@Test
	public void testSize() {
		int size = AnnotationUtil.getSize(Keys.getProperty(Person.$.aliasName));
		Assert.assertEquals(EchFormats.baseName, size);
	}
	
	@Test
	public void testSizes() {
		int size = AnnotationUtil.getSize(Keys.getProperty(Address.$.street));
		Assert.assertEquals(EchFormats.street, size);
	}

	@Test
	public void testSizes2() {
		int size = AnnotationUtil.getSize(Keys.getProperty(Address.$.houseNumber.dwellingNumber));
		Assert.assertEquals(EchFormats.dwellingNumber, size);
	}
	
}

