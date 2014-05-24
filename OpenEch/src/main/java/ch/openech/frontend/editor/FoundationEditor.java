package ch.openech.frontend.editor;

import static  ch.openech.model.organisation.Organisation.*;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.IForm;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.model.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.LoggingRuntimeException;
import org.minimalj.util.SerializationContainer;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.org.OrganisationPanel;
import ch.openech.frontend.page.OrganisationViewPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import  ch.openech.model.organisation.Organisation;
import ch.openech.transaction.OrganisationTransaction;
import ch.openech.xml.write.EchSchema;


public class FoundationEditor extends XmlEditor<Organisation> implements XmlResult<Organisation> {
	
	private final OpenEchPreferences preferences;

	public FoundationEditor(EchSchema ech, OpenEchPreferences preferences) {
		super(ech);
		this.preferences = preferences;
	}

	@Override
	public IForm<Organisation> createForm() {
		return new OrganisationPanel(Organisation.EditMode.FOUNDATION, echSchema);
	}
	
	@Override
	protected void validate(Organisation object, List<ValidationMessage> resultList) {
		super.validate(object, resultList);
		EmptyValidator.validate(resultList, object, ORGANISATION.commercialRegisterStatus);
		EmptyValidator.validate(resultList, object, ORGANISATION.vatStatus);
	}

	@Override
	public Organisation newInstance() {
		return MoveInEditor.newInstance(preferences);
	}
	
	@Override
	public String save(Organisation organisation) {
		String xml = getXml(organisation).get(0);
		organisation = (Organisation) SerializationContainer.unwrap(Backend.getInstance().execute(new OrganisationTransaction(xml)));
		return PageLink.link(OrganisationViewPage.class, echSchema.getVersion(), organisation.getId());
	}
	
	@Override
	public List<String> getXml(Organisation organisation) {
		try {
			return Collections.singletonList(echSchema.getWriterEch0148().foundation(organisation));
		} catch (Exception e) {
			throw new LoggingRuntimeException(e, logger, "XML generation failed");
		}
	}
}