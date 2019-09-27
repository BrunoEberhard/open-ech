package ch.ech.ech0078;

import java.time.LocalDateTime;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// handmade
public class ReportHeader {
	public static final ReportHeader $ = Keys.of(ReportHeader.class);

	public Object id;
	@NotEmpty
	@Size(255) // unknown
	public String senderId;
	@Size(255) // unknown
	public String recipientId;
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
	public List<PersonObject> object;
	public LocalDateTime initialMessageDate;

	public enum Action { _8, _9, _11, _13;	}
	@NotEmpty
	public Action action;
	@NotEmpty
	public Boolean testDeliveryFlag;
	@Size(250)
	public String testData;
	public byte[] extension;
	
	//
	
	public String getOurBusinessReferenceID() {
		return ourBusinessReferenceId;
	}
	
	public void setOurBusinessReferenceID(String ourBusinessReferenceId) {
		this.ourBusinessReferenceId = ourBusinessReferenceId;
	}
}