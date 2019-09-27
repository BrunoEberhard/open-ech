package ch.ech.ech0147.t0;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.316")
public class ReportHeader {
	public static final ReportHeader $ = Keys.of(ReportHeader.class);

	public Object id;
	@NotEmpty
	@Size(255) // unknown
	public String senderId;
	public List<String> recipientId;
	@NotEmpty
	@Size(255) // unknown
	public String messageId;
	@Size(255) // unknown
	public String referenceMessageId;
	@Size(255) // unknown
	public String uniqueBusinessTransactionId;
	@Size(255) // unknown
	public String ourBusinessReferenceId;
	@Size(255) // unknown
	public String yourBusinessReferenceId;
	@NotEmpty
	public Integer messageType;
	@Size(255) // unknown
	public String subMessageType;
	public final ch.ech.ech0039.MessageGroup messageGroup = new ch.ech.ech0039.MessageGroup();
	public final ch.ech.ech0058.SendingApplication sendingApplication = new ch.ech.ech0058.SendingApplication();
	public Object_ object;
	public LocalDateTime initialMessageDate;
	@NotEmpty
	public ch.ech.ech0039.ReportAction action;
	@NotEmpty
	public Boolean testDeliveryFlag;
	public ch.ech.ech0039.Reference reference;
}