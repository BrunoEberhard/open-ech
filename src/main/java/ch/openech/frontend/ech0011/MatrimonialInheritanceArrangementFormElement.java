package ch.openech.frontend.ech0011;

import org.h2.util.StringUtils;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.resources.Resources;

import ch.ech.ech0021.MatrimonialInheritanceArrangementData;
import ch.openech.frontend.YesNoFormElement;
import ch.openech.xml.YesNo;

public class MatrimonialInheritanceArrangementFormElement
		extends FormLookupFormElement<MatrimonialInheritanceArrangementData> implements LookupParser {

	public MatrimonialInheritanceArrangementFormElement(MatrimonialInheritanceArrangementData key, boolean editable) {
		super(key, editable);
	}

	@Override
	public int getAllowedSize() {
		return 50;
	}

	@Override
	public MatrimonialInheritanceArrangementData parse(String text) {
		MatrimonialInheritanceArrangementData object = new MatrimonialInheritanceArrangementData();
		if (text != null) {
			text = text.trim();
			int index = text.indexOf('(');
			if (index > 0) {
				object.matrimonialInheritanceArrangementValidFrom = RangeUtil.parseValidFrom(text.substring(index));
				text = text.substring(0, index).trim();
			}
			for (YesNo yesNo : YesNo.values()) {
				if (StringUtils.equals(Resources.getString("matrimonialInheritanceArrangement." + yesNo.name()),
						text)) {
					object.matrimonialInheritanceArrangement = yesNo;
					break;
				}
			}
			if (object.matrimonialInheritanceArrangement == null) {
				object.matrimonialInheritanceArrangement = InvalidValues.createInvalidEnum(YesNo.class, text);
			}
		}
		return object;
	}

	@Override
	protected String render(MatrimonialInheritanceArrangementData value) {
		if (value != null && value.matrimonialInheritanceArrangement != null) {
			StringBuilder s = new StringBuilder();
			s.append(Resources
					.getString("matrimonialInheritanceArrangement." + value.matrimonialInheritanceArrangement.name()));
			RangeUtil.appendValidFrom(s, value.matrimonialInheritanceArrangementValidFrom);
			return s.toString();
		} else {
			return null;
		}

	}

	@Override
	public Form<MatrimonialInheritanceArrangementData> createForm() {
		Form<MatrimonialInheritanceArrangementData> form = new Form<>(2);
		YesNoFormElement yesNoFormElement = new YesNoFormElement(
				MatrimonialInheritanceArrangementData.$.matrimonialInheritanceArrangement, true);
		form.line(yesNoFormElement);
		form.line(MatrimonialInheritanceArrangementData.$.matrimonialInheritanceArrangementValidFrom);
		return form;
	}

}
