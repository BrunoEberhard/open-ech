package ch.ech.ech0147.t0;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// handmade
public class File {
	public static final File $ = Keys.of(File.class);

	public Object id;
	@NotEmpty
	@Size(255) // unknown
	public String pathFileName;
	@NotEmpty
	@Size(255) // unknown
	public String mimeType;
	public Integer internalSortOrder;
	@Size(255) // unknown
	public String Version;
	@Size(255) // unknown
	public String hashCode;
	@Size(255) // unknown
	public String hashCodeAlgorithm;
	public List<ApplicationCustom> applicationCustom;
	
	//
	
	public String getVersion() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "version");
		return Version;
	}
	
	public void setVersion(String version) {
		this.Version = version;
	}
}