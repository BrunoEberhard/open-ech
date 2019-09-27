package ch.ech.ech0021;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.391")
public class JobData {
	public static final JobData $ = Keys.of(JobData.class);

	public Object id;

	public enum KindOfEmployment { _0, _1, _2, _3, _4;	}
	@NotEmpty
	public KindOfEmployment kindOfEmployment;
	@Size(100)
	public String jobTitle;
	public List<OccupationData> occupationData;
}