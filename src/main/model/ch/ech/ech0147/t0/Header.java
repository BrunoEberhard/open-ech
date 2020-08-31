package ch.ech.ech0147.t0;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.405083900")
public class Header {
	public static final Header $ = Keys.of(Header.class);

	public Object id;
	@NotEmpty
	@Size(255) // unknown
	public String senderId;
	public List<String> recipientId;
	@Size(255) // unknown
	public String originalSenderId;
	@Size(255) // unknown
	public String declarationLocalReference;
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
	public ch.ech.ech0039.Subjects subjects;
	public Object_ object;
	public ch.ech.ech0039.Comments comments;
	@NotEmpty
	public LocalDateTime messageDate;
	public LocalDateTime initialMessageDate;
	public LocalDateTime eventDate;
	@Size(255) // unknown
	public String eventPeriod;
	public LocalDateTime modificationDate;
	@NotEmpty
	public ch.ech.ech0039.Action action;
	@NotEmpty
	public Boolean testDeliveryFlag;
	public ch.ech.ech0039.Reference reference;
}