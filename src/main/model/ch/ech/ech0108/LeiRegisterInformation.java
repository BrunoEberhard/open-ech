package ch.ech.ech0108;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.260096300")
public class LeiRegisterInformation {
	public static final LeiRegisterInformation $ = Keys.of(LeiRegisterInformation.class);

	@NotEmpty
	public LocalDate initialRegistrationDate;
	public LocalDate expirationDate;

	public enum RegistrationStatus { ISSUED, DUPLICATE, LAPSED, MERGED, RETIRED, ANNULLED, CANCELLED;	}
	@NotEmpty
	public RegistrationStatus registrationStatus;
}