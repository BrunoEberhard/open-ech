package ch.openech.xml.model;

import org.junit.Assert;
import org.junit.Test;

import ch.openech.xml.EchSchemas;

public class XsdTypeTest {

	@Test
	public void testPackageNameModelData() {
		Assert.assertEquals("ch.ech.ech0135", EchSchemas.packageName("http://www.ech.ch/xmlns/eCH-0135/1"));
	}

	public void testPackageNameMutationData() {
		Assert.assertEquals("ch.ech.ech0020.v3", EchSchemas.packageName("http://www.ech.ch/xmlns/eCH-0020/3"));
		Assert.assertEquals("ch.ech.ech0211.v3", EchSchemas.packageName("http://www.ech.ch/xmlns/eCH-0211/3"));
	}

	@Test
	public void testPackageName2() {
		String namespace = "http://www.ech.ch/xmlns/eCH-0147/T0/1";
		Assert.assertEquals("ch.ech.ech0147.t0", EchSchemas.packageName(namespace));
	}
}
