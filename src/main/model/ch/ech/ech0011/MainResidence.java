package ch.ech.ech0011;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class MainResidence {
	public static final MainResidence $ = Keys.of(MainResidence.class);

	public Object id;
	public final ResidenceData mainResidence = new ResidenceData();
	public List<ch.ech.ech0007.SwissMunicipality> secondaryResidence;
}