package ch.openech.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import junit.framework.Assert;

import org.junit.BeforeClass;

import ch.openech.dm.person.Person;
import ch.openech.mj.resources.Resources;
import ch.openech.server.EchServer;
import ch.openech.server.ServerCallResult;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public abstract class AbstractServerTest {

	private static WriterEch0020 writer;
	
	@BeforeClass
	public static void startup() {
		Resources.addResourceBundle(ResourceBundle.getBundle("ch.openech.resources.OpenEch"));
	}
	
	protected String insertPerson(String vn) throws Exception {
		Person person = EchServer.getInstance().getPersistence().person().getByVn(vn);
		if (person != null) {
			return person.getId();
		} else {
			String testName = this.getClass().getSimpleName();
			ServerCallResult result = processFile("testPerson/" + testName + "_" + vn + ".xml");
			return result.createdPersonId;
		}
	}
	
	public static ServerCallResult processFile(String relativFileName) throws IOException {
		InputStream inputStream = AbstractServerTest.class.getResourceAsStream(relativFileName);
		String xml = convertStreamToString(inputStream);
		return EchServer.getInstance().process(xml);
	}
	
	public static ServerCallResult process(String string) throws IOException {
		String validationMessage = EchServer.validate(string);
		boolean valid = EchServer.OK.equals(validationMessage);
		if (!valid) {
			System.out.println(string);
			Assert.fail(validationMessage);
		}
		return EchServer.getInstance().process(string);
	}
	
	public static String convertStreamToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} finally {
			is.close();
		}
		return sb.toString();
	}
	
	public static Person load(String personId) {
		return EchServer.getInstance().getByLocalPersonId(personId);
	}
	
	public static Person getByVn(String vn) {
		return EchServer.getInstance().getPersistence().person().getByVn(vn);
	}
	
	public static WriterEch0020 writer() {
		if (writer == null) {
			EchNamespaceContext echNamespaceContext = EchNamespaceContext.getNamespaceContext(20, "2.2"); // !!
			writer = new WriterEch0020(echNamespaceContext);
		}
		return writer;
	}
	
}