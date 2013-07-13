package org.kotemaru.eclipse.browsereditor.generic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.kotemaru.eclipse.browsereditor.AbstractPreference;

public class Preference extends AbstractPreference {
	public static final String PREFERENCE_ = "Preference.";
	private static Item[] items = null;
	
	@Override
	public Item[] getItems() {
		if (items != null) return items;
		
		List<Item> list = new ArrayList<Item>();
		Iterator<Entry<Object, Object>> ite 
				= GenericConfig.getProperties().entrySet().iterator();
		while (ite.hasNext()) {
			Entry<Object, Object> ent = ite.next();
			String key = (String) ent.getKey();
			if (key.startsWith(PREFERENCE_)) {
				String name = key.substring(PREFERENCE_.length());
				String val = (String) ent.getValue();
				Class<?> type = String.class;
				Object value = val;
				if (val.startsWith("(boolean)")) {
					type = Boolean.class;
					value = Boolean.valueOf(val.substring(9).trim());
				} else if (val.startsWith("(int)")) {
					type = Integer.class;
					value = Integer.valueOf(val.substring(5).trim());
				}
				list.add(new Item(name, type, value));
			}
		}
		items = list.toArray(new Item[list.size()]);
		return items;
	}
}