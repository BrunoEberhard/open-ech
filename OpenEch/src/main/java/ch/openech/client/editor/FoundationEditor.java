package ch.openech.client.editor;

import static ch.openech.dm.organisation.Organisation.*;

import java.util.Collections;
import java.util.List;

import ch.openech.business.OrganisationTransaction;
import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.org.OrganisationPanel;
import ch.openech.client.page.OrganisationViewPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.backend.Backend;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.model.EmptyValidator;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.util.LoggingRuntimeException;
import ch.openech.mj.util.SerializationContainer;
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
