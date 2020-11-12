package ch.ech.ech0201;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ContactData {
	public static final ContactData $ = Keys.of(ContactData.class);

	public final ch.ech.ech0010.MailAddress contactAddress = new ch.ech.ech0010.MailAddress();
	public LocalDate contactValidFrom;
	public LocalDate contactValidTill;
}