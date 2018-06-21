package ch.ech.ech0058;

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
	@Size(255) // unknown
	public String messageId;
	@Size(255) // unknown
	public String referenceMessageId;
	@Size(50)
	public String ourBusinessReferenceId;
	@Size(50)
	public String yourBusinessReferenceId;
	@Size(50)
	public String uniqueIdBusinessTransaction;
	@NotEmpty
	@Size(7)
	public Integer messageType;
	@Size(255) // unknown
	public String subMessageType;
	public final SendingApplication sendingApplication = new SendingApplication();
	public List<byte[]> object;
	public LocalDateTime initialMessageDate;

	public enum Action { _8, _9, _11;	}
	@NotEmpty
	public Action action;
	@NotEmpty
	public Boolean testDeliveryFlag;
	public byte[] testData;
	public byte[] extension;
	
	public String getOurBusinessReferenceID() {
		return ourBusinessReferenceId;
	}
	
	public void setOurBusinessReferenceID(String ourBusinessReferenceId) {
		this.ourBusinessReferenceId = ourBusinessReferenceId;
	}
}