package ch.ech.ech0108;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.287083300")
public class CommercialRegisterInformation {
	public static final CommercialRegisterInformation $ = Keys.of(CommercialRegisterInformation.class);

	@NotEmpty
	public CommercialRegisterStatus commercialRegisterStatus;
	public CommercialRegisterEntryStatus commercialRegisterEntryStatus;
	@Size(255)
	public String commercialRegisterNameTranslation;
	public LocalDate commercialRegisterEntryDate;
	public LocalDate commercialRegisterLiquidationDate;
	public CommercialRegisterEnterpriseType commercialRegisterEnterpriseType;
}