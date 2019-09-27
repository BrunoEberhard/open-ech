package ch.ech.ech0108;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.120")
public class UidRegSource {
	public static final UidRegSource $ = Keys.of(UidRegSource.class);

	public final ch.openech.xml.UidStructure uid = new ch.openech.xml.UidStructure();

	public enum RelationType { Responsible, Registered, Subscribed;	}
	@NotEmpty
	public RelationType relationType;
	@NotEmpty
	public LocalDate registrationDate;

	public enum RegistrationStatus { Active, WaitingForSuccessor;	}
	@NotEmpty
	public RegistrationStatus registrationStatus;
}