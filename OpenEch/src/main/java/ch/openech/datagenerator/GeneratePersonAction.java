package ch.openech.datagenerator;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.Int4;

import java.util.List;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0148;


public class GeneratePersonAction extends EditorDialogAction {

	public GeneratePersonAction(EchSchema ewkNamespaceContext, EchSchema orgNamespaceContext) {
		super(new GeneratePersonEditor(ewkNamespaceContext, orgNamespaceContext));
	}
	
	public static class GeneratePersonEditor extends Editor<GeneratePersonData> {

		private final EchSchema ewkNamespaceContext;
		private final EchSchema orgNamespaceContext;
		private int count;
		private int saveProgress;
		
		public GeneratePersonEditor(EchSchema ewkNamespaceContext, EchSchema orgNamespaceContext) {
			this.ewkNamespaceContext = ewkNamespaceContext;
			this.orgNamespaceContext = orgNamespaceContext;
		}

		@Override
		protected IForm<GeneratePersonData> createForm() {
			Form<GeneratePersonData> form = new Form<GeneratePersonData>(GeneratePersonData.class, null, true);
			form.line(GeneratePersonData.GENERATE_PERSON_DATA.numberOfPersons);
			form.line(GeneratePersonData.GENERATE_PERSON_DATA.numberOfOrganisations);
			return form;
		}

		@Override
		protected void validate(GeneratePersonData data, List<ValidationMessage> resultList) {
			boolean person = !StringUtils.isBlank(data.numberOfPersons) && Integer.parseInt(data.numberOfPersons) > 0;
			boolean organisation = !StringUtils.isBlank(data.numberOfOrganisations) && Integer.parseInt(data.numberOfOrganisations) > 0;
			
			if (!person && !organisation) {
				resultList.add(new ValidationMessage(GeneratePersonData.GENERATE_PERSON_DATA.numberOfPersons, "Anzahl Personen oder Unternehmen wählen"));
			}
			
			if (organisation && Integer.parseInt(data.numberOfOrganisations) > 110) {
				resultList.add(new ValidationMessage(GeneratePersonData.GENERATE_PERSON_DATA.numberOfOrganisations, "So viele Testdatensätze existieren nicht"));
			}
		}

		@Override
		protected boolean isSaveSynchron() {
			return false;
		}

		@Override
		protected boolean save(GeneratePersonData data) throws Exception {
			if (!generatePerson(data.numberOfPersons)) return false;
			if (!generateOrganisation(data.numberOfOrganisations)) return false;
			return true;
		}

		private boolean generatePerson(String number) {
			if (StringUtils.isBlank(number)) return true;
			
			count = Integer.parseInt(number);
			if (count > 0) {
				WriterEch0020 writerEch0020 = new WriterEch0020(ewkNamespaceContext);
				for (saveProgress = 0; saveProgress<count; saveProgress++) {
					DataGenerator.generatePerson(writerEch0020);
					progress(saveProgress, count);
				}
			} else {
				ClientToolkit.getToolkit().showError(null, number);
				return false;
			}
			return true;
		}	
		
		private boolean generateOrganisation(String number) {
			if (StringUtils.isBlank(number)) return true;
			
			count = Integer.parseInt(number);
			if (count > 0) {
				WriterEch0148 writerEch0148 = new WriterEch0148(orgNamespaceContext);
				for (saveProgress = 0; saveProgress<count; saveProgress++) {
					DataGenerator.generateOrganisation(writerEch0148);
					progress(saveProgress, count);
				}
			} else {
				ClientToolkit.getToolkit().showError(null, number);
				return false;
			}
			return true;
		}	
		
	}

	public static class GeneratePersonData {
		public static final GeneratePersonData GENERATE_PERSON_DATA = Constants.of(GeneratePersonData.class);

		@Is(Int4)
		public String numberOfPersons;

		@Is(Int4)
		public String numberOfOrganisations;

	}

}
