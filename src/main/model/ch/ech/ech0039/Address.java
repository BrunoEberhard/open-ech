package ch.ech.ech0039;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Address {
	public static final Address $ = Keys.of(Address.class);

	@NotEmpty
	@Size(36)
	public String uuid;
	public TransactionRole transactionRole;
	@Size(255) // unknown
	public String position;
	public ch.ech.ech0046.Contact contact;
}