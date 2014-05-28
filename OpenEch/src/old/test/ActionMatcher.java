package org.minimalj.swing;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;

import org.fest.swing.core.GenericTypeMatcher;

import org.minimalj.util.StringUtils;

public class ActionMatcher extends GenericTypeMatcher<JMenuItem> {
	private final String actionName;

	public ActionMatcher(Class<? extends AbstractAction> actionClass) {
		this(actionClass.getSimpleName());
	}
	
	public ActionMatcher(String actionName) {
		super(JMenuItem.class);
		this.actionName = actionName;
	}

	@Override
	protected boolean isMatching(JMenuItem button) {
		Action action = button.getAction();
		return action != null && StringUtils.equals((String) action.getValue("actionName"), actionName);
	}

}
