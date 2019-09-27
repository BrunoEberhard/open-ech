package ch.ech.ech0039;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.657")
public class Header {
	public static final Header $ = Keys.of(Header.class);

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
	public LocalDateTime messageDate;
	@NotEmpty
	public Action action;
	@NotEmpty
	public Boolean testDeliveryFlag;
	public List<String> recipientId;
	@Size(255) // unknown
	public String originalSenderId;
	@Size(255) // unknown
	public String declarationLocalReference;
	@Size(255) // unknown
	public String referenceMessageId;
	@Size(50)
	public String uniqueIdBusinessTransaction;
	@Size(50)
	public String ourBusinessReferenceId;
	@Size(50)
	public String yourBusinessReferenceId;
	@Size(255) // unknown
	public String subMessageType;
	public static class PartialDelivery {
		public static final PartialDelivery $ = Keys.of(PartialDelivery.class);

		@NotEmpty
		@Size(50)
		public String uniqueIdBusinessCase;
		@NotEmpty
		public Integer totalNumberOfPackages;
		@NotEmpty
		public Integer numberOfActualPackage;
	}
	public PartialDelivery partialDelivery;
	public Subjects subjects;
	public List<byte[]> object;
	public Comments comments;
	public LocalDateTime initialMessageDate;
	public LocalDateTime eventDate;
	@Size(255) // unknown
	public String eventPeriod;
	public LocalDateTime modificationDate;
	public byte[] testData;
	public Reference reference;
	public byte[] extension;
}