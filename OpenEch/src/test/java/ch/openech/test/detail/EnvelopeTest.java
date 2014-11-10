package ch.openech.test.detail;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import java.time.LocalDateTime;

import ch.openech.model.Envelope;
import ch.openech.test.server.AbstractServerTest;
import ch.openech.xml.read.StaxEch0090;

public class EnvelopeTest  {

	@Test
	public void readEnvelop() throws Exception {
		String relativFileName = "samples/eCH-0020/changeName/envl_ordipro-changeName-40.xml";
		InputStream inputStream = AbstractServerTest.class.getResourceAsStream(relativFileName);
		Envelope envelope = StaxEch0090.process(inputStream);
		
		Assert.assertNotNull(envelope);
		Assert.assertEquals("286", envelope.messageId);
		Assert.assertEquals("20201", envelope.messageType);
		Assert.assertEquals("0", envelope.messageClass);
		Assert.assertEquals("3-CH-6", envelope.senderId);
		Assert.assertEquals("1-1102-1", envelope.recipientId);
		Assert.assertEquals(LocalDateTime.of(2009, 4, 23, 11, 11, 40), envelope.eventDate);
		Assert.assertEquals(LocalDateTime.of(2009, 4, 23, 11, 11, 40), envelope.messageDate);
	}
	
}
