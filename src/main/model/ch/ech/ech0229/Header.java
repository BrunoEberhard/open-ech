package ch.ech.ech0229;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.557")
public class Header {
	public static final Header $ = Keys.of(Header.class);

	public Object id;
	public List<Attachment> attachment;
	public CantonExtension cantonExtension;
	@Size(100)
	public String transactionNumber;
	public LocalDateTime transactionDate;
	@NotEmpty
	@Size(4)
	public Integer taxPeriod;
	public LocalDate periodFrom;
	public LocalDate periodTo;
	public ch.ech.ech0071.CantonAbbreviation canton;
	@NotEmpty
	public Source source;
	@Size(400)
	public String sourceDescription;
}