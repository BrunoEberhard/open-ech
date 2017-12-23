package  ch.openech.model;

import java.time.LocalDateTime;


public class Envelope {

	public String messageId;
	public String messageType;
	public String messageClass;
	public String referenceMessageId;
	public String senderId;
	public String recipientId;
	public LocalDateTime eventDate; // ab V4 nur LocalDate
	public LocalDateTime messageDate;
	public String loopback;
	public String authorise;
	
}
