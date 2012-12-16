package ch.openech.dm;

import org.joda.time.LocalDateTime;


public class Envelope {

	public String messageId;
	public String messageType;
	public String messageClass;
	public String referenceMessageId;
	public String senderId;
	public String recipientId;
	public LocalDateTime eventDate;
	public LocalDateTime messageDate;
	public String loopback;
	public String authorise;
	
}
