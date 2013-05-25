package org.kotemaru.eclipse.browsereditor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.security.storage.EncodingUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;

public abstract class AbstractBrowserCtrl implements StatusTextListener {
	private static final String ENCODING = "utf-8";

	private BrowserEditor editor;
	private Browser browser;
	private boolean isReady = false;
	
	private Action undoAction   ;
	private Action redoAction   ;
	
	// Edit stack
	private int undoHistoryCount = 0;
	private int redoHistoryCount = 0;
	private int savedHistoryCount = 0;
	
	// Clip board
	private Clipboard clipboard = null;
	private String clipPrefix;
	private String lastClip = "";

	
	public abstract String getAppPrefix();
	public abstract String getEditorUrl();

	
	public AbstractBrowserCtrl(BrowserEditor editor, Composite parent) {
		this.editor = editor;
		initBrowser(parent);
		
		Display display = Display.getCurrent();
		this.clipboard = new Clipboard(display);
		this.clipPrefix = getAppPrefix()+System.identityHashCode(editor)+":";

		this.undoAction   = getAction("undo");
		this.redoAction   = getAction("redo");
		this.undoAction.setEnabled(false);
		this.redoAction.setEnabled(false);
	}
	
	private void initBrowser(Composite parent) {
		try {
			browser = new Browser(parent, SWT.NONE);
			browser.setJavascriptEnabled(true);
			browser.addStatusTextListener(this);

			URL aboutURL = this.getClass().getResource(getEditorUrl());
			URL url = FileLocator.resolve(aboutURL);
			browser.setUrl(url.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Browser getBrowser() {
		return browser;
	}
	
	public Action getAction(String command) {
		Action action = new JsAction(this, "Eclipse."+command+"()");
		action.setEnabled(true);
		return action;
	}
	public Action getUndoAction() {
		return undoAction;
	}
	public Action getRedoAction() {
		return redoAction;
	}
	
	@Override
	public void changed(StatusTextEvent ev) {
		try {
			log("status=" + ev.text);
			if (isReady && ev.text.isEmpty()) {
				onIdol();
				return;
			}
			
			String[] params = ev.text.split(",");
			String method = params[0];
			if ("load".equals(method)) {
				onLoad(params);
				isReady = true;
			} else if ("change".equals(method)) {
				onChange(params, false);
			} else if ("undo".equals(method)) {
				onChange(params, true);
			} else if ("redo".equals(method)) {
				onChange(params, true);
			} else if ("syncPreferences".equals(method)) {
				onSyncPreferences(params);
			} else if ("copyClipboard".equals(method)) {
				onCopyClip(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onLoad(String[] params) {
		startup();
		
		try {
			IFileEditorInput input = (IFileEditorInput)editor.getEditorInput();
			InputStream in = input.getFile().getContents();
			try {
				browser.execute("Eclipse.openContent()");
				Reader r = new InputStreamReader(in,ENCODING);
				char[] buff = new char[4096];
				int n;
				while ((n=r.read(buff))>=0) {
					byte[] plain = new String(buff,0,n).getBytes(ENCODING);
					String base64 = EncodingUtils.encodeBase64(plain);
					browser.execute("Eclipse.addContent('"+base64+"')");
				}
				browser.execute("Eclipse.closeContent()");
			} catch (Exception e) {
				browser.execute("Eclipse.failContent('"+e+"')");
				throw e;
			} finally {
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
  
	private void startup() {
		String json = AbstractPreference.toJson();
		browser.execute("Eclipse.startup({" + json + "})");
	}

	private void onSyncPreferences(String[] params) {
		try {
			AbstractPreference.save(this);
		} catch (Exception e) {
			log("Error:" + e);
		}
	}
	public String getPreference(String key) {
		String val = (String) browser.evaluate("return Eclipse.getPreferences('" + key + "')");
		return val;
	}

	private void onChange(String[] params, boolean isUndoRedo) {
		undoHistoryCount = Integer.valueOf(params[1]);
		redoHistoryCount = Integer.valueOf(params[2]);
		undoAction.setEnabled(undoHistoryCount>0);
		redoAction.setEnabled(redoHistoryCount>0);
		if (!isUndoRedo && redoHistoryCount == 0 
				&& savedHistoryCount>=undoHistoryCount) {
			savedHistoryCount = -1;
		}
		
		editor.updateDirty();
	}
	
	private void onCopyClip(String[] params) {
		String textData = (String) browser.evaluate("return Eclipse.getClipboard();");
		if (textData == null) return;
		textData = clipPrefix+textData;
		TextTransfer textTransfer = TextTransfer.getInstance();
		clipboard.setContents(new Object[] { textData },
				new Transfer[] { textTransfer });
	}
	
	private void onIdol() throws UnsupportedEncodingException {
		syncClipboard();
	}

	private void syncClipboard() throws UnsupportedEncodingException {
		TextTransfer transfer = TextTransfer.getInstance();
		String textData = (String) clipboard.getContents(transfer);
		if (textData != null 
			&& textData.startsWith(getAppPrefix()) 
			&& !textData.startsWith(clipPrefix)
			&& !textData.equals(lastClip)) {
			log("update clipboard");
			lastClip = textData;
			textData = textData.substring(textData.indexOf(':')+1);
			String base64 = EncodingUtils.encodeBase64(textData.getBytes(ENCODING));
			browser.evaluate("Eclipse.setClipboard('"+base64+"');");
		}
	}

	public void doSave(IProgressMonitor monitor, IFile file) {
		String content = (String) browser.evaluate("return Eclipse.getContent();");
		try {
			InputStream in = new ByteArrayInputStream(content.getBytes(ENCODING));
			if (file.exists()) {
				file.setContents(
					in,
					true, // keep saving, even if IFile is out of sync with the Workspace
					false, // dont keep history
					monitor); // progress monitor
			} else {
				file.create(
					in,
					true, // keep saving, even if IFile is out of sync with the Workspace
					monitor); // progress monitor
			}
			savedHistoryCount = undoHistoryCount;
			editor.updateDirty();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void log(String msg) {
		System.out.println(msg); // TODO:
	}

	public void dispose() {
		if (browser != null && !browser.isDisposed()) {
			browser.close();
		}
	}

	public boolean isDirty() {
		return undoHistoryCount != savedHistoryCount;
	}



}
