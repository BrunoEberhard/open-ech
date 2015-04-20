package ch.openech.frontend.e21;

import org.minimalj.frontend.edit.fields.EnumEditField;

import ch.openech.frontend.e10.AddressField;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.code.BasedOnLaw;
import ch.openech.model.person.Relation;
import ch.openech.xml.write.EchSchema;

public class RelationPanel extends EchForm<Relation> {
	
// Bei Birth sollte ein eigenes Panel genutzt werden
	
	public RelationPanel(EchSchema echSchema) {
		super(echSchema);
		
		line(Relation.$.partner);
		line(new AddressField(Relation.$.address, true));

		line(Relation.$.typeOfRelationship);
		line(new BasedOnLawField());

		line(Relation.$.care);
	}

	private class BasedOnLawField extends EnumEditField<BasedOnLaw> {
		
		public BasedOnLawField() {
			super(Relation.$.basedOnLaw, echSchema == null || echSchema.reducedBasedOnLawCode() ?  BasedOnLaw.VERSION_3 :  null);
		}
	}
	
}