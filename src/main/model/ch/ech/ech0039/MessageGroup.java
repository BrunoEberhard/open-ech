package ch.ech.ech0039;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class MessageGroup {
	public static final MessageGroup $ = Keys.of(MessageGroup.class);

	@NotEmpty
	public Integer messageGroupId;
	@NotEmpty
	public Integer messageTypeId;
}