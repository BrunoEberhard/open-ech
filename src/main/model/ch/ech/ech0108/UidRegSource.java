package ch.ech.ech0108;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class UidRegSource {
	public static final UidRegSource $ = Keys.of(UidRegSource.class);

	public final ch.openech.model.UidStructure uid = new ch.openech.model.UidStructure();

	public enum RelationType { Responsible, Registered, Subscribed;	}
	@NotEmpty
	public RelationType relationType;
	@NotEmpty
	public LocalDate registrationDate;

	public enum RegistrationStatus { Active, WaitingForSuccessor;	}
	@NotEmpty
	public RegistrationStatus registrationStatus;
}