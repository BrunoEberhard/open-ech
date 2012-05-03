package ch.openech.datagenerator;

import java.util.List;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Int;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;


public class GeneratePersonAction extends EditorDialogAction {

	public GeneratePersonAction(EchNamespaceContext echNamespaceContext) {
		super(new GeneratePersonEditor(echNamespaceContext));
	}
	
	public static class GeneratePersonEditor extends Editor<GeneratePersonData> {

		private final EchNamespaceContext echNamespaceContext;
		private int count;
		private int saveProgress;
		
		public GeneratePersonEditor(EchNamespaceContext echNamespaceContext) {
			this.echNamespaceContext = echNamespaceContext;
		}

		@Override
		protected IForm<GeneratePersonData> createForm() {
			Form<GeneratePersonData> form = new Form<GeneratePersonData>(GeneratePersonData.class, null, true);
			form.line(GeneratePersonData.GENERATE_PERSON_DATA.numberOfPersons);
			return form;
		}

		@Override
		protected void validate(GeneratePersonData data, List<ValidationMessage> resultList) {
			if (!StringUtils.isBlank(data.numberOfPersons)) {
				int i = Integer.parseInt(data.numberOfPersons);
				if (i < 1) {
					resultList.add(new ValidationMessage(GeneratePersonData.GENERATE_PERSON_DATA.numberOfPersons, "Eingabe grösser 0 erforderlich"));
				}
			}
		}

		@Override
		protected boolean isSaveSynchron() {
			return false;
		}

		@Override
		protected boolean save(GeneratePersonData data) {
			String input = data.numberOfPersons;
			if (StringUtils.isBlank(input)) return true;
			
			count = Integer.parseInt(input);
			if (count > 0) {
				WriterEch0020 writerEch0020 = new WriterEch0020(echNamespaceContext);
				for (saveProgress = 0; saveProgress<count; saveProgress++) {
					DataGenerator.generatePerson(writerEch0020);
					progress(saveProgress, count);
				}
			} else {
				ClientToolkit.getToolkit().showError(null, input);
				return false;
			}
			return true;
		}		
		
	}

	public static class GeneratePersonData {
		public static final GeneratePersonData GENERATE_PERSON_DATA = Constants.of(GeneratePersonData.class);

		@Int(4) @Required
		public String numberOfPersons;
	}

}
