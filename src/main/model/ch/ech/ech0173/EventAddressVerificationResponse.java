package ch.ech.ech0173;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.422262300")
public class EventAddressVerificationResponse {
	public static final EventAddressVerificationResponse $ = Keys.of(EventAddressVerificationResponse.class);

	@NotEmpty
	public AddressVerificationResult adressVerificationResult;
	public TypeOfResidence typeOfResidenceType;
	public byte[] extension;
}