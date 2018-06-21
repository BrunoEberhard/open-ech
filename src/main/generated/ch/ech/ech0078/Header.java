package ch.ech.ech0078;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// handmade
public class Header {
	public static final Header $ = Keys.of(Header.class);

	public Object id;
	@NotEmpty
	@Size(255) // unknown
	public String senderId;
	@Size(255) // unknown
	public String originalSenderId;
	@Size(100)
	public String declarationLocalReference;
	public List<String> recipientId;
	@NotEmpty
	@Size(36)
	public String messageId;
	@Size(36)
	public String referenceMessageId;
	@Size(128)
	public String businessProcessId;
	@Size(50)
	public String ourBusinessReferenceId;
	@Size(50)
	public String yourBusinessReferenceId;
	@Size(50)
	public String uniqueIdBusinessTransaction;
	@NotEmpty
	@Size(255) // unknown
	public String messageType;
	@Size(36)
	public String subMessageType;
	public final ch.ech.ech0058.SendingApplication sendingApplication = new ch.ech.ech0058.SendingApplication();
	public ch.ech.ech0058.PartialDelivery partialDelivery;
	@Size(100)
	public String subject;
	public List<PersonObject> object;
	@Size(250)
	public String comment;
	@NotEmpty
	public LocalDateTime messageDate;
	public LocalDateTime initialMessageDate;
	@NotEmpty
	public LocalDate eventDate;
	public LocalDate modificationDate;

	public enum Action { _1, _3, _4, _5, _6, _7, _10, _12;	}
	@NotEmpty
	public Action action;
	public List<Attachment> attachment;
	@NotEmpty
	public Boolean testDeliveryFlag;
	@Size(250)
	public String testData;
	
	public final Extension extension = new Extension();
	
	//
	
	public String getOurBusinessReferenceID() {
		return ourBusinessReferenceId;
	}
	
	public void setOurBusinessReferenceID(String ourBusinessReferenceId) {
		this.ourBusinessReferenceId = ourBusinessReferenceId;
	}
	
	public String getOriginalSenderID() {
		return originalSenderId;
	}
	
	public void setOriginalSenderID(String originalSenderId) {
		this.originalSenderId = originalSenderId;
	}
}