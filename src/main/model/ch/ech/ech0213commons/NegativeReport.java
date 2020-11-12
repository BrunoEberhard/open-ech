package ch.ech.ech0213commons;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class NegativeReport {
	public static final NegativeReport $ = Keys.of(NegativeReport.class);

	public static class Notice {
		public static final Notice $ = Keys.of(Notice.class);

		@NotEmpty
		public Integer code;
		@Size(2)
		public String descriptionLanguage;
		@Size(300)
		public String codeDescription;
		@Size(5000)
		public String comment;
	}
	public final Notice notice = new Notice();
	@NotEmpty
	public byte[] data;
}