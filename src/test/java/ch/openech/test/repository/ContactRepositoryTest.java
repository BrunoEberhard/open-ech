package ch.openech.test.repository;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.repository.DataSourceFactory;
import org.minimalj.repository.Repository;
import org.minimalj.repository.sql.SqlRepository;
import org.minimalj.repository.sql.Table;

import ch.openech.model.contact.Contact;
import ch.openech.model.contact.ContactEntry;
import ch.openech.model.contact.ContactEntryType;
import ch.openech.model.types.ContactCategory;


public class ContactRepositoryTest {

	private static Repository repository = new SqlRepository(DataSourceFactory.embeddedDataSource(), Contact.class);
	
	@Test
	public void insertContactWithoutEntriesTest() throws Exception {
		Contact contact = new Contact();
		contact.stringId = "3";
		
		repository.insert(contact);
	}
	
	@Test
	public void insertContactWithEmailTest() throws Exception {
		Contact contact = new Contact();
		contact.stringId = "3";
		
		ContactEntry entry = new ContactEntry();
		entry.typeOfContact = ContactEntryType.Phone;
		entry.categoryCode = ContactCategory.geschaeftlich;

		contact.entries.add(entry);
		
		Object id = repository.insert(contact);
		
		Contact readContact = repository.read(Contact.class, id); 
		
		Assert.assertNotSame(contact, readContact);
		Assert.assertEquals(contact.stringId, readContact.stringId);
		Assert.assertEquals(1, readContact.entries.size());
		Assert.assertEquals(contact.getPhoneList().get(0).categoryCode, readContact.getPhoneList().get(0).categoryCode);
	}
	
}