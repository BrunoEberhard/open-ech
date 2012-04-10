package ch.openech.test.xml;

import junit.framework.Assert;

import org.junit.Test;

import ch.openech.dm.common.Address;
import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.mj.db.model.ColumnAccess;
import ch.openech.mj.util.DateUtils;
import ch.openech.server.EchServer;
import ch.openech.xml.read.StaxEch0046;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0046;

public class ContactXmlTest {

	@Test
	public void RoundTripTest() throws Exception {
		WriterEch0046 writer = new WriterEch0046(EchNamespaceContext.getNamespaceContext(46, "2.0"));
	
		Contact contact = new Contact();
		contact.stringId = "13";
		
		ContactEntry mail1 = new ContactEntry();
		mail1.typeOfContact = "E";
		mail1.dateFrom = DateUtils.parseCH("1.2.2003");
		mail1.dateTo = DateUtils.parseCH("4.5.2006");
		mail1.category.code = "2";
		mail1.value = "mymail@myhost.com";
		contact.entries.add(mail1);
		
		ContactEntry mail2 = new ContactEntry();
		mail2.typeOfContact = "E";
		mail2.category.other = "OtherMail";
		mail2.value = "otherMail@myhost.com";
		contact.entries.add(mail2);

		ContactEntry addressEntry1 = new ContactEntry();
		addressEntry1.typeOfContact = "A";
		addressEntry1.dateFrom = DateUtils.parseCH("1.2.2003");
		addressEntry1.dateTo = DateUtils.parseCH("4.5.2006");
		addressEntry1.category.code = "1";
		Address address1 = new Address();
		address1.firstName = "Vortester";
		address1.lastName = "Nachtestr";
		address1.addressLine1 = "Addresszeile 1";
		address1.addressLine2 = "Addresszeile 2";
		address1.street = "Teststrasse";
		address1.houseNumber.houseNumber = "42";
		address1.town = "Jona";
		address1.zip.swissZipCode = "8645";
		addressEntry1.address = address1;
		contact.entries.add(addressEntry1);
		
		String xml = writer.contactRoot(contact);
		
		String validationMessage = EchServer.validate(xml);
		boolean valid = EchServer.OK.equals(validationMessage);
		if (!valid) {
			System.out.println(xml);
			System.out.println(validationMessage);
		}
		Assert.assertTrue(valid);
		
		Contact contactOut = new StaxEch0046().read(xml);
		
		Assert.assertEquals(contact.stringId, contactOut.stringId);
		Assert.assertEquals(contact.getEmailList().size(), contactOut.getEmailList().size());
		Assert.assertEquals(contact.getAddressList().size(), contactOut.getAddressList().size());
		
		ContactEntry mail1Out = contactOut.getEmailList().get(0);
		Assert.assertEquals(mail1.dateFrom, mail1Out.dateFrom);
		Assert.assertEquals(mail1.dateTo, mail1Out.dateTo);
		Assert.assertEquals(mail1.category.code, mail1Out.category.code);
		Assert.assertEquals(mail1.category.other, mail1Out.category.other);
		Assert.assertEquals(mail1.value, mail1Out.value);
		Assert.assertTrue(ColumnAccess.equals(mail1, mail1Out));

		ContactEntry mail2Out = contactOut.getEmailList().get(1);
		Assert.assertEquals(mail2.dateFrom, mail2Out.dateFrom);
		Assert.assertEquals(mail2.dateTo, mail2Out.dateTo);
		Assert.assertEquals(mail2.category.code, mail2Out.category.code);
		Assert.assertEquals(mail2.category.other, mail2Out.category.other);
		Assert.assertEquals(mail2.value, mail2Out.value);
		Assert.assertTrue(ColumnAccess.equals(mail2, mail2Out));
		
		ContactEntry addressEntry1Out = contact.getAddressList().get(0);
		Assert.assertTrue(ColumnAccess.equals(addressEntry1, addressEntry1Out));

	}
}
