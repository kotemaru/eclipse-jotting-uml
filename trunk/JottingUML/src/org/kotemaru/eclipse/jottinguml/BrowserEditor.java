package org.kotemaru.eclipse.jottinguml;

import org.eclipse.swt.widgets.Composite;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserCtrl;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserEditor;


public class BrowserEditor extends AbstractBrowserEditor {

	@Override
	public AbstractBrowserCtrl createBrowserCtrl(AbstractBrowserEditor editor, Composite parent) {
		
		String ext = this.getConfigurationElement().getAttribute("extensions");
		this.log("ext="+ext);
		
		return new BrowserCtrl(editor, parent);
	}
}
