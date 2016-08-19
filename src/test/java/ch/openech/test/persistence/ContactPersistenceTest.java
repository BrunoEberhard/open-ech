package ch.openech.test.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.persistence.sql.SqlPersistence;
import org.minimalj.persistence.sql.Table;

import ch.openech.model.contact.Contact;
import ch.openech.model.contact.ContactEntry;
import ch.openech.model.contact.ContactEntryType;
import ch.openech.model.types.ContactCategory;


public class ContactPersistenceTest {

	private static SqlPersistence persistence = new SqlPersistence(SqlPersistence.embeddedDataSource(), Contact.class);
	private static Table<Contact> table = persistence.getTable(Contact.class);
	
	@Test
	public void insertContactWithoutEntriesTest() throws Exception {
		Contact contact = new Contact();
		contact.stringId = "3";
		
		table.insert(contact);
	}
	
	@Test
	public void insertContactWithEmailTest() throws Exception {
		Contact contact = new Contact();
		contact.stringId = "3";
		
		ContactEntry entry = new ContactEntry();
		entry.typeOfContact = ContactEntryType.Phone;
		entry.categoryCode = ContactCategory.geschaeftlich;

		contact.entries.add(entry);
		
		Object id = table.insert(contact);
		
		Contact readContact = table.read(id); 
		
		Assert.assertNotSame(contact, readContact);
		Assert.assertEquals(contact.stringId, readContact.stringId);
		Assert.assertEquals(1, readContact.entries.size());
		Assert.assertEquals(contact.getPhoneList().get(0).categoryCode, readContact.getPhoneList().get(0).categoryCode);
	}
	
}
