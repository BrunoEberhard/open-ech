package  ch.openech.model;

import org.minimalj.model.Rendering;

public interface EchSchema extends Rendering {

	public int getSchemaNumber();
	
	public int getVersion();
	
	public int getMinorVersion();
	
	@Override
	default String render(RenderType renderType) {
		return getVersion() + "." + getMinorVersion();
	}
	
}
