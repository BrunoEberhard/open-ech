package ch.openech.mj.swing;

import java.awt.Component;

import org.fest.swing.core.GenericTypeMatcher;

public class VisibleMatcher extends GenericTypeMatcher<Component> {
	
	public VisibleMatcher() {
		super(Component.class);
	}

	@Override
	protected boolean isMatching(Component component) {
		return component.isVisible();
	}

}
