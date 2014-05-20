package ch.openech.mj.model.annotation;

import junit.framework.Assert;

import org.junit.Test;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.AnnotationUtil;

import  ch.openech.model.EchFormats;
import  ch.openech.model.common.Address;
import  ch.openech.model.person.Person;

public class AnnotationUtilTest {

	@Test
	public void testSize() {
		int size = AnnotationUtil.getSize(Keys.getProperty(Person.PERSON.aliasName));
		Assert.assertEquals(EchFormats.baseName, size);
	}
	
	@Test
	public void testSizes() {
		int size = AnnotationUtil.getSize(Keys.getProperty(Address.ADDRESS.street));
		Assert.assertEquals(EchFormats.street, size);
	}

	@Test
	public void testSizes2() {
		int size = AnnotationUtil.getSize(Keys.getProperty(Address.ADDRESS.houseNumber.dwellingNumber));
		Assert.assertEquals(EchFormats.dwellingNumber, size);
	}
	
}

