package ch.openech.client.org;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import ch.openech.client.org.event.ChangeLegalFormEvent;
import ch.openech.client.org.event.ChangeOrganisationNameEvent;
import ch.openech.client.org.event.ChangeReportingEvent;
import ch.openech.client.org.event.ContactEvent;
import ch.openech.client.org.event.InLiquidationEvent;
import ch.openech.client.org.event.LiquidationEvent;
import ch.openech.client.org.event.MoveEvent;
import ch.openech.client.org.event.MoveOutEvent;
import ch.openech.client.org.event.OrganisationEventEditor;
import ch.openech.client.org.event.correct.CorrectContactEvent;
import ch.openech.client.org.event.correct.CorrectFoundationEvent;
import ch.openech.client.org.event.correct.CorrectLanguageOfCorrespondanceEvent;
import ch.openech.client.org.event.correct.CorrectLegalFormEvent;
import ch.openech.client.org.event.correct.CorrectLiquidationEvent;
import ch.openech.client.org.event.correct.CorrectOrganisationNameEvent;
import ch.openech.client.org.event.correct.CorrectReportingEvent;
import ch.openech.client.org.event.correct.CorrectUidBrancheEvent;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchNamespaceContext;

public class OrganisationMenu {

	private final EchNamespaceContext echNamespaceContext;
	private PageContext pageContext;
	private Organisation organisation;
	
//	private OrganisationHistoryMenuAction showHistory;

	private Action moveOut, move;
	private Action contact, changeOrganisationName, changeLegalForm, changeReporting;
	private Action inLiquidation, liquidation;
	
	private final List<Action> correctEditors = new ArrayList<Action>();

	public OrganisationMenu(EchNamespaceContext echNamespaceContext) {
		this.echNamespaceContext = echNamespaceContext;
		createMenuItems();
	}
	
	public void fillActionGroup(PageContext pageContext, ActionGroup actionGroup) {
		if (organisation == null) return;
		
		this.pageContext = pageContext;
		
		ActionGroup organisationActionGroup = actionGroup.getOrCreateActionGroup(ActionGroup.OBJECT);
		organisationActionGroup.putValue(Action.NAME, "Organisation");
		organisationActionGroup.putValue(Action.MNEMONIC_KEY, 'O');

		// TODO History of organisation
//		ActionGroup history = organisation.getOrCreateActionGroup("history");
//		organisation.addSeparator();

		organisationActionGroup.add(moveOut);
		organisationActionGroup.add(move);
		organisationActionGroup.addSeparator();
		
		organisationActionGroup.add(inLiquidation);
		organisationActionGroup.add(liquidation);
		organisationActionGroup.addSeparator();
		
		organisationActionGroup.add(contact);
		organisationActionGroup.add(changeOrganisationName);
		organisationActionGroup.add(changeLegalForm);
		organisationActionGroup.add(changeReporting);

		organisationActionGroup.addSeparator();

		fillCorrectionActionList();
		ActionGroup correct = organisationActionGroup.getOrCreateActionGroup("organisation_correction");
		for (Action action : correctEditors) {
			correct.add(action);
		}
		
		//
		
		List<Action> actions = actionGroup.getAllActions();
//		showHistory.setOrganisation(organisation);
		for (Action action : actions) {
			if (action instanceof OrganisationEditMenuAction) {
				OrganisationEditMenuAction organisationEditMenuAction = (OrganisationEditMenuAction) action;
				organisationEditMenuAction.setOrganisation(organisation);
			}
		}
	}

	private class OrganisationEditMenuAction extends EditorDialogAction {
		private final OrganisationEventEditor<?> editor;
		
		public OrganisationEditMenuAction(OrganisationEventEditor<?> editor) {
			super(editor);
			this.editor = editor;
		}
		
		public void setOrganisation(Organisation organisation) {
			editor.setOrganisation(organisation);
		}
	}
	
//	private class OrganisationHistoryMenuAction extends ResourceAction {
//		private Organisation organisation;
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			pageContext.show(Page.link(OrganisationHistoryViewPage.class, echNamespaceContext.getVersion(), organisation.getId()));
//		}
//		
//		public void setOrganisation(Organisation organisation) {
//			this.organisation = organisation;
//			setEnabled(organisation != null);
//		}
//	}

	private void createMenuItems() {
//		showHistory = new OrganisationHistoryMenuAction();
		
//		private Action moveOut, move;

		move = new OrganisationEditMenuAction(new MoveEvent(echNamespaceContext));
		moveOut = new OrganisationEditMenuAction(new MoveOutEvent(echNamespaceContext));

		contact = new OrganisationEditMenuAction(new ContactEvent(echNamespaceContext));
		changeOrganisationName = new OrganisationEditMenuAction(new ChangeOrganisationNameEvent(echNamespaceContext));
		changeLegalForm = new OrganisationEditMenuAction(new ChangeLegalFormEvent(echNamespaceContext));
		changeReporting = new OrganisationEditMenuAction(new ChangeReportingEvent(echNamespaceContext));

		inLiquidation = new OrganisationEditMenuAction(new InLiquidationEvent(echNamespaceContext));
		liquidation = new OrganisationEditMenuAction(new LiquidationEvent(echNamespaceContext));
	}

	private void fillCorrectionActionList() {
		addCorrectAction(new CorrectOrganisationNameEvent(echNamespaceContext));
		addCorrectAction(new CorrectLegalFormEvent(echNamespaceContext));
		addCorrectAction(new CorrectUidBrancheEvent(echNamespaceContext));
		addCorrectAction(new CorrectFoundationEvent(echNamespaceContext));
		addCorrectAction(new CorrectLiquidationEvent(echNamespaceContext));
		addCorrectAction(new CorrectContactEvent(echNamespaceContext));
		addCorrectAction(new CorrectLanguageOfCorrespondanceEvent(echNamespaceContext));
		addCorrectAction(new CorrectReportingEvent(echNamespaceContext));
	}
	
	private void addCorrectAction(OrganisationEventEditor<?> editor) {
		correctEditors.add(new OrganisationEditMenuAction(editor));
	}

	public void setOrganisation(Organisation organisation, boolean enabled) {
		this.organisation = organisation;
		update(organisation, enabled);
	}
	
	@BusinessRule("Welche Aktion in welchem Zustand ausgeführt werden darf")
	public void update(Organisation organisation, boolean enabled) {
		boolean isOrganisation = organisation != null && enabled;
//		boolean isAlive = isOrganisation && organisation.isAlive();
//
//		death.setEnabled(isAlive);
//		divorce.setEnabled(isAlive && isMarried);
//		separation.setEnabled(isAlive && isMarried && !isSeparated);
//
//		for (Action action : correctEditors) {
//			action.setEnabled(isOrganisation);
//		};
	}

}
