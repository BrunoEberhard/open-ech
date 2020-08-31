package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.359083100")
public class BuildingEntranceIdentification {
	public static final BuildingEntranceIdentification $ = Keys.of(BuildingEntranceIdentification.class);

	@NotEmpty
	@Size(9)
	public Integer EGID;
	@Size(9)
	public Integer EGAID;
	@Size(2)
	public Integer EDID;
	public final ch.openech.model.NamedId localID = new ch.openech.model.NamedId();
}