package ch.ech.ech0021;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
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