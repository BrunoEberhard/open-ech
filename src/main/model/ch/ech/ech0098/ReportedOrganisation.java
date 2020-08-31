package ch.ech.ech0098;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.184118700")
public class ReportedOrganisation {
	public static final ReportedOrganisation $ = Keys.of(ReportedOrganisation.class);

	@NotEmpty
	public Organisation organisation;
	public MainResidence hasMainResidence;
	public SecondaryResidence hasSecondaryResidence;
	public OtherResidence hasOtherResidence;
}