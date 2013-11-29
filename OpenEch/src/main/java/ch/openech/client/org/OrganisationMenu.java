package ch.openech.client.org;

import java.util.ArrayList;
import java.util.List;

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
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.page.ActionGroup;
import ch.openech.mj.page.EditorPageAction;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchSchema;

public class OrganisationMenu {

	private final EchSchema ech;
	private final Organisation organisation;
	
	private final PageLink showHistory;

	private final EditorPageAction moveOut, move;
	private final EditorPageAction contact, changeOrganisationName, changeLegalForm, changeReporting;
	private final EditorPageAction inLiquidation, liquidation;
	
	private final List<EditorPageAction> correctEditors = new ArrayList<EditorPageAction>();

	public OrganisationMenu(EchSchema ech, Organisation organisation) {
		this.ech = ech;
		this.organisation = organisation;
		
		move = editorLink(new MoveEvent(ech, organisation));
		moveOut = editorLink(new MoveOutEvent(ech, organisation));

		contact = editorLink(new ContactEvent(ech, organisation));
		changeOrganisationName = editorLink(new ChangeOrganisationNameEvent(ech, organisation));
		changeLegalForm = editorLink(new ChangeLegalFormEvent(ech, organisation));
		changeReporting = editorLink(new ChangeReportingEvent(ech, organisation));

		inLiquidation = editorLink(new InLiquidationEvent(ech, organisation));
		liquidation = editorLink(new LiquidationEvent(ech, organisation));
		
		showHistory = new PageLink(OrganisationHistoryPage.class, ech.getVersion(), organisation.getId());
	}

	private EditorPageAction editorLink(OrganisationEventEditor<?> editor) {
		return new EditorPageAction(editor);
	}

	public ch.openech.mj.page.ActionGroup getActions() {
		if (organisation == null) return null;
		
		ActionGroup menu = new ActionGroup("Organisation");
//		organisationActionGroup.putValue(Action.MNEMONIC_KEY, 'O');

		menu.add(moveOut);
		menu.add(move);
		menu.addSeparator();
		
		menu.add(inLiquidation);
		menu.add(liquidation);
		menu.addSeparator();
		
		menu.add(contact);
		menu.add(changeOrganisationName);
		menu.add(changeLegalForm);
		menu.add(changeReporting);

		menu.addSeparator();

		fillCorrectionActionList();
		ActionGroup correct = menu.addGroup("ActionGroup.correction");
		for (IAction action : correctEditors) {
			correct.add(action);
		}
		
		menu.addSeparator();
		menu.add(showHistory); 
		
		return menu;
	}

	private void fillCorrectionActionList() {
		addCorrectAction(new CorrectOrganisationNameEvent(ech, organisation));
		addCorrectAction(new CorrectLegalFormEvent(ech, organisation));
		addCorrectAction(new CorrectUidBrancheEvent(ech, organisation));
		addCorrectAction(new CorrectFoundationEvent(ech, organisation));
		addCorrectAction(new CorrectLiquidationEvent(ech, organisation));
		addCorrectAction(new CorrectContactEvent(ech, organisation));
		addCorrectAction(new CorrectLanguageOfCorrespondanceEvent(ech, organisation));
		addCorrectAction(new CorrectReportingEvent(ech, organisation));
	}
	
	private void addCorrectAction(OrganisationEventEditor<?> editor) {
		correctEditors.add(editorLink(editor));
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
