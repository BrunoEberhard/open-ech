package ch.ech.ech0129;

import java.util.List;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class ConstructionProjectIdentification {
	public static final ConstructionProjectIdentification $ = Keys.of(ConstructionProjectIdentification.class);

	public Object id;
	public List<ch.openech.model.NamedId> localID;
	@Size(9)
	public Integer EPROID;
	@Size(15)
	public String officialConstructionProjectFileNo;
	@Size(2)
	public Integer extensionOfOfficialConstructionProjectFileNo;
}