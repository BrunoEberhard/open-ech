package ch.ech.ech0108;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.198")
public class VatRegisterInformation {
	public static final VatRegisterInformation $ = Keys.of(VatRegisterInformation.class);

	@NotEmpty
	public VatStatus vatStatus;
	public VatEntryStatus vatEntryStatus;
	public LocalDate vatEntryDate;
	public LocalDate vatLiquidationDate;
	public ch.openech.model.UidStructure uidVat;
}