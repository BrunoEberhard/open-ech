package ch.ech.ech0039;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ReportHeader {
	public static final ReportHeader $ = Keys.of(ReportHeader.class);

	public Object id;
	@NotEmpty
	@Size(255) // unknown
	public String senderId;
	@NotEmpty
	@Size(255) // unknown
	public String messageId;
	public final MessageGroup messageGroup = new MessageGroup();
	@NotEmpty
	@Size(7)
	public Integer messageType;
	public final ch.ech.ech0058.SendingApplication sendingApplication = new ch.ech.ech0058.SendingApplication();
	@NotEmpty
	public ReportAction action;
	@NotEmpty
	public Boolean testDeliveryFlag;
	@Size(255) // unknown
	public String recipientId;
	@Size(255) // unknown
	public String referenceMessageId;
	@Size(50)
	public String ourBusinessReferenceId;
	@Size(50)
	public String yourBusinessReferenceId;
	@Size(50)
	public String uniqueIdBusinessTransaction;
	@Size(255) // unknown
	public String subMessageType;
	public List<byte[]> object;
	public LocalDateTime initialMessageDate;
	public byte[] testData;
	public Reference reference;
	public byte[] extension;
}