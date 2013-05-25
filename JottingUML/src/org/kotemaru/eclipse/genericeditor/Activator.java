package org.kotemaru.eclipse.genericeditor;

import org.eclipse.core.runtime.IConfigurationElement;
import org.kotemaru.eclipse.browsereditor.AbstractActivator;
import org.kotemaru.eclipse.browsereditor.AbstractPreference;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.kotemaru.eclipse.genericeditor";

	private static Preference preference = new Preference();

	private String extention;
	private String title;
	private String desc = "";
	private String initData = "";
	private String editorUrl = "/webapps/editor.html";

	public static Activator getMe() {
		return (Activator) getDefault();
	}
	public void config(IConfigurationElement config) {
		String[] exts = config.getAttribute("extensions").split(",");
		setExtention(config.getAttribute(exts[0]));
		setTitle(config.getAttribute("name"));
		
		IConfigurationElement[] preferences = config.getChildren("preference");
		Preference.config(preferences);

		IConfigurationElement[] wizard = config.getChildren("wizard");

	}
	
	@Override
	public String getPluginId() {
		return PLUGIN_ID;
	}

	@Override
	public AbstractPreference getPreference() {
		return preference;
	}

	//------------------------------------------------------------------------
	// Setter/Getter
	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getInitData() {
		return initData;
	}

	public void setInitData(String initData) {
		this.initData = initData;
	}

	public String getEditorUrl() {
		return editorUrl;
	}

	public void setEditorUrl(String editorUrl) {
		this.editorUrl = editorUrl;
	}

	public static void setPreference(Preference preference) {
		Activator.preference = preference;
	}

}
