package ch.ech.ech0147.t2;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.408083300")
public class Message {
	public static final Message $ = Keys.of(Message.class);

	@NotEmpty
	public ch.ech.ech0147.t0.Header header;
	public final Content content = new Content();
}