package ch.ech.ech0173;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventAddressVerificationResponse {
	public static final EventAddressVerificationResponse $ = Keys.of(EventAddressVerificationResponse.class);

	@NotEmpty
	public AddressVerificationResult adressVerificationResult;
	public TypeOfResidence typeOfResidenceType;
	public byte[] extension;
}