package ch.ech.ech0058;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class NamedMetaData {
	public static final NamedMetaData $ = Keys.of(NamedMetaData.class);

	@NotEmpty
	@Size(20)
	public String metaDataName;
	@NotEmpty
	@Size(50)
	public String metaDataValue;
}