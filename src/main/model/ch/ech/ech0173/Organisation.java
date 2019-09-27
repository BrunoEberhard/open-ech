package ch.ech.ech0173;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.441")
public class Organisation {
	public static final Organisation $ = Keys.of(Organisation.class);

	@NotEmpty
	public ch.ech.ech0097.OrganisationIdentification organisationIdentification;
	@Size(500)
	public String uidBrancheText;
	@Size(6)
	public String nogaCode;
	public static class Foundation {
		public static final Foundation $ = Keys.of(Foundation.class);

		public ch.openech.model.DatePartiallyKnown foundationDate;

		public enum FoundationReason { _1, _2, _3;		}
		public FoundationReason foundationReason;
	}
	public Foundation foundation;
	public static class Liquidation {
		public static final Liquidation $ = Keys.of(Liquidation.class);

		public ch.openech.model.DatePartiallyKnown liquidationDate;
		public ch.openech.model.DatePartiallyKnown liquidationStartDate;

		public enum LiquidationReason { _1, _2, _3;		}
		public LiquidationReason liquidationReason;
	}
	public Liquidation liquidation;
	public Contact contact;
	@Size(2)
	public String languageOfCorrespondance;
}