package org.kotemaru.eclipse.genericeditor;

import org.eclipse.swt.widgets.Composite;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserCtrl;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserEditor;


public class BrowserCtrl extends AbstractBrowserCtrl {
	private  String appPrefix;

	public BrowserCtrl(AbstractBrowserEditor editor, Composite parent) {
		super(editor, parent);
		appPrefix = Activator.getMe().getTitle()+"-";
	}

	@Override
	public String getAppPrefix() {
		return appPrefix;
	}

	@Override
	public String getEditorUrl() {
		return Activator.getMe().getEditorUrl();
	}
}
