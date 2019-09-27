package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.941")
public class CadastralMap {
	public static final CadastralMap $ = Keys.of(CadastralMap.class);

	@NotEmpty
	@Size(12)
	public String mapNumber;
	@NotEmpty
	@Size(12)
	public String identDN;
}