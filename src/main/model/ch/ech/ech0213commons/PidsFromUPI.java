package ch.ech.ech0213commons;

import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class PidsFromUPI {
	public static final PidsFromUPI $ = Keys.of(PidsFromUPI.class);

	@Size(13)
	public Long vn;
	@Size(36)
	public String SPID;
}