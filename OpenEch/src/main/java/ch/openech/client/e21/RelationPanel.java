package ch.openech.client.e21;

import static ch.openech.dm.person.Relation.RELATION;

import java.util.List;

import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.person.Relation;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.CodeEditField;
import ch.openech.mj.edit.fields.EditField;
import ch.openech.mj.edit.form.DependingOnFieldAbove;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;

public class RelationPanel extends EchFormPanel<Relation> {
	
	public RelationPanel(EchNamespaceContext echNamespaceContext, boolean withNameOfParents) {
		super(echNamespaceContext);
		
		area(RELATION.partner);
		area(new AddressField(RELATION.address, true));
        if (withNameOfParents) {
			line(RELATION.officialNameAtBirth);
			line(RELATION.firstNameAtBirth);
        }

		line(RELATION.typeOfRelationship);
		line(new BasedOnLawField());

		// TODO: Was war hier gemeint?
		/*
		FormField<String> care = new DependenceDecorator<String>(new CodeField(RELATION.typeOfRelationship, EchCodes.careTypeOfRelationship), StringConstants.TYPE_OF_RELATIONSHIP) {
			@Override
			public void setDependedField(FormField<String> field) {
				String typeOfRelationship = field.getObject();
				// TODO das ist isParent auf Relation
				boolean isParent = "3".equals(typeOfRelationship) || "4".equals(typeOfRelationship) || "5".equals(typeOfRelationship) || "6".equals(typeOfRelationship);
				setEnabled(isParent);
			}
		};
		line(care);
		*/
		
		line(RELATION.care);
	}

	private class BasedOnLawField extends CodeEditField implements DependingOnFieldAbove<String> {
		
		public BasedOnLawField() {
			super(RELATION.basedOnLaw, getNamespaceContext() == null || getNamespaceContext().reducedBasedOnLawCode() ?  EchCodes.basedOnLaw3 :  EchCodes.basedOnLaw);
		}

		@Override
		public String getNameOfDependedField() {
			return Constants.getConstant(RELATION.typeOfRelationship);
		}

		@Override
		public void setDependedField(EditField<String> dependedField) {
			String typeOfRelationship = dependedField.getObject();
			// TODO das ist isCare auf Relation
			boolean isVormund = "7".equals(typeOfRelationship) || "8".equals(typeOfRelationship) || "9".equals(typeOfRelationship);
			setEnabled(isVormund);

		}
	}
	
	@Override
	public Relation getObject() {
		Relation relation = super.getObject();

		if (!relation.isCareRelation()) relation.basedOnLaw = null;
		if (!relation.isParent()) relation.care = null;
			
		return relation;
	}

	@Override
	public void validate(List<ValidationMessage> resultList) {
		super.validate(resultList);
		
		Relation relation = getObject();
		if (relation.partner == null) {
			if (!relation.isParent()) {
				resultList.add(new ValidationMessage(RELATION.partner, "Person muss gesetzt sein"));
			} else if (relation.address != null) {
				resultList.add(new ValidationMessage(RELATION.partner, "Adresse darf nur gesetzt sein, wenn Person gesetzt ist"));
			}
		}
		if (!relation.isParent()) {
			if (!StringUtils.isBlank(relation.firstNameAtBirth) || !StringUtils.isBlank(relation.officialNameAtBirth)) {
				resultList.add(new ValidationMessage(RELATION.partner, "\"Name:\" darf nur bei Mutter oder Vater gesetzt sein"));
			}
		}
	}

}
