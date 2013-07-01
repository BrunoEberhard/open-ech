package ch.openech.client.e21;

import static ch.openech.dm.person.Relation.*;
import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchForm;
import ch.openech.dm.code.BasedOnLaw;
import ch.openech.dm.person.Relation;
import ch.openech.mj.edit.fields.EnumEditField;
import ch.openech.xml.write.EchSchema;

public class RelationPanel extends EchForm<Relation> {
	
// Bei Birth sollte ein eigenes Panel genutzt werden
	
	public RelationPanel(EchSchema echSchema) {
		super(echSchema);
		
		line(RELATION.partner);
		line(new AddressField(RELATION.address, true));

		line(RELATION.typeOfRelationship);
		line(new BasedOnLawField());

		line(RELATION.care);
	}

	private class BasedOnLawField extends EnumEditField<BasedOnLaw> {
		
		public BasedOnLawField() {
			super(RELATION.basedOnLaw, echSchema == null || echSchema.reducedBasedOnLawCode() ?  BasedOnLaw.VERSION_3 :  null);
		}
	}
	
}
