package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.139")
public class EventCorrectPersonAdditionalData {
	public static final EventCorrectPersonAdditionalData $ = Keys.of(EventCorrectPersonAdditionalData.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctPersonAdditionalDataPerson;
	public ch.ech.ech0021.PersonAdditionalData personAdditionalData;
}