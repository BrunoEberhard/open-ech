package ch.ech.ech0116.v4;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Response {
	public static final Response $ = Keys.of(Response.class);

	public static class EventReport {
		public static final EventReport $ = Keys.of(EventReport.class);

		@NotEmpty
		public ch.ech.ech0058.Header reportHeader;
		public final ch.ech.ech0058.Info info = new ch.ech.ech0058.Info();
	}
	public final EventReport eventReport = new EventReport();
	public EventFederalRegisterBaseDelivery federalRegisterBaseDelivery;
}