package org.kotemaru.eclipse.browsereditor.generic;

import org.kotemaru.eclipse.browsereditor.AbstractActivator;
import org.kotemaru.eclipse.browsereditor.AbstractPreference;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractActivator {

	private static Preference preference = new Preference();
	
	@Override
	public String getEditorId() {
		return GenericConfig.get("Editor.id");
	}

	@Override
	public AbstractPreference getPreference() {
		return preference;
	}


}
