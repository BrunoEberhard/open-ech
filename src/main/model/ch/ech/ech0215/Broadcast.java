package ch.ech.ech0215;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.244")
public class Broadcast {
	public static final Broadcast $ = Keys.of(Broadcast.class);

	@NotEmpty
	public ch.ech.ech0058.Header header;
	public static class Content {
		public static final Content $ = Keys.of(Content.class);

		@NotEmpty
		@Size(20)
		public String SPIDCategory;
		public static class DateInterval {
			public static final DateInterval $ = Keys.of(DateInterval.class);

			@NotEmpty
			public LocalDate from;
			@NotEmpty
			public LocalDate till;
		}
		public final DateInterval dateInterval = new DateInterval();
		public static class InactivationOfSPID {
			public static final InactivationOfSPID $ = Keys.of(InactivationOfSPID.class);

			@NotEmpty
			public LocalDateTime inactivationTimestamp;
			@NotEmpty
			@Size(36)
			public String inactiveSPID;
			@NotEmpty
			@Size(36)
			public String activeSPID;
		}
		public List<InactivationOfSPID> inactivationOfSPID;
		public static class CancellationOfSPID {
			public static final CancellationOfSPID $ = Keys.of(CancellationOfSPID.class);

			@NotEmpty
			public LocalDateTime cancellationTimestamp;
			@Size(13)
			public Long vn;
			@NotEmpty
			public ch.ech.ech0213commons.VnStatus vnStatus;
			@NotEmpty
			@Size(36)
			public String cancelledSPID;
		}
		public List<CancellationOfSPID> cancellationOfSPID;
		public static class MultipleActiveSPIDs {
			public static final MultipleActiveSPIDs $ = Keys.of(MultipleActiveSPIDs.class);

			@NotEmpty
			public LocalDateTime lastAssociationTimestamp;
			@Size(13)
			public Long vn;
			@NotEmpty
			public List<String> activeSPID;
		}
		public List<MultipleActiveSPIDs> multipleActiveSPIDs;
	}
	@NotEmpty
	public Content content;
	@NotEmpty
	public Integer minorVersion;
}