package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.186")
public class EventDeletedInRegister {
	public static final EventDeletedInRegister $ = Keys.of(EventDeletedInRegister.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification deledetInRegisterPerson;
}