package org.kotemaru.eclipse.browsereditor;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;

public abstract class AbstractPreference extends AbstractPreferenceInitializer {

	public static class Item {
		public String key;
		public Class<?> type;
		public Object defaultValue;
		
		public Item(String k, Class<?> t, Object v) {
			key = k;
			type = t;
			defaultValue = v;
		}
	}
	
	public abstract Item[] getItems();
	
	public void initializeDefaultPreferences() {
		IPreferenceStore store = AbstractActivator.getDefault().getPreferenceStore();
		Item[] items = getItems();
		for (int i=0; i<items.length; i++) {
			if (items[i].type == String.class) {
				store.setDefault(items[i].key, (String)items[i].defaultValue);
			} else if (items[i].type == Boolean.class) {
				store.setDefault(items[i].key, StringConverter.asString((Boolean)items[i].defaultValue));
			} else if (items[i].type == Integer.class) {
				store.setDefault(items[i].key, StringConverter.asString((Integer)items[i].defaultValue));
			}
		}
	}
	
	public static String toJson () {
		IPreferenceStore store = AbstractActivator.getDefault().getPreferenceStore();
		Item[] items = AbstractActivator.getDefault().getPreference().getItems();
		String json = "";
		for (int i = 0; i < items.length; i++) {
			String key = items[i].key;
			if (items[i].type == String.class) {
				json += key + ":'" + store.getString(key) + "',";
			} else {
				json += key + ":" + store.getString(key) + ",";
			}
		}
		return json;
	}

	public static void save(AbstractBrowserCtrl browser) throws IOException {
		IPreferenceStore store = AbstractActivator.getDefault().getPreferenceStore();
		Item[] items = AbstractActivator.getDefault().getPreference().getItems();
		
		for (int i = 0; i < items.length; i++) {
			String key = items[i].key;
			String val = browser.getPreference(key);
			store.setValue(key, val);
		}
		((IPersistentPreferenceStore) store).save();
	}
	
}