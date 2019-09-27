package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.264")
public class PositiveReport {
	public static final PositiveReport $ = Keys.of(PositiveReport.class);

	public Object id;
	public List<Info> generalResponse;
	public static class PersonResponse {
		public static final PersonResponse $ = Keys.of(PersonResponse.class);

		@NotEmpty
		public ch.ech.ech0044.PersonIdentification personIdentification;
		public List<Info> response;
	}
	public List<PersonResponse> personResponse;
}