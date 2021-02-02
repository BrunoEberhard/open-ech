package ch.openech.frontend.org;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;

import ch.openech.transaction.ImportSwissDataTransaction;

public class ImportSwissDataAction extends Action {

	public ImportSwissDataAction() {
	}

	@Override
	public void run() {
		Backend.execute(new ImportSwissDataTransaction());
	}
	
}
