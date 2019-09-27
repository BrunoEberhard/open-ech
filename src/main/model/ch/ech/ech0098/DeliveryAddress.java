package ch.ech.ech0098;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:01.902")
public class DeliveryAddress {
	public static final DeliveryAddress $ = Keys.of(DeliveryAddress.class);

	public final ch.ech.ech0010.MailAddress deliveryAddress = new ch.ech.ech0010.MailAddress();
	public LocalDate addressValidSince;
}