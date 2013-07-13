package org.kotemaru.eclipse.browsereditor.generic;

import org.eclipse.swt.widgets.Composite;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserCtrl;
import org.kotemaru.eclipse.browsereditor.AbstractBrowserEditor;


public class BrowserEditor extends AbstractBrowserEditor {

	@Override
	public AbstractBrowserCtrl createBrowserCtrl(AbstractBrowserEditor editor, Composite parent) {
		GenericConfig.setup(getConfigurationElement());
		return new BrowserCtrl(editor, parent);
	}
}
