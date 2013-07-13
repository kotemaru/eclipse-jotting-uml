package org.kotemaru.eclipse.browsereditor.generic;

import org.eclipse.swt.widgets.Composite;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserCtrl;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserEditor;


public class BrowserCtrl extends AbstractBrowserCtrl {
	public BrowserCtrl(AbstractBrowserEditor editor, Composite parent) {
		super(editor, parent);
	}

	@Override
	public String getAppPrefix() {
		return GenericConfig.get("Editor.appPrefix");
	}

	@Override
	public String getEditorUrl() {
		return GenericConfig.get("Editor.url");
	}
}
