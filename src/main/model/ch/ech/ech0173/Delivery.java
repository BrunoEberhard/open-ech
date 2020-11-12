package ch.ech.ech0173;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Delivery {
	public static final Delivery $ = Keys.of(Delivery.class);

	@NotEmpty
	public ch.ech.ech0058.Header deliveryHeader;
	public EventAddressVerification addressVerification;
	public EventAddressVerificationResponse addressVerificationResponse;
	public EventPersonData personData;
	public EventPersonDataResponse personDataResponse;
}