package ch.openech.client.org;

import java.awt.event.ActionEvent;
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
import ch.openech.client.page.OrganisationHistoryPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.SeparatorAction;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchSchema;

public class OrganisationMenu {

	private final EchSchema echSchema;
	private final PageContext context;
	private Organisation organisation;
	
	private HistoryAction showHistory;

	private Action moveOut, move;
	private Action contact, changeOrganisationName, changeLegalForm, changeReporting;
	private Action inLiquidation, liquidation;
	
	private final List<Action> correctEditors = new ArrayList<Action>();

	public OrganisationMenu(PageContext pageContext, EchSchema echSchema) {
		this.echSchema = echSchema;
		this.context = pageContext;
		createMenuItems();
	}
	
	public void fillActionGroup(ActionGroup organisationActionGroup) {
		if (organisation == null) return;
		
		organisationActionGroup.putValue(Action.NAME, "Organisation");
		organisationActionGroup.putValue(Action.MNEMONIC_KEY, 'O');

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
		ActionGroup correct = organisationActionGroup.getOrCreateActionGroup("correction");
		for (Action action : correctEditors) {
			correct.add(action);
		}
		
		organisationActionGroup.add(new SeparatorAction());
		organisationActionGroup.add(showHistory); 
		
		//
		
		List<Action> actions = organisationActionGroup.getAllActions();
		showHistory.setOrganisation(organisation);
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
	
	private class HistoryAction extends ResourceAction {
		private Organisation organisation;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			context.show(Page.link(OrganisationHistoryPage.class, echSchema.getVersion(), organisation.getId()));
		}
		
		public void setOrganisation(Organisation organisation) {
			this.organisation = organisation;
			setEnabled(organisation != null);
		}
	}	

	private void createMenuItems() {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();

		move = new OrganisationEditMenuAction(new MoveEvent(echSchema, preferences));
		moveOut = new OrganisationEditMenuAction(new MoveOutEvent(echSchema, preferences));

		contact = new OrganisationEditMenuAction(new ContactEvent(echSchema, preferences));
		changeOrganisationName = new OrganisationEditMenuAction(new ChangeOrganisationNameEvent(echSchema, preferences));
		changeLegalForm = new OrganisationEditMenuAction(new ChangeLegalFormEvent(echSchema, preferences));
		changeReporting = new OrganisationEditMenuAction(new ChangeReportingEvent(echSchema, preferences));

		inLiquidation = new OrganisationEditMenuAction(new InLiquidationEvent(echSchema, preferences));
		liquidation = new OrganisationEditMenuAction(new LiquidationEvent(echSchema, preferences));
		
		showHistory = new HistoryAction();
	}

	private void fillCorrectionActionList() {
		OpenEchPreferences preferences = (OpenEchPreferences) context.getApplicationContext().getPreferences();

		addCorrectAction(new CorrectOrganisationNameEvent(echSchema, preferences));
		addCorrectAction(new CorrectLegalFormEvent(echSchema, preferences));
		addCorrectAction(new CorrectUidBrancheEvent(echSchema, preferences));
		addCorrectAction(new CorrectFoundationEvent(echSchema, preferences));
		addCorrectAction(new CorrectLiquidationEvent(echSchema, preferences));
		addCorrectAction(new CorrectContactEvent(echSchema, preferences));
		addCorrectAction(new CorrectLanguageOfCorrespondanceEvent(echSchema, preferences));
		addCorrectAction(new CorrectReportingEvent(echSchema, preferences));
	}
	
	private void addCorrectAction(OrganisationEventEditor<?> editor) {
		correctEditors.add(new OrganisationEditMenuAction(editor));
	}

	public void setOrganisation(Organisation organisation, boolean enabled) {
		this.organisation = organisation;
		update(organisation, enabled);
	}
	
	@BusinessRule("Welche Aktion in welchem Zustand von Unternehmen ausgeführt werden darf")
	public void update(Organisation organisation, boolean enabled) {
//		boolean isOrganisation = organisation != null && enabled;
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
