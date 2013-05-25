package org.kotemaru.eclipse.browsereditor;

import org.eclipse.jface.action.Action;

public class JsAction extends Action {
	protected AbstractBrowserCtrl browserCtrl;
	protected String script;
	
	public JsAction(AbstractBrowserCtrl browserCtrl, String script) {
		this.browserCtrl = browserCtrl;
		this.script = script;
	}
	public void run() {
		browserCtrl.getBrowser().execute(script);
	}
}
