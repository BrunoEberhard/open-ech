package ch.openech.xml.model;

import org.junit.Assert;
import org.junit.Test;

import ch.openech.xml.XsdModel;

public class XsdTypeTest {

	@Test
	public void testPackageName1() {
		String namespace = "http://www.ech.ch/xmlns/eCH-0135/1";
		Assert.assertEquals("ch.ech.ech0135.v1", .packageName(namespace));
	}

	@Test
	public void testPackageName2() {
		String namespace = "http://www.ech.ch/xmlns/eCH-0147/T0/1";
		Assert.assertEquals("ch.ech.ech0147.t0.v1", XsdSchema.packageName(namespace));
	}
}
