package org.kotemaru.eclipse.jottinguml;

import org.eclipse.swt.widgets.Composite;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserCtrl;
import org.kotemaru.eclipse.browsereditor.BrowserEditor;


public class BrowserCtrl extends AbstractBrowserCtrl {
	private static final String APP_PREFIX = "JottingUML-";
	private static final String EDITOR_URL = "/webapps/editor.html";

	public BrowserCtrl(BrowserEditor editor, Composite parent) {
		super(editor, parent);
	}

	@Override
	public String getAppPrefix() {
		return APP_PREFIX;
	}

	@Override
	public String getEditorUrl() {
		return EDITOR_URL;
	}
}
