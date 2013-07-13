package org.kotemaru.eclipse.browsereditor.generic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.runtime.IConfigurationElement;



public class GenericConfig  {

	public static final String PLUGIN_ID = "Plugin.id";
	public static final String PLUGIN_EXT = "Plugin.ext";
	public static final String PREFERENCE = "Preference";

	private static Properties props = new Properties();
	static {
		try {
			load("/webapps/editor.properties");
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	
	private static void load(String resName) throws IOException {
		InputStream in = GenericConfig.class.getResourceAsStream(resName);
		try {
			props.load(in);
		} finally {
			in.close();
		}
	}
	
	public static void setup(IConfigurationElement elem) {
		String name = elem.getAttribute("name");
		String ext = elem.getAttribute("extensions");
		setDefault(PLUGIN_ID, elem.getAttribute("id"));
		setDefault(PLUGIN_EXT, ext);
		setDefault("NewWizard.title", name);
		setDefault("NewWizard.initFile", "/webapps/init."+ext);
		setDefault("Editor.appPrefix", name+"-");
		setDefault("Editor.url", "/webapps/editor.html");
	}
	private static void setDefault(String key, String val) {
		if (props.get(key) == null) {
			props.setProperty(key, val);
		}
	}

	public static Properties getProperties() {
		return props;
	}
	public static String get(String key) {
		return props.getProperty(key);
	}
	
	
	
}
