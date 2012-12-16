package ch.openech.client.e21;

import static ch.openech.dm.person.Relation.RELATION;
import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.code.BasedOnLaw;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.mj.edit.fields.CodeEditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.xml.write.EchSchema;

public class RelationPanel extends EchFormPanel<Relation> {
	
	public RelationPanel(EchSchema echSchema, boolean withNameOfParents) {
		super(echSchema);
		
		area(RELATION.partner);
		area(new AddressField(RELATION.address, true));
        if (withNameOfParents) {
			line(RELATION.officialNameAtBirth);
			line(RELATION.firstNameAtBirth);
        }

		line(RELATION.typeOfRelationship);
		line(new BasedOnLawField());

		line(RELATION.care);
	}

	private class BasedOnLawField extends CodeEditField<BasedOnLaw> implements DependingOnFieldAbove<TypeOfRelationship> {
		
		public BasedOnLawField() {
			super(RELATION.basedOnLaw, echSchema == null || echSchema.reducedBasedOnLawCode() ?  BasedOnLaw.VERSION_3 :  null);
		}

		@Override
		public TypeOfRelationship getKeyOfDependedField() {
			return RELATION.typeOfRelationship;
		}

		@Override
		public void valueChanged(TypeOfRelationship typeOfRelationship) {
			boolean isVormund = typeOfRelationship != null && typeOfRelationship.isCare();
			setEnabled(isVormund);

		}
	}
	
}
