package ch.openech.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.minimalj.application.Application;
import org.minimalj.backend.Backend;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.transaction.persistence.DeleteAllTransaction;

import ch.openech.OpenEchApplication;
import ch.openech.frontend.org.ImportSwissDataAction;
import ch.openech.model.EchSchemaValidation;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.person.Person;
import ch.openech.transaction.PersonTransaction;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public abstract class AbstractServerTest {

	private static boolean started = false;
	private static WriterEch0020 writer;
	
	@BeforeClass
	public static void startup() throws Exception {
		if (!started) {
			started = true;
			Application.setApplication(new OpenEchApplication());
			
			initCodes();
		}
		clear();
	}

	protected static void initCodes() {
		new ImportSwissDataAction().action();
	}
	
	public static void clear() {
		Backend.getInstance().execute(new DeleteAllTransaction(Person.class));
		Backend.getInstance().execute(new DeleteAllTransaction(Organisation.class));
	}

	protected Person insertPerson(String vn) throws Exception {
		List<Person> persons = Backend.getInstance().read(Person.class, Criteria.search(vn, Person.SEARCH_BY_VN), 2);
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
		return Backend.getInstance().execute(new PersonTransaction(xml));
	}
	
	public static Object process(String string) throws IOException {
		String validationMessage = EchSchemaValidation.validate(string);
		boolean valid = EchSchemaValidation.OK.equals(validationMessage);
		if (!valid) {
			System.out.println(string);
			Assert.fail(validationMessage);
		}
		return Backend.getInstance().execute(new PersonTransaction(string));
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
		return Backend.getInstance().read(Person.class, person.id);
	}
	
	public static WriterEch0020 writer() {
		if (writer == null) {
			EchSchema echNamespaceContext = EchSchema.getNamespaceContext(20, "2.2"); // !!
			writer = new WriterEch0020(echNamespaceContext);
		}
		return writer;
	}
	
}