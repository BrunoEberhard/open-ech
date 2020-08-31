package ch.ech.ech0039;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.905523600")
public class File {
	public static final File $ = Keys.of(File.class);

	@NotEmpty
	@Size(255) // unknown
	public String pathFileName;
	@NotEmpty
	@Size(255) // unknown
	public String mimeType;
	public Integer internalSortOrder;
	@Size(255) // unknown
	public String version;
	@Size(255) // unknown
	public String hashCode;
	@Size(255) // unknown
	public String hashCodeAlgorithm;
}