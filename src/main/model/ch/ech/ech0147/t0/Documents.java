package ch.ech.ech0147.t0;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.144")
public class Documents {
	public static final Documents $ = Keys.of(Documents.class);

	public Object id;
	@NotEmpty
	public List<Document> document;
}