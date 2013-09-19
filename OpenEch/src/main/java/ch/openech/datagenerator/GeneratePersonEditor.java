package ch.openech.datagenerator;

import java.util.List;

import ch.openech.datagenerator.GeneratePersonEditor.GeneratePersonData;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.Validation;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Size;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0148;


public class GeneratePersonEditor extends Editor<GeneratePersonData> {

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
		Form<GeneratePersonData> form = new Form<GeneratePersonData>();
		form.line(GeneratePersonData.GENERATE_PERSON_DATA.numberOfPersons);
		form.line(GeneratePersonData.GENERATE_PERSON_DATA.numberOfOrganisations);
		return form;
	}

	@Override
	protected Object save(GeneratePersonData data) throws Exception {
		if (!generatePerson(data.numberOfPersons)) return false;
		if (!generateOrganisation(data.numberOfOrganisations)) return false;
		return data;
	}

	private boolean generatePerson(Integer number) {
		if (number == null) return true;
		
		count = number;
		if (count > 0) {
			WriterEch0020 writerEch0020 = new WriterEch0020(ewkNamespaceContext);
			for (saveProgress = 0; saveProgress<count; saveProgress++) {
				DataGenerator.generatePerson(writerEch0020);
				// progress(saveProgress, count);
			}
		} else {
			showError("" + number);
			return false;
		}
		return true;
	}	
	
	private boolean generateOrganisation(Integer number) {
		if (number == null) return true;
		
		count = number;
		if (count > 0) {
			WriterEch0148 writerEch0148 = new WriterEch0148(orgNamespaceContext);
			for (saveProgress = 0; saveProgress<count; saveProgress++) {
				DataGenerator.generateOrganisation(writerEch0148);
				// progress(saveProgress, count);
			}
		} else {
			showError("" + number);
			return false;
		}
		return true;
	}	
		

	public static class GeneratePersonData implements Validation {
		public static final GeneratePersonData GENERATE_PERSON_DATA = Keys.of(GeneratePersonData.class);

		@Size(4)
		public Integer numberOfPersons;

		@Size(4)
		public Integer numberOfOrganisations;

		@Override
		public void validate(List<ValidationMessage> resultList) {
			boolean person = numberOfPersons != null && numberOfPersons > 0;
			boolean organisation = numberOfOrganisations  != null && numberOfOrganisations > 0;
			
			if (!person && !organisation) {
				resultList.add(new ValidationMessage(GeneratePersonData.GENERATE_PERSON_DATA.numberOfPersons, "Anzahl Personen oder Unternehmen wählen"));
			}
			
			if (organisation && numberOfOrganisations > 110) {
				resultList.add(new ValidationMessage(GeneratePersonData.GENERATE_PERSON_DATA.numberOfOrganisations, "So viele Testdatensätze existieren nicht"));
			}
		}
	}

}
