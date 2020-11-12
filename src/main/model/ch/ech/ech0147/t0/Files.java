package ch.ech.ech0147.t0;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Files {
	public static final Files $ = Keys.of(Files.class);

	public Object id;
	@NotEmpty
	public List<File> file;
}