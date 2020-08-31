package ch.ech.ech0011;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.784068")
public class MainResidence {
	public static final MainResidence $ = Keys.of(MainResidence.class);

	public Object id;
	public final ResidenceData mainResidence = new ResidenceData();
	public List<ch.ech.ech0007.SwissMunicipality> secondaryResidence;
}