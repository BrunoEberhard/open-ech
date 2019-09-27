package ch.ech.ech0201;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.854")
public class NegativeReport {
	public static final NegativeReport $ = Keys.of(NegativeReport.class);

	public Object id;
	public List<Info> generalError;
	public static class PersonError {
		public static final PersonError $ = Keys.of(PersonError.class);

		@NotEmpty
		public ch.ech.ech0044.PersonIdentification personIdentification;
		public List<Info> errorInfo;
	}
	public List<PersonError> personError;
}