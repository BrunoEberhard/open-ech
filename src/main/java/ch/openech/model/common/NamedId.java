package  ch.openech.model.common;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;

import  ch.openech.model.EchFormats;

@Sizes(EchFormats.class)
public class NamedId implements Rendering {

	public static final NamedId $ = Keys.of(NamedId.class);
	public static final String OPEN_ECH_ID_CATEGORY = "LOC.OPENECH";
	
	public String personIdCategory;
	
	@Size(36)
	public String personId;
	
	@Override
	public String render(RenderType renderType) {
		return personIdCategory + "=" + personId;
	}
	
	public boolean openEch() {
		return OPEN_ECH_ID_CATEGORY.equals(personIdCategory);
	}
	
	public void clear() {
		personIdCategory = null;
		personId = null;
	}
	
}
