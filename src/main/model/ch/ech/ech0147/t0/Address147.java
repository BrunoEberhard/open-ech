package ch.ech.ech0147.t0;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Address147 {
	public static final Address147 $ = Keys.of(Address147.class);

	public Object id;
	@NotEmpty
	@Size(36)
	public String uuid;
	public ch.ech.ech0039.TransactionRole transactionRole;
	@Size(255) // unknown
	public String position;
	public ch.ech.ech0046.Contact contact;
	public List<ApplicationCustom> applicationCustom;
}