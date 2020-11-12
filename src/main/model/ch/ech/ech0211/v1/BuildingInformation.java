package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class BuildingInformation {
	public static final BuildingInformation $ = Keys.of(BuildingInformation.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0129.Building building;
	public List<ch.ech.ech0129.Dwelling> dwelling;
}