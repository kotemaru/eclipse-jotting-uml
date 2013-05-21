package org.kotemaru.eclipse.jottinguml;

import org.eclipse.jface.action.Action;

public class JsAction extends Action {
	protected BrowserCtrl browserCtrl;
	protected String script;
	
	public JsAction(BrowserCtrl browserCtrl, String script) {
		this.browserCtrl = browserCtrl;
		this.script = script;
	}
	public void run() {
		browserCtrl.getBrowser().execute(script);
	}
}
