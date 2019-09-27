package ch.ech.ech0058;

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
	public final SendingApplication sendingApplication = new SendingApplication();
	public PartialDelivery partialDelivery;
	@Size(100)
	public String subject;
	@Size(250)
	public String comment;
	@NotEmpty
	public LocalDateTime messageDate;
	public LocalDateTime initialMessageDate;
	public LocalDate eventDate;
	public LocalDate modificationDate;
	@NotEmpty
	public Action action;
	public List<byte[]> attachment;
	@NotEmpty
	public Boolean testDeliveryFlag;
	public Boolean responseExpected;
	public Boolean businessCaseClosed;
	public List<NamedMetaData> namedMetaData;
	public byte[] extension;
	
	// andere (falsche) Schreibweise in früheren Versionen
	
	public String getOriginalSenderID() {
		return originalSenderId;
	}
	
	public void setOriginalSenderID(String originalSenderId) {
		this.originalSenderId = originalSenderId;
	}
	
	public String getOurBusinessReferenceID() {
		return ourBusinessReferenceId;
	}
	
	public void setOurBusinessReferenceID(String ourBusinessReferenceId) {
		this.ourBusinessReferenceId = ourBusinessReferenceId;
	}
	
	// folgende elemente existieren in neueren Versionen nicht mehr
	
	public List<byte[]> object;
	
	public byte[] testData;
}