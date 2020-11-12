package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventChangeFireService {
	public static final EventChangeFireService $ = Keys.of(EventChangeFireService.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeFireServicePerson;
	public ch.ech.ech0021.FireServiceData fireServiceData;
}