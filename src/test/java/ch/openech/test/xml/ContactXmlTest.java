package ch.openech.test.xml;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.minimalj.util.EqualsHelper;

import ch.openech.model.EchSchemaValidation;
import ch.openech.model.common.Address;
import ch.openech.model.contact.Contact;
import ch.openech.model.contact.ContactEntry;
import ch.openech.model.contact.ContactEntryType;
import ch.openech.model.types.ContactCategory;
import ch.openech.xml.read.StaxEch0046;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0046;

public class ContactXmlTest {

	@Test
	public void RoundTripTest() throws Exception {
		WriterEch0046 writer = new WriterEch0046(EchSchema.getNamespaceContext(46, "2.0"));
	
		Contact contact = new Contact();
		contact.stringId = "13";
		
		ContactEntry mail1 = new ContactEntry();
		mail1.typeOfContact = ContactEntryType.Email;
		mail1.dateFrom = LocalDate.of(2003, 2, 1);
		mail1.dateTo = LocalDate.of(2006, 5, 4);
		mail1.categoryCode = ContactCategory.geschaeftlich;
		mail1.value = "mymail@myhost.com";
		contact.entries.add(mail1);
		
		ContactEntry mail2 = new ContactEntry();
		mail2.typeOfContact = ContactEntryType.Email;
		mail2.categoryOther = "OtherMail";
		mail2.value = "otherMail@myhost.com";
		contact.entries.add(mail2);

		ContactEntry addressEntry1 = new ContactEntry();
		addressEntry1.typeOfContact = ContactEntryType.Address;
		addressEntry1.dateFrom = LocalDate.of(2003, 2, 1);
		addressEntry1.dateTo = LocalDate.of(2006, 5, 4);
		addressEntry1.categoryCode = ContactCategory.privat;
		Address address1 = new Address();
		address1.firstName = "Vortester";
		address1.lastName = "Nachtestr";
		address1.addressLine1 = "Addresszeile 1";
		address1.addressLine2 = "Addresszeile 2";
		address1.street = "Teststrasse";
		address1.houseNumber.houseNumber = "42";
		address1.town = "Jona";
		address1.zip = "8645";
		addressEntry1.address = address1;
		contact.entries.add(addressEntry1);
		
		String xml = writer.contactRoot(contact);
		
		String validationMessage = EchSchemaValidation.validate(xml);
		boolean valid = EchSchemaValidation.OK.equals(validationMessage);
		if (!valid) {
			System.out.println(xml);
			System.out.println(validationMessage);
		}
		Assert.assertTrue(valid);
		
		Contact contactOut = new StaxEch0046().read(xml);
		
		Assert.assertEquals(contact.stringId, contactOut.stringId);
		Assert.assertEquals(contact.entries.size(), contactOut.entries.size());
		
		ContactEntry mail1Out = contactOut.getEmailList().get(0);
		Assert.assertEquals(mail1.dateFrom, mail1Out.dateFrom);
		Assert.assertEquals(mail1.dateTo, mail1Out.dateTo);
		Assert.assertEquals(mail1.categoryCode, mail1Out.categoryCode);
		Assert.assertEquals(mail1.categoryOther, mail1Out.categoryOther);
		Assert.assertEquals(mail1.value, mail1Out.value);
		Assert.assertTrue(EqualsHelper.equals(mail1, mail1Out));

		ContactEntry mail2Out = contactOut.getEmailList().get(1);
		Assert.assertEquals(mail2.dateFrom, mail2Out.dateFrom);
		Assert.assertEquals(mail2.dateTo, mail2Out.dateTo);
		Assert.assertEquals(mail2.categoryCode, mail2Out.categoryCode);
		Assert.assertEquals(mail2.categoryOther, mail2Out.categoryOther);
		Assert.assertEquals(mail2.value, mail2Out.value);
		Assert.assertTrue(EqualsHelper.equals(mail2, mail2Out));
		
		ContactEntry addressEntry1Out = contact.getAddressList().get(0);
		Assert.assertTrue(EqualsHelper.equals(addressEntry1, addressEntry1Out));

	}
}
