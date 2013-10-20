package ch.openech.test.persistence;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.dm.contact.ContactEntryType;
import ch.openech.dm.types.ContactCategory;
import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.HistorizedTable;


@Ignore
public class ContactPersistenceTest {

	private static ContactPersistence persistence = new ContactPersistence();
	
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
		entry.typeOfContact = ContactEntryType.Phone;
		entry.categoryCode = ContactCategory.geschaeftlich;

		contact.entries.add(entry);
		
		int id = persistence.contact().insert(contact);
		persistence.commit();
		
		Contact readContact = persistence.contact().read(id); 
		
		Assert.assertNotSame(contact, readContact);
		Assert.assertEquals(contact.stringId, readContact.stringId);
		Assert.assertEquals(1, readContact.entries.size());
		Assert.assertEquals(contact.getPhoneList().get(0).categoryCode, readContact.getPhoneList().get(0).categoryCode);
	}
	
	private static class ContactPersistence extends DbPersistence {
		private final HistorizedTable<Contact> contact;

		public ContactPersistence() {
			contact = addHistorizedClass(Contact.class);
			connect();
		}

		public HistorizedTable<Contact> contact() {
			return contact;
		}
	}
}
