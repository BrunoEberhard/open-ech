package ch.ech.ech0129;

import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.368084")
public class Area {
	public static final Area $ = Keys.of(Area.class);

	@NotEmpty
	public AreaType areaType;
	@NotEmpty
	public AreaDescriptionCode areaDescriptionCode;
	@NotEmpty
	@Size(100)
	public String areaDescription;
	@NotEmpty
	@Size(12)
	public BigDecimal areaValue;
}