package org.kotemaru.eclipse.browsereditor.generic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.runtime.IConfigurationElement;



public class GenericConfig  {

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
	
	public static void configEditor(IConfigurationElement elem) {
		String name = elem.getAttribute("name");
		String ext = elem.getAttribute("extensions");
		setDefault("Editor.id", elem.getAttribute("id"));
		setDefault("Editor.ext", ext);
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
