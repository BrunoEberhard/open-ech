package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventChangeOrigin {
	public static final EventChangeOrigin $ = Keys.of(EventChangeOrigin.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeOriginPerson;
	public List<PlaceOfOriginInfo> originInfo;
}