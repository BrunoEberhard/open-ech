package ch.openech.datagenerator;

import java.util.List;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Int;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.swing.FrameManager;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;


public class GeneratePersonAction extends EditorDialogAction {

	public GeneratePersonAction(EchNamespaceContext echNamespaceContext) {
		super(new GeneratePersonEditor(echNamespaceContext));
	}
	
	public static class GeneratePersonEditor extends Editor<GeneratePersonData> {

		private final EchNamespaceContext echNamespaceContext;
		
		public GeneratePersonEditor(EchNamespaceContext echNamespaceContext) {
			this.echNamespaceContext = echNamespaceContext;
		}

		@Override
		protected FormVisual<GeneratePersonData> createForm() {
			AbstractFormVisual<GeneratePersonData> form = new AbstractFormVisual<GeneratePersonData>(GeneratePersonData.class, null, true);
			form.line(GeneratePersonData.GENERATE_PERSON_DATA.numberOfPersons);
			return form;
		}

		@Override
		protected GeneratePersonData load() {
			return new GeneratePersonData();
		}

		@Override
		protected void validate(GeneratePersonData object, List<ValidationMessage> resultList) {
			// nothing to validate
		}

		@Override
		protected boolean save(GeneratePersonData data) {
			String input = data.numberOfPersons;
			if (StringUtils.isBlank(input)) return true;
			
			try {
				int count = Integer.parseInt(input);
				if (count > 0) {
					DataGenerator.generateData(echNamespaceContext, count, FrameManager.getInstance());
				} else {
					ClientToolkit.getToolkit().showError(null, input);
					return false;
				}
			} catch (NumberFormatException x) {
				ClientToolkit.getToolkit().showError(null, input);
				return false;
			}
			return true;
		}
		
	}

	public static class GeneratePersonData {
		public static final GeneratePersonData GENERATE_PERSON_DATA = Constants.of(GeneratePersonData.class);

		@Int(4)
		public String numberOfPersons;
	}

}
