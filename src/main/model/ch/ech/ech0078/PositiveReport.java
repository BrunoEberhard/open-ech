package ch.ech.ech0078;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.129099400")
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