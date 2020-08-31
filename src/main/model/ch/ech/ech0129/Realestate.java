package ch.ech.ech0129;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.366083600")
public class Realestate {
	public static final Realestate $ = Keys.of(Realestate.class);

	public Object id;
	public final RealestateIdentification realestateIdentification = new RealestateIdentification();
	@Size(12)
	public String authority;
	public LocalDate date;
	@NotEmpty
	public RealestateType realestateType;
	@Size(60)
	public String cantonalSubKind;
	public RealestateStatus status;
	@Size(12)
	public String mutnumber;
	@Size(12)
	public String identDN;
	@Size(12)
	public BigDecimal squareMeasure;
	public Boolean realestateIncomplete;
	public Coordinates coordinates;
	public List<NamedMetaData> namedMetaData;
}