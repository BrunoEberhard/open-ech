package ch.openech.model.common;

import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

public class NamedMetaData {

	@Size(20) @NotEmpty
	public String metaDataName;
	
	// ist in eCH 0129 V4 nicht begrenzt
	@Size(1024) @NotEmpty
	public String metaDataValue;
	
}
