package ch.openech.test.detail;

import org.junit.Assert;
import org.junit.Test;

import ch.openech.xml.write.EchNamespaceUtil;

public class EchNamespaceUtilTest {

	@Test
	public void extractSchemaNumber() {
		Assert.assertEquals(78, EchNamespaceUtil.extractSchemaNumber("http://www.ech.ch/xmlns/eCH-0078/3/eCH-0078-3-0.xsd"));
		Assert.assertEquals(213, EchNamespaceUtil.extractSchemaNumber("http://www.ech.ch/xmlns/eCH-0213-commons/1"));
		Assert.assertEquals(-1, EchNamespaceUtil.extractSchemaNumber("InvalidString"));
	}

	@Test
	public void extractSchemaMajorVersionURI() {
		Assert.assertEquals(5, EchNamespaceUtil.extractSchemaMajorVersion("http://www.ech.ch/xmlns/eCH-0011/5"));
		Assert.assertEquals(1, EchNamespaceUtil.extractSchemaMajorVersion("http://www.ech.ch/xmlns/eCH-0213-commons/1"));
		Assert.assertEquals(-1, EchNamespaceUtil.extractSchemaMajorVersion("InvalidString"));
	}

	@Test
	public void extractSchemaMajorVersionLocation() {
		Assert.assertEquals(2, EchNamespaceUtil.extractSchemaMajorVersion("http://www.ech.ch/xmlns/eCH-0046/2/eCH-0046-2-0.xsd"));
		Assert.assertEquals(1, EchNamespaceUtil.extractSchemaMajorVersion("http://www.ech.ch/xmlns/eCH-0084/1/eCH-0084-commons-1-4.xsd"));
		Assert.assertEquals(-1, EchNamespaceUtil.extractSchemaMajorVersion("InvalidString"));
	}

	@Test
	public void extractSchemaMinorVersion() {
		Assert.assertEquals(7, EchNamespaceUtil.extractSchemaMinorVersion("http://www.ech.ch/xmlns/eCH-0046/2/eCH-0046-2-7.xsd"));
		Assert.assertEquals(4, EchNamespaceUtil.extractSchemaMinorVersion("http://www.ech.ch/xmlns/eCH-0084/1/eCH-0084-commons-1-4.xsd"));
		Assert.assertEquals(-1, EchNamespaceUtil.extractSchemaMinorVersion("InvalidString"));
	}

	@Test
	public void schemaLocation() {
		Assert.assertEquals("http://www.ech.ch/xmlns/eCH-0010/4/eCH-0010-4-1.xsd", EchNamespaceUtil.schemaLocation("http://www.ech.ch/xmlns/eCH-0010/4", "1"));
	}

	@Test
	public void schemaLocation3() {
		Assert.assertEquals("http://www.ech.ch/xmlns/eCH-0123/4/eCH-0123-4-5.xsd", EchNamespaceUtil.schemaLocation(123, "4", "5"));
	}
	
	@Test
	public void schemaURI() {
		Assert.assertEquals("http://www.ech.ch/xmlns/eCH-0010/4", EchNamespaceUtil.schemaURI("http://www.ech.ch/xmlns/eCH-0010/4/eCH-0010-4-1.xsd"));
		Assert.assertEquals("http://www.ech.ch/xmlns/eCH-0123/45", EchNamespaceUtil.schemaURI("http://www.ech.ch/xmlns/eCH-0123/45/eCH-0123-45-6.xsd"));
	}
	
	@Test
	public void schemaURIByNumber() {
		Assert.assertEquals("http://www.ech.ch/xmlns/eCH-0017/9", EchNamespaceUtil.schemaURI(17, "9"));
	}

}
