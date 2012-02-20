package ch.openech.test.persistence;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.server.EchPersistence;


public class ContactPersistenceTest {

	private static EchPersistence persistence;
	
	@BeforeClass
	public static void beforeClass() throws SQLException {
		persistence = new EchPersistence();
		persistence.connect();
	}

	@AfterClass
	public static void afterClass() throws SQLException {
		persistence.commit();
		persistence.disconnect();
	}

	@Test
	public void insertContactWithoutEntriesTest() throws Exception {
		Contact contact = new Contact();
		contact.stringId = "3";
		
		persistence.contact().insert(contact);
		persistence.commit();
	}
	
	@Test
	public void insertContactWithEmailTest() throws Exception {
		Contact contact = new Contact();
		contact.stringId = "3";
		
		persistence.contact().insert(contact);
		
		ContactEntry entry = new ContactEntry();
		entry.category.code = "2";

		contact.entries.add(entry);
		
		int id = persistence.contact().insert(contact);
		persistence.commit();
		
		Contact readContact = persistence.contact().read(id); 
		
		Assert.assertNotSame(contact, readContact);
		Assert.assertEquals(contact.stringId, readContact.stringId);
		Assert.assertEquals(1, readContact.entries.size());
		Assert.assertEquals(contact.entries.get(0).category.code, readContact.entries.get(0).category.code);
		
	}
}
