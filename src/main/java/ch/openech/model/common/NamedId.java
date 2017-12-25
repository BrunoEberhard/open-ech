package ch.openech.model.common;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;

import ch.openech.model.EchFormats;

/**
 * Wird für person, firma, gebäude etc verwendet. Die XML Elemente heissen
 * jeweils anders, z.B. personId statt Id.
 *
 */
@Sizes(EchFormats.class)
public class NamedId implements Rendering {

	public static final NamedId $ = Keys.of(NamedId.class);
	public static final String OPEN_ECH_ID_CATEGORY = "LOC.OPENECH";

	// ja, das I von Id wird wirklich gross geschrieben

	// @Size(EchFormats.iDCategory)
	@Size(20)
	public String IdCategory;

	@Size(36)
	public String Id;

	@Override
	public String render(RenderType renderType) {
		return IdCategory + "=" + Id;
	}

	public boolean openEch() {
		return OPEN_ECH_ID_CATEGORY.equals(IdCategory);
	}

	public void clear() {
		IdCategory = null;
		Id = null;
	}

}
