package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.956")
public class CadastralSurveyorRemark {
	public static final CadastralSurveyorRemark $ = Keys.of(CadastralSurveyorRemark.class);

	@NotEmpty
	public RemarkType remarkType;
	@Size(255) // unknown
	public String remarkOtherType;
	@NotEmpty
	@Size(255) // unknown
	public String remarkText;
	@NotEmpty
	@Size(255) // unknown
	public String objectID;
}