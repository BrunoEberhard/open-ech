package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class PlanningPermissionApplicationIdentification {
	public static final PlanningPermissionApplicationIdentification $ = Keys.of(PlanningPermissionApplicationIdentification.class);

	public Object id;
	public List<ch.openech.model.NamedId> localID;
	public List<ch.openech.model.NamedId> otherID;
	@Size(255)
	public String dossierIdentification;
}