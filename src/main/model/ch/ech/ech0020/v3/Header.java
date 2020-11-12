package ch.ech.ech0020.v3;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
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
	@Size(250)
	public String comment;
	@NotEmpty
	public LocalDateTime messageDate;
	public LocalDateTime initialMessageDate;
	public LocalDate eventDate;
	public LocalDate modificationDate;
	@NotEmpty
	public ch.ech.ech0058.Action action;
	public List<byte[]> attachment;
	@NotEmpty
	public Boolean testDeliveryFlag;
	public Boolean responseExpected;
	public Boolean businessCaseClosed;
	public List<ch.ech.ech0058.NamedMetaData> namedMetaData;
	public byte[] extension;
	public ch.ech.ech0021.DataLock dataLock;
	public LocalDate dataLockValidFrom;
	public LocalDate dataLockValidTill;
}