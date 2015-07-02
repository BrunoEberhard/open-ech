package ch.openech.frontend.page;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.transaction.persistence.ReadTransaction;
import org.minimalj.util.BusinessRule;
import org.minimalj.util.resources.Resources;

import ch.openech.frontend.org.OrganisationPanel;
import ch.openech.frontend.org.event.ChangeLegalFormEvent;
import ch.openech.frontend.org.event.ChangeOrganisationNameEvent;
import ch.openech.frontend.org.event.ChangeReportingEvent;
import ch.openech.frontend.org.event.ContactEvent;
import ch.openech.frontend.org.event.InLiquidationEvent;
import ch.openech.frontend.org.event.LiquidationEvent;
import ch.openech.frontend.org.event.MoveEvent;
import ch.openech.frontend.org.event.MoveOutEvent;
import ch.openech.frontend.org.event.OrganisationEventEditor;
import ch.openech.frontend.org.event.correct.CorrectContactEvent;
import ch.openech.frontend.org.event.correct.CorrectFoundationEvent;
import ch.openech.frontend.org.event.correct.CorrectLanguageOfCorrespondanceEvent;
import ch.openech.frontend.org.event.correct.CorrectLegalFormEvent;
import ch.openech.frontend.org.event.correct.CorrectLiquidationEvent;
import ch.openech.frontend.org.event.correct.CorrectOrganisationNameEvent;
import ch.openech.frontend.org.event.correct.CorrectReportingEvent;
import ch.openech.frontend.org.event.correct.CorrectUidBrancheEvent;
import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;

public class OrganisationPage extends ObjectPage<Organisation> {

	private final EchSchema echSchema;
	private int version;
	private transient OrganisationActionGroup actionGroup;

	public OrganisationPage(EchSchema echSchema, Organisation organisation) {
		super(organisation);
		this.echSchema = echSchema;
		this.version = organisation.version;
	}

	public OrganisationPage(EchSchema echSchema, Object organisationId) {
		super(Organisation.class, organisationId);
		this.echSchema = echSchema;
		this.version = 0;
	}
	
	public EchSchema getEchSchema() {
		return echSchema;
	}

	@Override
	public ActionGroup getMenu() {
		if (version == 0) {
			if (actionGroup == null) {
				actionGroup = new OrganisationActionGroup();
			}
			return actionGroup;
		} else {
			return null;
		}
	}
	
	@Override
	public Organisation load() {
		Organisation organisation;
		if (version == 0) {
			organisation = Backend.getInstance().read(Organisation.class, getObjectId());
		} else {
			organisation = Backend.getInstance().execute(new ReadTransaction<Organisation>(Organisation.class, getObjectId(), version));
		}
		return organisation;
	}

	@Override
	public Form<Organisation> createForm() {
		return new OrganisationPanel(Organisation.EditMode.DISPLAY, echSchema);
	}
	
	private static final int MAX_NAME_LENGTH = 20;
	
	@Override
	public String getTitle() {
		Organisation organisation = getObject();
		String title;
		if (organisation.organisationName != null) {
			if (organisation.organisationName.length() <= MAX_NAME_LENGTH) {
				title = organisation.organisationName;
			} else {
				title = organisation.organisationName.substring(0, MAX_NAME_LENGTH-1) + "..";
			}
		} else {
			title = Resources.getString("Organisation");
		}
		return title;
	}

	public class OrganisationActionGroup extends ActionGroup {

		private final OrganisationHistoryPage historyPage;

		private final Action moveOut, move;
		private final Action contact, changeOrganisationName, changeLegalForm, changeReporting;
		private final Action inLiquidation, liquidation;
		
		private final List<Action> correctEditors = new ArrayList<>();

		public OrganisationActionGroup() {
			super(Resources.getString("Organisation"));
			
			move = new MoveEvent(OrganisationPage.this);
			moveOut = new MoveOutEvent(OrganisationPage.this);

			contact = new ContactEvent(OrganisationPage.this);
			changeOrganisationName = new ChangeOrganisationNameEvent(OrganisationPage.this);
			changeLegalForm = new ChangeLegalFormEvent(OrganisationPage.this);
			changeReporting = new ChangeReportingEvent(OrganisationPage.this);

			inLiquidation = new InLiquidationEvent(OrganisationPage.this);
			liquidation = new LiquidationEvent(OrganisationPage.this);
			
			historyPage = new OrganisationHistoryPage(OrganisationPage.this);
			
			add(moveOut);
			add(move);
			addSeparator();
			
			add(inLiquidation);
			add(liquidation);
			addSeparator();
			
			add(contact);
			add(changeOrganisationName);
			add(changeLegalForm);
			add(changeReporting);

			addSeparator();

			fillCorrectionActionList();
			ActionGroup correct = addGroup(Resources.getString("ActionGroup.correction"));
			for (Action action : correctEditors) {
				correct.add(action);
			}
			
			addSeparator();
			add(historyPage); 
			
			update(getObject());
		}

		private void fillCorrectionActionList() {
			addCorrectAction(new CorrectOrganisationNameEvent(OrganisationPage.this));
			addCorrectAction(new CorrectLegalFormEvent(OrganisationPage.this));
			addCorrectAction(new CorrectUidBrancheEvent(OrganisationPage.this));
			addCorrectAction(new CorrectFoundationEvent(OrganisationPage.this));
			addCorrectAction(new CorrectLiquidationEvent(OrganisationPage.this));
			addCorrectAction(new CorrectContactEvent(OrganisationPage.this));
			addCorrectAction(new CorrectLanguageOfCorrespondanceEvent(OrganisationPage.this));
			addCorrectAction(new CorrectReportingEvent(OrganisationPage.this));
		}
		
		private void addCorrectAction(OrganisationEventEditor<?> editor) {
			correctEditors.add(editor);
		}

		@BusinessRule("Welche Aktion in welchem Zustand von Unternehmen ausgeführt werden darf")
		public void update(Organisation organisation) {
			boolean isHistorical = organisation.version != 0;
			boolean isOrganisation = organisation != null && !isHistorical;

//			boolean isAlive = isOrganisation && organisation.isAlive();
	//
//			death.setEnabled(isAlive);
//			divorce.setEnabled(isAlive && isMarried);
//			separation.setEnabled(isAlive && isMarried && !isSeparated);
	//
//			for (Action action : correctEditors) {
//				action.setEnabled(isOrganisation);
//			};
		}
	}

}
