package org.kotemaru.eclipse.genericeditor;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.widgets.Composite;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserCtrl;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserEditor;


public class BrowserEditor extends AbstractBrowserEditor {

	public BrowserEditor() {
		
		IConfigurationElement config = this.getConfigurationElement();
		Activator plugin = Activator.getMe();
		String[] exts = config.getAttribute("extensions").split(",");
		plugin.setExtention(config.getAttribute(exts[0]));
		plugin.setTitle(config.getAttribute("name"));
	}
	
	@Override
	public AbstractBrowserCtrl createBrowserCtrl(AbstractBrowserEditor editor, Composite parent) {
		return new BrowserCtrl(editor, parent);
	}
}
