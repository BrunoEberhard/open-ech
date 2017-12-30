package ch.openech.model.common;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

public class NamedMetaData implements Rendering {

	public static final NamedMetaData $ = Keys.of(NamedMetaData.class);
	
	@Size(20) @NotEmpty
	public String metaDataName;
	
	// ist in eCH 0129 V4 nicht begrenzt
	@Size(1024) @NotEmpty
	public String metaDataValue;
	
	@Override
	public String render(RenderType renderType) {
		if (metaDataValue != null && metaDataValue.length() > 30) {
			return metaDataName + " = " + metaDataValue.substring(0, 30) + "...";
		} else {
			return metaDataName + " = " + metaDataValue;
		}
	}
}
