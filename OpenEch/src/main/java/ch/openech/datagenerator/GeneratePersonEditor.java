package ch.openech.datagenerator;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.Editor;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.datagenerator.GeneratePersonEditor.GeneratePersonData;
import ch.openech.transaction.GenerateDemoDataTransaction;
import ch.openech.xml.write.EchSchema;


public class GeneratePersonEditor extends Editor<GeneratePersonData> {

//	private final String ewkVersion;
//	private final String orgVersion;
	
	public GeneratePersonEditor(EchSchema ewkNamespaceContext, EchSchema orgNamespaceContext) {
//		this.ewkVersion = ewkNamespaceContext.getVersion();
//		this.orgVersion = orgNamespaceContext.getVersion();
	}

	@Override
	protected Form<GeneratePersonData> createForm() {
		Form<GeneratePersonData> form = new Form<GeneratePersonData>();
		form.line(GeneratePersonData.$.numberOfPersons);
		form.line(GeneratePersonData.$.numberOfOrganisations);
		return form;
	}

	@Override
	protected Object save(GeneratePersonData data) throws Exception {
		Backend.getInstance().execute(new GenerateDemoDataTransaction(data.numberOfPersons != null ? data.numberOfPersons : 0, data.numberOfOrganisations != null ? data.numberOfOrganisations : 0));
		return data;
	}

	public static class GeneratePersonData implements Validation {
		public static final GeneratePersonData $ = Keys.of(GeneratePersonData.class);

		@Size(5)
		public Integer numberOfPersons;

		@Size(5)
		public Integer numberOfOrganisations;

		@Override
		public void validate(List<ValidationMessage> resultList) {
			boolean person = numberOfPersons != null && numberOfPersons > 0;
			boolean organisation = numberOfOrganisations  != null && numberOfOrganisations > 0;
			
			if (!person && !organisation) {
				resultList.add(new ValidationMessage(GeneratePersonData.$.numberOfPersons, "Anzahl Personen oder Unternehmen wählen"));
			}
			
			if (organisation && numberOfOrganisations > 110) {
				resultList.add(new ValidationMessage(GeneratePersonData.$.numberOfOrganisations, "So viele Testdatensätze existieren nicht"));
			}
		}
	}

}
