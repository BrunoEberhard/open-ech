package ch.openech.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;

import ch.openech.business.EchService;
import ch.openech.client.OpenEchApplication;
import ch.openech.dm.EchSchemaValidation;
import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.Person;
import ch.openech.mj.server.DbService;
import ch.openech.mj.server.Services;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public abstract class AbstractServerTest {

	private static boolean started = false;
	private static WriterEch0020 writer;
	
	@BeforeClass
	public static void startup() throws Exception {
		if (!started) {
			started = true;
			new OpenEchApplication();
		}
		clear();
	}
	
	public static void clear() {
		Services.get(DbService.class).deleteAll(Person.class);
		Services.get(DbService.class).deleteAll(Organisation.class);
	}

	protected Person insertPerson(String vn) throws Exception {
		List<Person> persons = Services.get(DbService.class).search(Person.class, Person.SEARCH_BY_VN, vn, 2);
		if (persons.size() == 1) {
			return persons.get(0);
		} else if (persons.isEmpty()){
			String testName = this.getClass().getSimpleName();
			return processFile("testPerson/" + testName + "_" + vn + ".xml");
		} else {
			throw new IllegalStateException();
		}
	}
	
	public static Person processFile(String relativFileName) throws IOException {
		InputStream inputStream = AbstractServerTest.class.getResourceAsStream(relativFileName);
		String xml = convertStreamToString(inputStream);
		return Services.get(EchService.class).process(xml);
	}
	
	public static Person process(String string) throws IOException {
		String validationMessage = EchSchemaValidation.validate(string);
		boolean valid = EchSchemaValidation.OK.equals(validationMessage);
		if (!valid) {
			System.out.println(string);
			Assert.fail(validationMessage);
		}
		return Services.get(EchService.class).process(string);
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
	
	public static Person reload(Person person) {
		return Services.get(DbService.class).read(Person.class, person.id);
	}
	
	public static WriterEch0020 writer() {
		if (writer == null) {
			EchSchema echNamespaceContext = EchSchema.getNamespaceContext(20, "2.2"); // !!
			writer = new WriterEch0020(echNamespaceContext);
		}
		return writer;
	}
	
}
