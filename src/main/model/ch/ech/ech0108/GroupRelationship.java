package ch.ech.ech0108;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.105")
public class GroupRelationship {
	public static final GroupRelationship $ = Keys.of(GroupRelationship.class);

	public Object id;
	@NotEmpty
	@Size(100)
	public String groupName;
	@NotEmpty
	public OrganisationMembershipRole organisationMembershipRole;
	public static class GroupParticipant {
		public static final GroupParticipant $ = Keys.of(GroupParticipant.class);

		public final ch.openech.model.UidStructure participant = new ch.openech.model.UidStructure();
		public OrganisationMembershipRole participantRole;
	}
	public List<GroupParticipant> groupParticipant;
}