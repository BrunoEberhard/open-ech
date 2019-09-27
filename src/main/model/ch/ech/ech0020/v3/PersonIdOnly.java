package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.999")
public class PersonIdOnly {
	public static final PersonIdOnly $ = Keys.of(PersonIdOnly.class);

	public Object id;
	@Size(13)
	public Long vn;
	public final ch.openech.xml.NamedId localPersonId = new ch.openech.xml.NamedId();
	public List<ch.openech.xml.NamedId> otherPersonId;
	public List<ch.openech.xml.NamedId> euPersonId;
}