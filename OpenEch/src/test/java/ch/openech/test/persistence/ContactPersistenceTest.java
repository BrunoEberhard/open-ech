package ch.openech.test.persistence;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.dm.contact.ContactEntryType;
import ch.openech.dm.types.ContactCategory;
import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.Table;


public class ContactPersistenceTest {

	private static DbPersistence persistence = new DbPersistence(DbPersistence.embeddedDataSource(), Contact.class);
	private static Table<Contact> table = (Table<Contact>) persistence.getTable(Contact.class);
	
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
		
		table.insert(contact);
		
		ContactEntry entry = new ContactEntry();
		entry.typeOfContact = ContactEntryType.Phone;
		entry.categoryCode = ContactCategory.geschaeftlich;

		contact.entries.add(entry);
		
		long id = table.insert(contact);
		
		Contact readContact = table.read(id); 
		
		Assert.assertNotSame(contact, readContact);
		Assert.assertEquals(contact.stringId, readContact.stringId);
		Assert.assertEquals(1, readContact.entries.size());
		Assert.assertEquals(contact.getPhoneList().get(0).categoryCode, readContact.getPhoneList().get(0).categoryCode);
	}
	
}
