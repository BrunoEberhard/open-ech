package ch.ech.ech0058;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Info {
	public static final Info $ = Keys.of(Info.class);

	public static class PositiveReport {
		public static final PositiveReport $ = Keys.of(PositiveReport.class);

		@NotEmpty
		public byte[] notice;
		public byte[] data;
	}
	public PositiveReport positiveReport;
	public static class NegativeReport {
		public static final NegativeReport $ = Keys.of(NegativeReport.class);

		@NotEmpty
		public byte[] notice;
		public byte[] data;
	}
	public NegativeReport negativeReport;
}