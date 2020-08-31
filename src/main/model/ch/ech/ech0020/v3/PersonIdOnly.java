package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.835335900")
public class PersonIdOnly {
	public static final PersonIdOnly $ = Keys.of(PersonIdOnly.class);

	public Object id;
	@Size(13)
	public Long vn;
	public final ch.openech.model.NamedId localPersonId = new ch.openech.model.NamedId();
	public List<ch.openech.model.NamedId> otherPersonId;
	public List<ch.openech.model.NamedId> euPersonId;
}